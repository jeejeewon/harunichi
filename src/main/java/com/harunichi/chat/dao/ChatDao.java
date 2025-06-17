package com.harunichi.chat.dao;

import java.util.List;
import java.util.Map;

import com.harunichi.chat.vo.ChatRoomVo;
import com.harunichi.chat.vo.ChatVo;
import com.harunichi.member.vo.MemberVo;

public interface ChatDao {

	void saveMessage(ChatVo chatMsg);

	List<MemberVo> selectMembers(String id);

	List<MemberVo> selectRandomMembers();

	String selectRoomId(String senderId, String receiverId, String chatType);

	void insertRoomId(Map<String, Object> roomMap);

	List<ChatVo> selectChatHistory(String roomId);

	int selectUserCount(String roomId);

	String selectTitle(String roomId);

	MemberVo selectNick(String receiverId);

	List<ChatRoomVo> selectOpenChat();

	List<ChatVo> selectMyChat(String id);

}
