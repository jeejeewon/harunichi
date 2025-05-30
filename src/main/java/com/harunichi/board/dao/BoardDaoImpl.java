package com.harunichi.board.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired; // 의존성 주입을 위한 어노테이션
import org.springframework.stereotype.Repository; // 이 클래스가 DAO 역할을 한다는 걸 알려주는 어노테이션

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.harunichi.board.vo.BoardVo;
import com.harunichi.member.vo.MemberVo;

@Repository("boardDao")
public class BoardDaoImpl implements BoardDao { // BoardDao 인터페이스 구현

	private static final String NAMESPACE = "mapper.board.";

	@Autowired
	private SqlSession sqlSession;

	// 전체 게시글 목록 가져오기
	@Override
	public List<BoardVo> selectBoardList() throws Exception {
		return sqlSession.selectList(NAMESPACE + "selectBoardList");
	}

	// 게시글 등록
	@Override
	public void insertBoard(BoardVo boardVo) throws Exception {
		sqlSession.insert(NAMESPACE + "insertBoard", boardVo);
	}

	@Override
	public BoardVo selectBoardById(int boardId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
