package com.example.springdataredis.service;

import com.example.springdataredis.vo.Keyword;
import com.example.springdataredis.vo.Product;
import com.example.springdataredis.vo.ProductGrp;

import java.util.Set;

public interface LowestPriceService {

    Set getZsetValue(String key);

    Set getZsetValueWithStatus(String key) throws Exception;

    Set getZsetValueWithSpecificException(String key) throws Exception;

    int setNewProduct(Product newProduct);

    int setNewProductGroup(ProductGrp newProductGrp);

    int setNewProductGrpToKeyword(String keyword, String prodGrpId, double score);

    Keyword getLowestPriceProductByKeyword(String keyword);
}
