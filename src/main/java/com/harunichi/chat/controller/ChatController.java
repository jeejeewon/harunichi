package com.harunichi.chat.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.harunichi.chat.service.ChatService;
import com.harunichi.member.vo.MemberVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/chat")
public class ChatController {
	
	@Autowired
	ChatService chatService;
	
	@RequestMapping("/login")
	public String loginTest(HttpServletRequest request, HttpServletResponse response) throws Exception{		
		System.out.println("chatController의 loginTest 메소드 실행 -------------");		
		return "/chat/loginTest";		
	}
	
	
	@RequestMapping("/main")
	public String chatMain(HttpServletRequest request, 
						   HttpServletResponse response, HttpSession session) throws Exception{
		
		System.out.println("chatController의 chatMain 메소드 실행 -------------");
	
		//로그인 했다고 가정하에 작성 (추후 삭제 필요)
		String id = "hong";
		session.setAttribute("id", id);
		
		//DB에서 친구추천 리스트 조회
		List<MemberVo> members = chatService.selectMembers(id);
			//1. 사용자의 myLike(관심사)를 토대로 
				//1-1. 관심사가 없거나 관심사에 맞는 사람이 없을 경우 그냥 조회
			//2. 자신 빼고 조회
		
		
		
		
		return "/chatMain";
		
	}
	
	
	@RequestMapping("/window")
	public String chatWindow (HttpServletRequest request, 
			   HttpServletResponse response) throws Exception{
		
		System.out.println("chatController의 chatWindow 메소드 실행 -------------");
		
		return "/chatWindow";
		
	}
	
	
	
	
}
