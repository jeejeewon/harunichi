package com.harunichi.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harunichi.chat.dao.ChatDao;
import com.harunichi.chat.vo.ChatVo;

@Service
public class ChatService {

	@Autowired
	private ChatDao chatDao;
	
	//채팅 메세지 DB에 저장
	public void saveMessage(ChatVo chatMsg) {
		
		System.out.println("ChatService의 saveMessage메소드 호출 ===================");
		
		//DAO로 DB작업 요청시키기
		chatDao.saveMessage(chatMsg);
	}
	
	
}
