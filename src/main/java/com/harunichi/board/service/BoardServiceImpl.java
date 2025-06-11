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
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteBoard(int boardId, String uploadPath) throws Exception {
		log.info(">>BoardServiceImpl-deleteBoard() 호출 시작, boardId:{}, uploadPath:{}", boardId, uploadPath);

		BoardVo boardToDelete = null;
		try {
			// 1. 삭제할 게시글 정보 (이미지 파일명 포함) 조회
			log.info(">>게시글 정보 조회 시도, boardId: {}", boardId);
			boardToDelete = boardDao.getBoardByIdWithoutIncrement(boardId);
			if (boardToDelete != null) {
				log.info(">>게시글 정보 조회 완료.");
			} else {
				log.warn(">>삭제할 게시글(ID:{})을 찾을 수 없습니다. 파일 삭제 및 DB 삭제 건너뜁니다.", boardId);
				return 0; // 게시글이 없으면 삭제된 행 수 0 반환
			}
		} catch (Exception e) {
			log.error(">>게시글 정보 조회 중 예외 발생, boardId: {}", boardId, e);
			throw e; // 예외를 다시 던져 트랜잭션 롤백 및 Controller에서 잡히도록 함
		}

		// 2. 실제 파일 시스템에서 이미지 파일 삭제
		log.info(">>파일 삭제 시도 시작.");
		try {
			deleteFile(boardToDelete.getBoardImg1(), uploadPath);
			deleteFile(boardToDelete.getBoardImg2(), uploadPath);
			deleteFile(boardToDelete.getBoardImg3(), uploadPath);
			deleteFile(boardToDelete.getBoardImg4(), uploadPath);
			log.info(">>파일 삭제 시도 완료.");
		} catch (Exception e) {
			log.error(">>이미지 파일 삭제 중 예외 발생, boardId: {}", boardId, e);
		}

		// 3. 해당 게시글에 속한 댓글 먼저 삭제
		log.info(">>게시글(ID:{})에 속한 댓글 삭제 시도.", boardId);
		int deletedRepliesCount = 0;
		try {
		    deletedRepliesCount = replyDao.deleteRepliesByBoardId(boardId); // <-- 여기서 예외 발생 가능성 높음
		    log.info(">>게시글(ID:{}) 댓글 삭제 완료. 삭제된 댓글 수: {}", boardId, deletedRepliesCount);
		} catch (Exception e) {
		    log.error(">>댓글 삭제 중 예외 발생, boardId: {}", boardId, e);
		    throw e; // 예외를 다시 던져 트랜잭션 롤백 및 Controller에서 잡히도록 함
		}


		// 4. 데이터베이스에서 게시글 레코드 삭제
		log.info(">>게시글 레코드 삭제 시도, boardId: {}", boardId);
		int result = 0;
		try {
		    result = boardDao.deleteBoard(boardId); // <-- 여기서 예외 발생 가능성 높음 (외래 키 등)
		    log.info(">>게시글 레코드 삭제 완료. 삭제된 행 수: {}", result);
		} catch (Exception e) {
		    log.error(">>게시글 레코드 삭제 중 예외 발생, boardId: {}", boardId, e);
		    throw e; // 예외를 다시 던져 트랜잭션 롤백 및 Controller에서 잡히도록 함
		}

		log.info(">>BoardServiceImpl-deleteBoard() 호출 종료, 삭제된 행 수:{}", result);

		return result; // 삭제된 행 수 반환
	}

	// deleteFile 헬퍼 메소드
	private void deleteFile(String fileName, String uploadPath) {
		if (fileName != null && !fileName.isEmpty() && uploadPath != null && !uploadPath.isEmpty()) {
			File fileToDelete = new File(uploadPath, fileName);

			if (fileToDelete.exists()) {
				if (fileToDelete.delete()) {
					log.info(">>파일 삭제 성공: {}", fileName);
				} else {
					log.warn(">>파일 삭제 실패: {}", fileName);
					// TODO: 파일 삭제 실패 시 예외를 던져 트랜잭션을 롤백할지 결정
					// throw new IOException("Failed to delete file: " + fileName); // 예외 발생 시 DB
					// 삭제도 롤백
				}
			} else {
				log.warn(">>삭제하려는 파일이 존재하지 않습니다: {}", fileName);
			}
		} else {
			log.warn(">>파일 삭제 시도 실패: 파일명 또는 업로드 경로가 유효하지 않습니다. fileName:{}, uploadPath:{}", fileName, uploadPath);
		}
	}
}
