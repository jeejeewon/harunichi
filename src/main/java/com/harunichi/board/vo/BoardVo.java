package com.harunichi.board.vo;

import java.sql.Timestamp;

public class BoardVo {
	private int boardId;
	private String boardCont;
	private Timestamp boardDate;
	private String boardImg;
	private int boardLike;
	private int boardCount;
	private int boardRe;
	public int getBoardId() {
		return boardId;
	}
	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	public String getBoardCont() {
		return boardCont;
	}
	public void setBoardCont(String boardCont) {
		this.boardCont = boardCont;
	}
	public Timestamp getBoardDate() {
		return boardDate;
	}
	public void setBoardDate(Timestamp boardDate) {
		this.boardDate = boardDate;
	}
	public String getBoardImg() {
		return boardImg;
	}
	public void setBoardImg(String boardImg) {
		this.boardImg = boardImg;
	}
	public int getBoardLike() {
		return boardLike;
	}
	public void setBoardLike(int boardLike) {
		this.boardLike = boardLike;
	}
	public int getBoardCount() {
		return boardCount;
	}
	public void setBoardCount(int boardCount) {
		this.boardCount = boardCount;
	}
	public int getBoardRe() {
		return boardRe;
	}
	public void setBoardRe(int boardRe) {
		this.boardRe = boardRe;
	}
}
