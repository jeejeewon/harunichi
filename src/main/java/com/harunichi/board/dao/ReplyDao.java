package com.harunichi.board.dao;

public interface ReplyDao {
	
	// 특정 게시글에 속한 모든 댓글 삭제
	public int deleteRepliesByBoardId(int boardId) throws Exception;

}
