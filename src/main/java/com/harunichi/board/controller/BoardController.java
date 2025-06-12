package com.harunichi.board.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.harunichi.board.vo.ReplyVo;

public interface BoardController {

	// 전체 글 목록
	ModelAndView boardList(HttpServletRequest request, HttpServletResponse response) throws Exception;

	// 게시글 등록
	ModelAndView boardPost(HttpServletRequest request, HttpServletResponse response, List<MultipartFile> imageFiles)
			throws Exception;

	// 게시글 수정 폼
	ModelAndView editBoardForm(HttpServletRequest request, HttpServletResponse response, int boardId) throws Exception;

	// 게시글 수정 폼에서 넘어온 데이터를 처리하는 메소드
	// ModelAndView updateBoard(HttpServletRequest request, HttpServletResponse
	// response, List<MultipartFile> imageFiles) throws Exception;
	ModelAndView updateBoard(HttpServletRequest request, HttpServletResponse response, List<MultipartFile> imageFiles,
			List<Integer> deleteIndices);

	// 게시글 삭제
	ModelAndView deleteBoard(HttpServletRequest request, HttpServletResponse response, int boardId) throws Exception;

	// 게시글 상세 (댓글 목록 포함)
	ModelAndView viewBoard(HttpServletRequest request, HttpServletResponse response, int boardId) throws Exception;

	public String addReply(ReplyVo reply, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
