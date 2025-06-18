package com.harunichi.chat.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.harunichi.chat.dao.ChatDao;
import com.harunichi.chat.dao.ChatDaoImpl;
import com.harunichi.chat.vo.ChatRoomVo;
import com.harunichi.chat.vo.ChatVo;
import com.harunichi.member.vo.MemberVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

	@Autowired
	private ChatDao chatDao;
	
	//채팅 메세지 DB에 저장
	@Override
	public void saveMessage(ChatVo chatMsg) {		
		System.out.println("---ChatService의 saveMessage메소드 호출");		
		//DAO로 DB작업 요청시키기
		chatDao.saveMessage(chatMsg);
	}

	//친구 추천 리스트 조회
	@Override
	public List<MemberVo> selectMembers(String id) {
		System.out.println("---ChatService의 selectMembers메소드 호출");			
		//비로그인
		if(id == null) { return chatDao.selectRandomMembers();
		//로그인
		}else { return chatDao.selectMembers(id); }	
	}
		
	//채팅방 ID 조회
	@Override
	public String selectRoomId(String senderId, String receiverId,  String chatType) {
		System.out.println("---ChatService의 selectRoomId메소드 호출");		
		
		String roomId = chatDao.selectRoomId(senderId, receiverId, chatType);	
		log.info("roomId : " + roomId);
				
		ChatRoomVo vo = new ChatRoomVo();
		vo.setUserId(senderId);
		vo.setReceiverId(receiverId);
		vo.setChatType(chatType);	
		
		//DB에 조회된 채팅방ID가 없다면?
		if(roomId == null) { roomId = insertRoomId(vo); }	
		
		//DB에서 조회된 채팅방ID 반환
		return roomId;
	}
	
	//채팅방 ID DB에 저장
	@Override
	public String insertRoomId(ChatRoomVo vo) {		
		System.out.println("---ChatService의 insertRoomId메소드 호출");		
		
		Map<String, Object> roomMap = new HashMap<String, Object>();
	
		//새로운 채팅방 아이디 생성
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
		
		//단체 채팅일 경우
		if(vo.getChatType().equals("group")) {			
			roomMap.put("userId", vo.getUserId());
			roomMap.put("title", vo.getTitle());		
			roomMap.put("chatType", vo.getChatType());
			roomMap.put("persons", vo.getPersons());
			//DB의 chatRoom테이블에 채팅방 정보 저장
			chatDao.insertRoomId(roomMap);
		//개인 채팅일 경우
		}else {			
			List<String> userList = new ArrayList<String>();
			userList.add(vo.getReceiverId()); //상대방
			userList.add(vo.getUserId());	  //로그인 사용자
			
			for(String userId : userList) {
				roomMap.put("roomId", newRoomId);
				roomMap.put("userId", userId);		
				roomMap.put("chatType", vo.getChatType());
				
				// 상대방 닉네임을 타이틀로 보여주기 위해, 상대 ID 기준으로 닉네임 조회
				String id = userId.equals(vo.getUserId()) ? vo.getReceiverId() : vo.getUserId();
				String title = chatDao.selectNick(id).getNick();
				roomMap.put("title", title);
				
				chatDao.insertRoomId(roomMap);
			}		
		}		
		return newRoomId;
	}

	//과거 채팅 내역 불러오기
	@Override
	public List<ChatVo> selectChatHistory(String roomId) {
		System.out.println("---ChatService의 selectChatHistory메소드 호출");	
		return chatDao.selectChatHistory(roomId);
	}
	
	//채팅방 참여 인원 확인
	@Override
	public int selectUserCount(String roomId) {		
		System.out.println("---ChatService의 selectUserCount메소드 호출");		
		return chatDao.selectUserCount(roomId);
	}
	
	//채팅방 타이틀 확인(단체채팅 - 방장이 정한 타이틀)
	@Override
	public String selectTitle(String roomId) {
		System.out.println("---ChatService의 selectTitle메소드 호출");
		System.out.println("roomId : " + roomId);
		return chatDao.selectTitle(roomId);
	}
	
	//채팅방 타이틀 확인(개인채팅 - 상대방 닉네임)
	@Override
	public MemberVo selectNick(String receiverId) {
		System.out.println("---ChatService의 selectNick메소드 호출");
		return chatDao.selectNick(receiverId);
	}

	//오픈 채팅방 리스트 조회
	@Override
	public List<ChatRoomVo> selectOpenChat() {	
		System.out.println("---ChatService의 selectOpenChat메소드 호출");
		List<ChatRoomVo> openChatList = chatDao.selectOpenChat();		
		//채팅방 참여 인원 확인
		for(ChatRoomVo vo : openChatList) {
			int count = selectUserCount(vo.getRoomId());
			vo.setUserCount(count);
		}	
		return openChatList;
	}
	
	//참여중인 채팅방 정보 조회
	@Override
	public List<ChatRoomVo> selectMyChatList(String id) {
		System.out.println("---ChatService의 selectMyChatList메소드 호출");
		return chatDao.selectMyChatList(id);
	}
	
	//참여중인 채팅의 메세지 정보 조회
	@Override
	public ChatVo selectMyChatMessage(String roomId) {
		System.out.println("---ChatService의 selectMyChat메소드 호출");
		
		//최신 채팅 정보 조회
		ChatVo myChatMessage = chatDao.selectMyChatMessage(roomId);
						
		try {
			//채팅 목록에 나타낼 최신 메세지 시간 나타내기 위한 반복문
			Timestamp sentTime = myChatMessage.getSentTime();
			
			//Timestamp -> LocalDateTime 데이터타입 변환
			LocalDateTime sentDateTime = sentTime.toLocalDateTime();
			
			LocalDate today = LocalDate.now();
			LocalDate sentDate = sentDateTime.toLocalDate();
			
			//날짜와 시간 포맷팅
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("a hh:mm", Locale.KOREAN); 
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			
			//조건문에서 포맷팅한 날짜와 시간 담을 변수
			String displayTime;
			
			if (sentDate.equals(today)) {
			    displayTime = sentDateTime.format(timeFormatter);
			} else {
			    displayTime = sentDateTime.format(dateFormatter);
			}
			myChatMessage.setDisplayTime(displayTime);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return myChatMessage;		
	}


	
}

