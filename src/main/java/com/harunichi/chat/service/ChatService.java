package com.harunichi.chat.service;

import java.util.List;

import com.harunichi.chat.vo.ChatRoomVo;
import com.harunichi.chat.vo.ChatVo;
import com.harunichi.member.vo.MemberVo;

public interface ChatService {

	void saveMessage(ChatVo chatMsg);

	List<MemberVo> selectMembers(String id);

	String selectRoomId(String senderId, String receiverId, String chatType);

	String insertRoomId(ChatRoomVo vo);

	List<ChatVo> selectChatHistory(String roomId);

	int selectUserCount(String roomId);

	String selectTitle(String roomId);

	MemberVo selectNick(String receiverId);

	List<ChatRoomVo> selectOpenChat();

	List<ChatVo> selectMyChat(String id);

}
