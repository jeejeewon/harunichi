package com.harunichi.chat.vo;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ChatVo {

	int messageId;			//메시지 고유 ID
	String roomId;			//채팅방 ID
	String senderId; 		//보낸 사람 ID
	String receiverId; 		//받는 사람 ID (개인 채팅일 때만)
	String chatType;		//채팅 종류 (개인:personal, 단체:group)
	String message;			//채팅 내용
	String chatFile; 		//첨부 파일 경로 
	boolean isRead;			//읽음 여부 (0/false: 안읽음, 1/true: 읽음)
	boolean isDeleted; 		//삭제 여부 (0/false: 안 지움, 1/true: 삭제됨)
	boolean isReported;		//신고 여부 (0/false: 정상, 1/true: 신고됨)
	Timestamp sentTime;		//보낸 시간
	Timestamp receivedTime; //받은 시간 (안읽음 NULL, 읽는 순간 시간 들어가도록)

}
