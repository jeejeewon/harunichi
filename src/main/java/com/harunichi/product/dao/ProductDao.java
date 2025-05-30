package com.harunichi.product.dao;

import com.harunichi.product.vo.ProductVo;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ProductDao {

    List<ProductVo> findAll() throws Exception;

    ProductVo findById(int productId) throws Exception;
    
    List<ProductVo> findPaged(@Param("offset") int offset, @Param("limit") int limit) throws Exception;

    void insert(ProductVo product) throws Exception;

    void update(ProductVo product) throws Exception;

    void delete(int productId) throws Exception;

    void incrementViewCount(int productId) throws Exception;
    
    List<ProductVo> searchFiltered(String keyword, String category, Integer minPrice, Integer maxPrice, int offset, int limit) throws Exception;


}
