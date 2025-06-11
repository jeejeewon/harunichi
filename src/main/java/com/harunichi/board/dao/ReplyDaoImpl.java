package com.harunichi.board.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Repository("replyDao") 
public class ReplyDaoImpl implements ReplyDao {
	
	private static final String NAMESPACE = "mapper.reply.";

	@Autowired
	private SqlSession sqlSession;

	
	@Override
    public int deleteRepliesByBoardId(int boardId) throws Exception {
        log.info(">>ReplyDaoImpl-deleteRepliesByBoardId() 호출 시작, boardId: {}", boardId);
        // ReplyMapper.xml에 정의된 deleteRepliesByBoardId 쿼리 실행
        int deletedCount = sqlSession.delete(NAMESPACE + "deleteRepliesByBoardId", boardId);
        log.info(">>ReplyDaoImpl-deleteRepliesByBoardId() 호출 완료, 삭제된 댓글 수: {}", deletedCount);
        return deletedCount; // 삭제된 행 수 반환
    }

}
