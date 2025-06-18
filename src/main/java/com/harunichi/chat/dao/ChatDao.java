package com.harunichi.chat.dao;

import java.util.List;
import java.util.Map;

import com.harunichi.chat.vo.ChatRoomVo;
import com.harunichi.chat.vo.ChatVo;
import com.harunichi.member.vo.MemberVo;

public interface ChatDao {

	//채팅 메세지 DB에 저장
	void saveMessage(ChatVo chatMsg);

	//친구 추천 리스트 조회	
	List<MemberVo> selectMembers(String id);

	//친구 추천 리스트 조회(비로그인)
	List<MemberVo> selectRandomMembers();

	//DB에서 채팅방 ID 조회
	String selectRoomId(String senderId, String receiverId, String chatType);

	//DB의 chatRoom테이블에 roomId 저장
	void insertRoomId(Map<String, Object> roomMap);

	//DB에서 채팅 내역 조회	
	List<ChatVo> selectChatHistory(String roomId);

	//채팅방에 참여하고 있는 유저 조회
	int selectUserCount(String roomId);

	//채팅방 타이틀 조회(단체)
	String selectTitle(String roomId);

	//채팅방 타이틀 조회(개인)
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
