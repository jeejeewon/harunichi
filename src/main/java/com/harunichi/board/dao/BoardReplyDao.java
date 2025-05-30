package com.harunichi.board.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.harunichi.board.vo.BoardReplyVo;
import com.harunichi.board.vo.BoardVo;

public interface BoardReplyDao {
	// 특정 게시글에 달린 댓글 목록 가져오기
    // 파라미터로 boardId를 받아서 해당 게시글의 댓글들만 가져오기
    public List<BoardReplyVo> selectReplyList(int boardId);

    // 새로운 댓글 저장하기
    // 어느 게시글에 달린 댓글인지 알려줘야 하니 BoardReplyVo 객체 안에 boardId 정보도 필요함
    // 만약 BoardReplyVo에 boardId 변수가 없다면, 별도의 파라미터로 boardId를 받기
    public void insertReply(BoardReplyVo reply);

    // 댓글 내용 수정하기
    // 수정할 댓글 정보(replyId)랑 수정 내용이 BoardReplyVo 객체
    public int updateReply(BoardReplyVo reply);

    // 댓글 삭제하기 (replyId로 삭제)
    public int deleteReply(int replyId);

    // 댓글 좋아요 수 1 올리기
    public int updateReplyLike(int replyId);

    // (추가) 특정 게시글의 총 댓글 개수 가져오기
    public int selectReplyCount(int boardId);
    
    
    // 댓글 목록 가져오기
	public BoardReplyVo selectBoardReplyById(String replyId) throws DataAccessException;
}
