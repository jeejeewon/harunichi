package com.harunichi.board.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate; // SqlSessionTemplate 임포트
import org.springframework.beans.factory.annotation.Autowired; // 의존성 주입을 위한 어노테이션
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository; // 이 클래스가 DAO 역할을 한다는 걸 알려주는 어노테이션

import com.harunichi.board.vo.BoardVo;
import com.harunichi.member.vo.MemberVo; 

@Repository("boardDao") 
public class BoardDaoImpl implements BoardDao { // BoardDao 인터페이스 구현

	@Autowired
	private SqlSession sqlSession;
	
	@Override 
	public BoardVo selectBoardById(String boardId) throws DataAccessException {
		
		BoardVo boardVo = sqlSession.selectOne("mapper.board.selectBoardById", boardId);

		return boardVo; 
	}

	@Override
	public void insertBoard(BoardVo boardVo) throws DataAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BoardVo selectBoard(int boardId) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BoardVo> selectBoardList() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateBoard(BoardVo board) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteBoard(int boardId) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateBoardCount(int boardId) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateBoardLike(int boardId) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}	

}
