package com.harunichi.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harunichi.chat.dao.ChatDao;
import com.harunichi.chat.vo.ChatVo;


public class ChatService {

	@Autowired
	ChatDao chatDao;
	
	public void saveMessage(ChatVo chatMsg) {
		
		//DAO로 DB작업 요청시키기
		
	}
	
	
}
