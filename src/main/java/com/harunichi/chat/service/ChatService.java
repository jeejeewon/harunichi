package com.harunichi.chat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harunichi.chat.dao.ChatDao;
import com.harunichi.chat.vo.ChatVo;
import com.harunichi.member.vo.MemberVo;

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

	//DB에서 친구 추천 리스트 조회
	public List<MemberVo> selectMembers(String id) {
		return chatDao.selectMembers(id);
	}
	
	
}
