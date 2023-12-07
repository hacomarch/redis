package com.example.springdataredis.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProductGrp {

    private String prodGrpId; //FPG0001

    //셀러 별 아이디와 가격을 갖는 것
    private List<Product> productList; // [{sdfe234-dfge-sd34d-dfgr21-ghjg5vgh7, 25000}, {}...]
}
