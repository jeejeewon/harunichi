package com.harunichi.product.dao;

import com.harunichi.product.vo.ProductVo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("productDao")
public class ProductDaoImpl implements ProductDao {

    private static final String NAMESPACE = "mapper.product.";

    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<ProductVo> findAll() throws Exception {
        return sqlSession.selectList(NAMESPACE + "findAll");
    }

    @Override
    public ProductVo findById(int productId) throws Exception {
        return sqlSession.selectOne(NAMESPACE + "findById", productId);
    }

    @Override
    public void insert(ProductVo product) throws Exception {
        sqlSession.insert(NAMESPACE + "insert", product);
    }

    @Override
    public void update(ProductVo product) throws Exception {
        sqlSession.update(NAMESPACE + "update", product);
    }

    @Override
    public void delete(int productId) throws Exception {
        sqlSession.delete(NAMESPACE + "delete", productId);
    }

    @Override
    public void incrementViewCount(int productId) throws Exception {
        sqlSession.update(NAMESPACE + "incrementViewCount", productId);
    }
}
