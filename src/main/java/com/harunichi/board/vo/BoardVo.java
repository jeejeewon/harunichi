package com.harunichi.board.vo;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class BoardVo {
	private int boardId;
	private String boardCont;
	private Timestamp boardDate;
	private String boardImg;
	private int boardLike;
	private int boardCount;
	private int boardRe;
}
