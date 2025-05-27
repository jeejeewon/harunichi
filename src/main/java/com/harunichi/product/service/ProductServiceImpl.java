package com.harunichi.product.service;

import com.harunichi.product.dao.ProductDao;
import com.harunichi.product.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public List<ProductVo> findAll() throws Exception {
        return productDao.findAll();
    }

    @Override
    public ProductVo findById(int productId) throws Exception {
        return productDao.findById(productId);
    }

    @Override
    public void insert(ProductVo product) throws Exception {
        productDao.insert(product);
    }

    @Override
    public void update(ProductVo product) throws Exception {
        productDao.update(product);
    }

    @Override
    public void delete(int productId) throws Exception {
        productDao.delete(productId);
    }

    @Override
    public void incrementViewCount(int productId) throws Exception {
        productDao.incrementViewCount(productId);
    }
}
