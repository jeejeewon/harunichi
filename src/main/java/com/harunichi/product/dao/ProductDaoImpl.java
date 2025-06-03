package com.harunichi.product.dao;

import com.harunichi.product.vo.ProductVo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<ProductVo> findPaged(int offset, int pageSize) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("offset", offset);
        paramMap.put("limit", pageSize);
        return sqlSession.selectList(NAMESPACE + "findPaged", paramMap);
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
    
    @Override
    public List<ProductVo> searchFiltered(String keyword, String category, Integer minPrice, Integer maxPrice, int offset, int limit) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("keyword", keyword);
        paramMap.put("category", category);
        paramMap.put("minPrice", minPrice);
        paramMap.put("maxPrice", maxPrice);
        paramMap.put("offset", offset);
        paramMap.put("limit", limit);
        return sqlSession.selectList(NAMESPACE + "searchFiltered", paramMap);
    }

}
