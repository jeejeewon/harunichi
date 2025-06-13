package com.harunichi.board.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harunichi.board.controller.BoardControllerImpl;
import com.harunichi.board.dao.BoardDao;
import com.harunichi.board.dao.ReplyDao;
import com.harunichi.board.vo.BoardVo;
import com.harunichi.board.vo.ReplyVo;

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
	@Transactional(propagation = Propagation.REQUIRED)
	public BoardVo getBoardById(int boardId) throws Exception {
		// 1. 해당 boardId의 게시글 조회수를 1 증가
		incrementBoardCount(boardId);
		// 2. 조회수가 증가된 최신 게시글 정보를 가져옴
		BoardVo board = boardDao.getBoardById(boardId);
		// 3. 댓글 개수 조회 및 설정 로직은 Controller로 이동했으므로 여기서는 제거
		if (board != null) {
			log.info(">>게시글 정보 조회 완료.");
		} else {
			log.warn(">>조회할 게시글(ID:{})을 찾을 수 없습니다.", boardId);
		}
		log.info(">>BoardServiceImpl-getBoardById() 호출 종료.");
		return board; // 게시글 정보만 반환
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
		int result = 0;
		try {
			// 1. 해당 게시글에 속한 댓글 먼저 삭제		
			int deletedRepliesCount = 0;		
			deletedRepliesCount = replyDao.deleteRepliesByBoardId(boardId);				
			result = boardDao.deleteBoard(boardId);	
		} catch (Exception e) {
			log.error(">>데이터베이스 삭제 중 예외 발생, boardId:{}", boardId, e);
			throw e;
		}
		log.info(">>BoardServiceImpl-deleteBoardData() 호출 종료, 삭제된 행 수:{}", result);
		return result;
	}

	// 댓글 추가
	@Override
	public int addReply(ReplyVo reply) throws Exception {		
		// ReplyDao의 insertReply 메소드를 호출하여 DB에 댓글 저장
		int result = replyDao.insertReply(reply);
		log.info(">>댓글 DB 저장 완료. 결과: {}", result);
		return result; // 삽입된 행 수 반환
	}

	// 특정 게시물의 댓글 조회
	@Override
	public List<ReplyVo> getRepliesByBoardId(int boardId) throws Exception {	
		List<ReplyVo> replyList = replyDao.selectRepliesByBoardId(boardId);
		log.info(">>BoardServiceImpl-getRepliesByBoardId() 호출 종료. 조회된 댓글 수:{}",
				replyList != null ? replyList.size() : 0);
		return replyList; // 조회된 댓글 목록 반환
	}
	
	@Override	
	@Transactional(readOnly = true) // 읽기 전용 트랜잭션
	public int getReplyCountByBoardId(int boardId) throws Exception {
		log.info(">>BoardServiceImpl-getReplyCountByBoardId() 호출 시작, boardId:{}", boardId);
		// ReplyDao의 countRepliesByBoardId 메소드를 호출하여 댓글 개수를 가져옵니다.
		int replyCount = replyDao.countRepliesByBoardId(boardId);
		log.info(">>BoardServiceImpl-getReplyCountByBoardId() 호출 종료. 댓글 개수:{}", replyCount);
		return replyCount; // 댓글 개수 반환
	}
	
	@Override
    public int deleteReply(int replyId, String replyWriter) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("replyId", replyId);
        paramMap.put("replyWriter", replyWriter);
        return replyDao.deleteReply(paramMap);
    }

    @Override
    public int updateReply(int replyId, String replyCont, String replyWriter) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("replyId", replyId);
        paramMap.put("replyCont", replyCont);
        paramMap.put("replyWriter", replyWriter); 
        return replyDao.updateReply(paramMap);
    }

}
