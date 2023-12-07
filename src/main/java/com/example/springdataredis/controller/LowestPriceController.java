package com.example.springdataredis.controller;

import com.example.springdataredis.service.LowestPriceService;
import com.example.springdataredis.vo.Keyword;
import com.example.springdataredis.vo.NotFoundException;
import com.example.springdataredis.vo.Product;
import com.example.springdataredis.vo.ProductGrp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping
public class LowestPriceController {

    @Autowired private LowestPriceService lowestPriceService;

    @GetMapping("/product")
    public Set getZSetValue(String key) {
        return lowestPriceService.getZsetValue(key);
    }

    @GetMapping("/product1")
    public Set getZSetValueWithStatus(String key) {
        try {
            return lowestPriceService.getZsetValueWithStatus(key);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e); //404, NotFound
        }
    }

    @GetMapping("/product2")
    public Set getZSetValueUsingExceptionController(String key) throws Exception {
        try {
            return lowestPriceService.getZsetValueWithStatus(key);
        } catch (Exception e) {
            throw new Exception(e); //500, Internal Server Error
        }
    }

    @GetMapping("/product3")
    public ResponseEntity<Set> getZSetValueUsingExceptionControllerWithSpecificException(String key) throws Exception {
        Set<String> mySet;
        try {
            mySet = lowestPriceService.getZsetValueWithSpecificException(key);
        } catch (NotFoundException e) {
            throw new Exception(e); //404, Response Body : The Key doesn't exist in redis
        }
        HttpHeaders responseHeaders = new HttpHeaders();

        return new ResponseEntity<Set>(mySet, responseHeaders, HttpStatus.OK);
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
