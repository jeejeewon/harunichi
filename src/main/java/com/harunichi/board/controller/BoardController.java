package com.harunichi.board.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

public interface BoardController {

	// 전체 글 목록
	ModelAndView boardList(HttpServletRequest request, HttpServletResponse response) throws Exception;

	// 게시글 등록 및 수정
	 ModelAndView boardEdit(HttpServletRequest request, HttpServletResponse response,
             int boardId, List<MultipartFile> imageFiles);

}
