package com.harunichi.board.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.harunichi.board.vo.BoardReplyVo;
import com.harunichi.board.vo.BoardVo;

@Repository("boardReplyDao")
public class BoardReplyImpl implements BoardReplyDao { 

	@Autowired
	private SqlSession sqlSession;

	@Override
	public BoardReplyVo selectBoardReplyById(String replyId) throws DataAccessException {

		BoardReplyVo boardReplyVo = sqlSession.selectOne("mapper.board.selectBoardReplyById", replyId);

		return boardReplyVo;
	}


	@Override
	public void insertReply(BoardReplyVo reply) throws DataAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public int updateReply(BoardReplyVo reply) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteReply(int replyId) throws DataAccessException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateReplyLike(int replyId) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int selectReplyCount(int boardId) throws DataAccessException  {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public List<BoardReplyVo> selectReplyList(int boardId) {
		// TODO Auto-generated method stub
		return null;
	}

}