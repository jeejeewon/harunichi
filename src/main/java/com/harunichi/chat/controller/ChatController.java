package com.harunichi.chat.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.harunichi.chat.vo.ChatVo;

public interface ChatController {

	String chatMain(HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model)
			throws Exception;

	String loginChek(HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session)
			throws Exception;

	List<ChatVo> selectChatHistory(String roomId, HttpSession session);

	String createOpenChat(HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session,
			MultipartFile file) throws Exception;

	String doChat(String roomId, HttpServletRequest request, HttpServletResponse response, Model model,
			HttpSession session) throws Exception;

	String createChat(HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session)
			throws Exception;

	String doOpenChat(String roomId, HttpServletRequest request, HttpServletResponse response, Model model,
			HttpSession session) throws Exception;

	
	
}
