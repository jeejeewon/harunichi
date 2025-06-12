package com.harunichi.chat.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	public String selectRoomId(String senderId, String receiverId,  String chatType) {
		System.out.println("ChatService의 selectRoomId메소드 호출 ===================");		
		
		String roomId = chatDao.selectRoomId(senderId, receiverId, chatType);
		
		Map<String, Object> roomMap = new HashMap<String, Object>();
		
//		String userId = senderId + "," + receiverId;
		
//		roomMap.put("userId", userId);
		
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
			
			roomMap.put("roomId", newRoomId);
			
			List<String> userList = new ArrayList<String>();		
			userList.add(receiverId);
			userList.add(senderId);
			
			roomMap.put("chatType", chatType);
			roomMap.put("userList", userList);
						
			//DB의 chatRoom테이블에 채팅방 정보 저장
			chatDao.insertRoomId(roomMap);
			
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

	
	//채팅방 참여 인원 확인
	public int selectUserCount(String roomId) {		
		return chatDao.selectUserCount(roomId);
	}

	//채팅방 타이틀 확인(단체채팅)
	public String selectTitle(String roomId) {
		return chatDao.selectTitle(roomId);
	}

	//채팅방 타이틀 확인(개인채팅)
	public String selectNick(String receiverId) {
		return chatDao.selectNick(receiverId);
	}



}
