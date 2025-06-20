package com.harunichi.follow.dao;

import java.util.List;

import com.harunichi.follow.vo.FollowVo;

public interface FollowDao {
    List<FollowVo> selectFollowList(String searchKeyword, int offset, int pageSize);
    int selectFollowCount(String searchKeyword);
}