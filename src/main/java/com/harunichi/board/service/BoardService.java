package com.harunichi.board.service;

import java.util.List;

import com.harunichi.board.vo.BoardVo;

public interface BoardService {
	
	List<BoardVo> selectBoardList() throws Exception;
	
	void insertBoard(BoardVo boardVo) throws Exception;	
	
    void updateBoard(BoardVo boardVo) throws Exception; // 게시글 수정 메서드
}
