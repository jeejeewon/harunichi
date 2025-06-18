package com.harunichi.chat.service;

import java.util.List;

import com.harunichi.chat.vo.ChatRoomVo;
import com.harunichi.chat.vo.ChatVo;
import com.harunichi.member.vo.MemberVo;

public interface ChatService {

	//채팅 메세지 DB에 저장
	void saveMessage(ChatVo chatMsg);

	//친구 추천 리스트 조회
	List<MemberVo> selectMembers(String id);

	//채팅방 ID 조회
	String selectRoomId(String senderId, String receiverId, String chatType);

	//채팅방 ID DB에 저장
	String insertRoomId(ChatRoomVo vo);

	//과거 채팅 내역 불러오기
	List<ChatVo> selectChatHistory(String roomId);

	//채팅방 참여 인원 확인
	int selectUserCount(String roomId);

	//채팅방 타이틀 확인(단체채팅 - 방장이 정한 타이틀)
	String selectTitle(String roomId);

	//채팅방 타이틀 확인(개인채팅 - 상대방 닉네임)
	MemberVo selectNick(String receiverId);

	//오픈 채팅방 리스트 조회
	List<ChatRoomVo> selectOpenChat();

	//참여중인 채팅방 정보 조회
	List<ChatRoomVo> selectMyChatList(String id);
	
	//참여중인 채팅의 메세지 정보 조회
	ChatVo selectMyChatMessage(String roomId);

	//오픈 채팅방 최신 메세지 조회
	String selectMessage(String roomId);


}
