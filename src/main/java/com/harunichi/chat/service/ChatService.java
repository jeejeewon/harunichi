package com.harunichi.chat.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
	List<ChatVo> selectChatHistory(String roomId, String userId);

	//채팅방 참여 인원 확인
	int selectUserCount(String roomId);

	//특정 오픈 채팅방 정보 조회
	ChatRoomVo selectOpenChatById(String roomId);
	
	//나와 채팅 중인 상대방 ID 조회
	String selectChatMemberId(String userId, String roomId);

	//채팅 상대 프로필 정보 조회
	MemberVo selectProfile(String receiverId);

	//오픈 채팅방 리스트 조회
	List<ChatRoomVo> selectOpenChat();

	//참여중인 채팅방 정보 조회
	List<ChatRoomVo> selectMyChatList(String id);
	
	//참여중인 채팅의 메세지 정보 조회
	ChatVo selectMyChatMessage(String roomId);
	
	//로그인 사용자가 참여하려는 채팅방에 이미 참여하고 있는지 확인
	boolean isUserInRoom(String roomId, String userId);

	//오픈 채팅 참여
	void doOpenChat(ChatRoomVo chatRoomVo);

	//채팅방 프로필 이미지 C드라이브에 저장
	String chatProfileImgUpload(MultipartFile file);

	//채팅방 정보에 상품ID 업데이트
	void updateChatProduct(String roomId, int productId);

	//채팅방 정보에 상품ID 제거
	void deleteProductId(String roomId, int productId);

	//채팅방 나가기
	void leaveChatRoom(String userId, String roomId);

	//채팅방 정보와 채팅 내역 삭제
	void deleteChat(String roomId);

	//로그인한 유저가 오픈 채팅방의 리더인지 확인
	boolean isLeader(String roomId, String userId);

	

	



}
