package com.harunichi.chat.vo;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component("chatVo")
public class chatVo {

	private int chatId; 			//채팅방 고유 ID
	private String recv;			//받는 사람
	private String send;			//보낸 사람
	private Timestamp recvDate;		//읽은 날짜
	private Timestamp sendDate;		//보낸 날짜
	private String chatCont;		//채팅 내용
	private String chatImg;			//사진명
	
}
