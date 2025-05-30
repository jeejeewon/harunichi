package com.harunichi.board.service;

import java.util.List;

import com.harunichi.board.vo.BoardVo;

public interface BoardService {
	List<BoardVo> selectBoardList() throws Exception;
}
