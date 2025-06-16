package com.harunichi.product.vo;

import lombok.Data;

@Data
public class ProductLikeVo {

    private int productId;       // 상품 ID
    private String likeUserId;   // 좋아요 누른 회원 ID

}
