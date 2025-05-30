package com.harunichi.board.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.harunichi.board.vo.BoardVo;

public interface BoardDao {
	
    // 게시글 조회
    public BoardVo selectBoardById(String boardId) throws DataAccessException;
	
	// 새로운 게시글 저장하기
    public void insertBoard(BoardVo board) throws DataAccessException;

    // 게시글 하나 상세 정보 가져오기 (boardId로 찾기)
    public BoardVo selectBoard(int boardId) throws DataAccessException;

    // 전체 게시글 목록 가져오기 (나중엔 페이징 처리도 하겠지만 일단 목록만!)
    public List<BoardVo> selectBoardList() throws DataAccessException;

    // 게시글 내용 수정하기
    public int updateBoard(BoardVo board) throws DataAccessException;

    // 게시글 삭제하기 (boardId로 삭제)
    public int deleteBoard(int boardId) throws DataAccessException;

    // 게시글 조회수 1 올리기
    public int updateBoardCount(int boardId) throws DataAccessException;

    // 게시글 좋아요 수 1 올리기
    public int updateBoardLike(int boardId) throws DataAccessException;;


}
