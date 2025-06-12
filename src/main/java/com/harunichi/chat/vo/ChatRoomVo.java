package com.harunichi.chat.vo;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class ChatRoomVo {

	private String roomId;				//채팅방 고유 ID
	private String userId;				//참여 유저 ID
	private String title;				//채팅방 제목
	private boolean leader;				//채팅방 방장
	private int persons;				//채팅방 인원 수 
	private String chatType;			//채팅방 타입 (개인채팅 or 단체채팅)
	private boolean isDeleted; 			//채팅방 삭제 여부 (0/false: 안 지움, 1/true: 삭제됨)
	private Timestamp admissionTime;	//채팅방 입장 시간 (채팅 내역 불러올 때 사용)
	
}
