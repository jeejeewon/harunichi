package com.harunichi.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
