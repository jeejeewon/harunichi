package com.harunichi.board.vo;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class BoardReplyVo {
	private int replyId;
	private String replyCont;
	private Timestamp replyDate;
	private String replyImg;
	private int replyLike;
}
