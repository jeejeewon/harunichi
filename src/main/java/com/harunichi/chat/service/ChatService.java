package com.harunichi.chat.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
	
	//채팅방 ID 조회
	public String selectRoomId(String senderId, String receiverId) {
		System.out.println("ChatService의 selectRoomId메소드 호출 ===================");		
		
		String roomId = chatDao.selectRoomId(senderId, receiverId);
		
		System.out.println("roomId : " + roomId);
		
		//DB에 조회된 채팅방ID가 없다면?
		if(roomId == null) {		
			//현재 날짜
			String now = new SimpleDateFormat("yyyyMMdd").format(new Date());		
			//랜덤 숫자 (0 ~ 999999)
			int randomInt = new Random().nextInt(1_000_000);			
			//6자리로 포맷 (앞에 0 채움)
			String random = String.format("%06d", randomInt);			
			//새로운 채팅방 ID 생성 후 반환
			String newRoomId = now + "_" + random;			
			System.out.println("newRoomId : " + newRoomId);
		
			return newRoomId;		
		}	
		
		//DB에서 조회된 채팅방ID 반환
		return roomId;
	}

	

	//과거 채팅 내역 불러오기
	public List<ChatVo> selectChatHistory(String roomId) {
		System.out.println("ChatService의 selectChatHistory메소드 호출 ===================");	
		return chatDao.selectChatHistory(roomId);
	}
	
	
}
