package com.harunichi.follow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.harunichi.follow.vo.FollowVo;

@Repository("followDao")
public class FollowDaoImpl implements FollowDao {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<FollowVo> selectFollowList(String searchKeyword, int offset, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchKeyword", searchKeyword);
        params.put("offset", offset);
        params.put("pageSize", pageSize);
        return sqlSession.selectList("followMapper.selectFollowList", params);
    }

    @Override
    public int selectFollowCount(String searchKeyword) {
        return sqlSession.selectOne("followMapper.selectFollowCount", searchKeyword);
    }
}
