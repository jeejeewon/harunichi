package com.harunichi.product.service;

import com.harunichi.product.vo.ProductVo;
import java.util.List;

public interface ProductService {

    List<ProductVo> findAll() throws Exception;

    ProductVo findById(int productId) throws Exception;
    
    List<ProductVo> findPaged(int offset, int limit) throws Exception;

    void insert(ProductVo product) throws Exception;

    void update(ProductVo product) throws Exception;

    void delete(int productId) throws Exception;

    void incrementViewCount(int productId) throws Exception;
    
    List<ProductVo> searchFiltered(String keyword, String category, Integer minPrice, Integer maxPrice, int offset, int limit) throws Exception;

    List<ProductVo> findOtherProducts(String writerId, int productId, int offset, int size) throws Exception;

}
