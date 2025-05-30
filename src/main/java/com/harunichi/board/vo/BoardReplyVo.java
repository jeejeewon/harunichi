package com.harunichi.board.vo;

import java.sql.Timestamp;

public class BoardReplyVo {
	private int replyId;
	private String replyCont;
	private Timestamp replyDate;
	private String replyImg;
	private int replyLike;
	public int getReplyId() {
		return replyId;
	}
	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}
	public String getReplyCont() {
		return replyCont;
	}
	public void setReplyCont(String replyCont) {
		this.replyCont = replyCont;
	}
	public Timestamp getReplyDate() {
		return replyDate;
	}
	public void setReplyDate(Timestamp replyDate) {
		this.replyDate = replyDate;
	}
	public String getReplyImg() {
		return replyImg;
	}
	public void setReplyImg(String replyImg) {
		this.replyImg = replyImg;
	}
	public int getReplyLike() {
		return replyLike;
	}
	public void setReplyLike(int replyLike) {
		this.replyLike = replyLike;
	}
}
