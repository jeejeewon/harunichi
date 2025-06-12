package com.harunichi.board.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harunichi.board.controller.BoardControllerImpl;
import com.harunichi.board.dao.BoardDao;
import com.harunichi.board.dao.ReplyDao;
import com.harunichi.board.vo.BoardVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("BoardService")
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardDao boardDao;

	@Autowired
	private ReplyDao replyDao;

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

	// 게시글 삭제
	@Override
	@Transactional(propagation = Propagation.REQUIRED) // DB 작업에 트랜잭션 적용
	public int deleteBoardData(int boardId) throws Exception {
		log.info(">>BoardServiceImpl-deleteBoardData() 호출 시작, boardId:{}", boardId);
		int result = 0;
		try {
			// 1. 해당 게시글에 속한 댓글 먼저 삭제
			log.info(">>게시글(ID:{})에 속한 댓글 삭제 시도.", boardId);
			int deletedRepliesCount = 0;
			// replyDao에 deleteRepliesByBoardId 메소드
			deletedRepliesCount = replyDao.deleteRepliesByBoardId(boardId);
			log.info(">>게시글(ID:{}) 댓글 삭제 완료. 삭제된 댓글 수:{}", boardId, deletedRepliesCount);

			// 2. 데이터베이스에서 게시글 레코드 삭제
			log.info(">>게시글 레코드 삭제 시도, boardId:{}", boardId);
			// boardDao에 deleteBoard 메소드
			result = boardDao.deleteBoard(boardId);
			log.info(">>게시글 레코드 삭제 완료. 삭제된 행 수:{}", result);

		} catch (Exception e) {
			log.error(">>데이터베이스 삭제 중 예외 발생, boardId:{}", boardId, e);		
			throw e; 
		}
		log.info(">>BoardServiceImpl-deleteBoardData() 호출 종료, 삭제된 행 수:{}", result);
		return result;
	}

}
