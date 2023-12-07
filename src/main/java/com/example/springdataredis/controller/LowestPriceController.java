package com.example.springdataredis.controller;

import com.example.springdataredis.service.LowestPriceService;
import com.example.springdataredis.vo.Keyword;
import com.example.springdataredis.vo.Product;
import com.example.springdataredis.vo.ProductGrp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping
public class LowestPriceController {

    @Autowired private LowestPriceService lowestPriceService;

    @GetMapping("/getZSETValue")
    public Set getZSetValue(String key) {
        return lowestPriceService.getZsetValue(key);
    }

    @PutMapping("/product")
    public int setNewProduct(@RequestBody Product newProduct) {
        return lowestPriceService.setNewProduct(newProduct);
    }

    @PutMapping("/productGroup")
    public int setNewProductGroup(@RequestBody ProductGrp newProductGrp) {
        return lowestPriceService.setNewProductGroup(newProductGrp);
    }

    @PutMapping("/productGroupToKeyword")
    public int setNewProductGrpToKeyword(String keyword, String prodGrpId, double score) {
        return lowestPriceService.setNewProductGrpToKeyword(keyword, prodGrpId, score);
    }

    @GetMapping("/productPrice/lowest")
    public Keyword getLowestPriceProductByKeyword(String keyword) {
        return lowestPriceService.getLowestPriceProductByKeyword(keyword);
    }
}
