package com.harunichi.product.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ProductVo {
	
    private int productId;
    private String productTitle;
    private int productPrice;
    private int productStatus;
    private String productCategory;
    private String productContent;
    private Timestamp productDate;
    private String productImg;
    private int productCount;
    private String productWriterId;
    
}
