package com.harunichi.product.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.harunichi.product.vo.ProductLikeVo;

@Repository
public class ProductLikeDaoImpl implements ProductLikeDao {

    @Autowired
    private SqlSession sqlSession;

    private static final String NAMESPACE = "mapper.productLike.";

    @Override
    public boolean isLiked(ProductLikeVo likeVo) {
        Integer count = sqlSession.selectOne(NAMESPACE + "isLiked", likeVo);
        return count != null && count > 0;
    }

    @Override
    public void insertLike(ProductLikeVo likeVo) {
        sqlSession.insert(NAMESPACE + "insertLike", likeVo);
    }

    @Override
    public void deleteLike(ProductLikeVo likeVo) {
        sqlSession.delete(NAMESPACE + "deleteLike", likeVo);
    }

    @Override
    public int getLikeCount(int productId) {
        return sqlSession.selectOne(NAMESPACE + "getLikeCount", productId);
    }
}
