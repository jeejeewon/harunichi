package com.harunichi.product.dao;

import com.harunichi.product.vo.ProductVo;
import java.util.List;

public interface ProductDao {

    List<ProductVo> findAll() throws Exception;

    ProductVo findById(int productId) throws Exception;

    void insert(ProductVo product) throws Exception;

    void update(ProductVo product) throws Exception;

    void delete(int productId) throws Exception;

    void incrementViewCount(int productId) throws Exception;
}
