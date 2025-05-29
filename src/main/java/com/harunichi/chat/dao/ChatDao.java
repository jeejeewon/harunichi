package com.harunichi.chat.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.harunichi.chat.vo.ChatVo;

@Repository
public class ChatDao {

	@Autowired
	private SqlSession sqlSession;
	
	
	private static final String NAMESPACE = "mapper.chat.";

	
	public void saveMessage(ChatVo chatMsg) {		
		sqlSession.insert(NAMESPACE + "saveMessage", chatMsg);			
	}

	
	
	
}
