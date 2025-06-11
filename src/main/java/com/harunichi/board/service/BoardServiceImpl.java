package com.harunichi.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harunichi.board.dao.BoardDao;
import com.harunichi.board.vo.BoardVo;

@Service("BoardService")
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardDao boardDao;

	@Override
	public List<BoardVo> selectBoardList() throws Exception {
		return boardDao.selectBoardList();
	}

	@Override
	public void insertBoard(BoardVo boardVo) throws Exception {
		boardDao.insertBoard(boardVo);
	}

	@Override
	public void updateBoard(BoardVo boardVo) throws Exception {
		boardDao.updateBoard(boardVo);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED) // 이 메소드 전체를 하나의 트랜잭션으로 묶음
	public BoardVo getBoardById(int boardId) throws Exception {
		// 1. 해당 boardId의 게시글 조회수를 1 증가
		incrementBoardCount(boardId);

		// 2. 조회수가 증가된 최신 게시글 정보를 가져와서 리턴
		return boardDao.getBoardById(boardId); 
	}

	// 조회수 증가 메소드 
	// getBoardById 메소드에서 내부로 호출
	@Override
	public void incrementBoardCount(int boardId) throws Exception {	
		boardDao.incrementBoardCount(boardId); 
	}
	
	// 조회수 증가 없이 게시글 정보만 가져오는 메소드
    @Override
    public BoardVo getBoardByIdWithoutIncrement(int boardId) throws Exception {
        return boardDao.getBoardByIdWithoutIncrement(boardId);
    }	

}
