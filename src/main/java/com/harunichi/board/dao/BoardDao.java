package com.harunichi.board.dao;

import java.util.List;

import com.harunichi.board.vo.BoardVo;

public interface BoardDao {

    // boardId로 조회
    BoardVo selectBoardById(int boardId) throws Exception;
	
    // 전체 게시글 목록 가져오기
    List<BoardVo> selectBoardList() throws Exception; 
    
    // 게시글 등록
    void insertBoard(BoardVo boardVo) throws Exception;

}
