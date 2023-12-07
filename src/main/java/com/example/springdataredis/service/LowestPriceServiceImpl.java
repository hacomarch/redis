package com.example.springdataredis.service;

import com.example.springdataredis.vo.Keyword;
import com.example.springdataredis.vo.NotFoundException;
import com.example.springdataredis.vo.Product;
import com.example.springdataredis.vo.ProductGrp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LowestPriceServiceImpl implements LowestPriceService {

    @Autowired private RedisTemplate myProductPriceRedis;

    @Override
    public Set getZsetValue(String key) {
        //redis의 zset에서 key의 0~9까지의 값을 가져와 set으로 변환
        return myProductPriceRedis.opsForZSet().rangeWithScores(key, 0, 9);
    }

    @Override
    public Set getZsetValueWithStatus(String key) throws Exception {
        Set myTempSet = myProductPriceRedis.opsForZSet().rangeWithScores(key, 0, 9);
        if (myTempSet.size() < 1) {
            throw new Exception("The Key doesn't have any member");
        }
        return myTempSet;
    }

    @Override
    public Set getZsetValueWithSpecificException(String key) {
        Set myTempSet = myProductPriceRedis.opsForZSet().rangeWithScores(key, 0, 9);
        if (myTempSet.size() < 1) {
            throw new NotFoundException("The Key doesn't exist in redis", HttpStatus.NOT_FOUND);
        }
        return myTempSet;
    }

    @Override
    public int setNewProduct(Product newProduct) {
        int rank = 0;
        //zset에 새 제품 추가
        myProductPriceRedis.opsForZSet().add(newProduct.getProdGrpId(), newProduct.getProductId(), newProduct.getPrice());
        //해당 제품의 순위를 얻어와 저장
        rank = myProductPriceRedis.opsForZSet().rank(newProduct.getProdGrpId(), newProduct.getProductId()).intValue();
        return rank;
    }

    @Override
    public int setNewProductGroup(ProductGrp newProductGrp) {
        List<Product> productList = newProductGrp.getProductList();
        String productId = productList.get(0).getProductId();
        int price = productList.get(0).getPrice();

        myProductPriceRedis.opsForZSet().add(newProductGrp.getProdGrpId(), productId, price);

        return myProductPriceRedis.opsForZSet().zCard(newProductGrp.getProdGrpId()).intValue(); //counting
    }

    @Override
    public int setNewProductGrpToKeyword(String keyword, String prodGrpId, double score) {
        myProductPriceRedis.opsForZSet().add(keyword, prodGrpId, score); //키워드가 없을 때 새로 추가

        return myProductPriceRedis.opsForZSet().rank(keyword, prodGrpId).intValue(); //키워드가 있다면 rank 반환
    }

    @Override
    public Keyword getLowestPriceProductByKeyword(String keyword) {
        Keyword newKeyword = new Keyword();
        List<ProductGrp> tempProductGrp = getProdGrpUsingKeyword(keyword);

        newKeyword.setKeyword(keyword);
        newKeyword.setProductGrpList(tempProductGrp);

        return newKeyword;
    }

    public List<ProductGrp> getProdGrpUsingKeyword(String keyword) {
        List<ProductGrp> resultWithProdGrpList = new ArrayList<>();

        //엘라스틱 서치에서 매칭 score 가 1-2 사이인데 2와 가까울수록 매칭이 잘 되는 것이기 때문에
        //score가 높은 순서로 정렬한 그룹 아이디들을 가져온다.
        List<String> prodGrpIdList = List.copyOf(myProductPriceRedis.opsForZSet().reverseRange(keyword, 0, 9));
        List<Product> tempProdList = new ArrayList<>();

        for (final String prodGrpId : prodGrpIdList) {
            Set prodAndPriceList = myProductPriceRedis.opsForZSet().rangeWithScores(prodGrpId, 0, 9);
            ProductGrp tempProdGrp = new ProductGrp();

            //각 제품에 대해 반복
            Iterator<Object> prodPriceObj = prodAndPriceList.iterator();
            while (prodPriceObj.hasNext()) {
                //제품 정보와 가격을 맵 형식으로 변환
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> prodPriceMap = objectMapper.convertValue(prodPriceObj.next(), Map.class); //{"value":00-1210-111},{"score":12000}

                //tempProduct에 제품 id와 가격 저장
                Product tempProduct = new Product();
                tempProduct.setProductId(prodPriceMap.get("value").toString()); //value : product id
                tempProduct.setProdGrpId(prodGrpId);
                tempProduct.setPrice(Double.valueOf(prodPriceMap.get("score").toString()).intValue()); //score : 엘라스틱 서치에서 검색된 score를 바탕으로 가져온 price

                //tempProdList에 tempProduct 추가
                tempProdList.add(tempProduct);
            }

            //tempProdGrp에 현재 제품 그룹 id와 현재 제품 리스트 저장
            tempProdGrp.setProdGrpId(prodGrpId);
            tempProdGrp.setProductList(tempProdList);

            //제품 그룹 리스트에 제품 그룹 저장
            resultWithProdGrpList.add(tempProdGrp);
        }

        return resultWithProdGrpList;
    }
}
