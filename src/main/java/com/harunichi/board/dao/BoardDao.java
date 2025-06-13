package com.harunichi.board.dao;

import java.util.List;

import com.harunichi.board.vo.BoardVo;

public interface BoardDao {

	// 전체 게시글 목록
	List<BoardVo> selectBoardList() throws Exception;

	// 게시글 등록
	void insertBoard(BoardVo boardVo) throws Exception;

	// 게시글 수정
	void updateBoard(BoardVo boardVo) throws Exception;

	// 게시글 상세 조회
	BoardVo getBoardById(int boardId) throws Exception;

	// 조회수 증가
	void incrementBoardCount(int boardId) throws Exception;

	// 조회수 증가 없이 정보만 가져오기
	BoardVo getBoardByIdWithoutIncrement(int boardId) throws Exception;
	
	// 게시글 삭제
	public int deleteBoard(int boardId) throws Exception;

}
