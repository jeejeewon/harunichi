package com.harunichi.chat.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
public class ChatController {

	@RequestMapping("/login")
	public String loginTest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		System.out.println("chatController의 loginTest 메소드 실행 -------------");
		
		return "/chat/loginTest";
		
	}
	
	@RequestMapping("/main")
	public String chatMain(HttpServletRequest request, 
						   HttpServletResponse response) throws Exception{
		
		System.out.println("chatController의 chatMain 메소드 실행 -------------");
		
		//------ 인터셉터에서 로그인 인증 후 왔다고 가정하에 코드 작성
		
		//session에 사용자 정보가 저장 되어 있을거임!
		//그걸 들고 chatMain으로 이동!		
		
		return "/chatMain";
		
	}
	
	
	@RequestMapping("/window")
	public String chatWindow (HttpServletRequest request, 
			   HttpServletResponse response) throws Exception{
		
		System.out.println("chatController의 chatWindow 메소드 실행 -------------");
		
		return "/chatWindow";
		
	}
	
	
	
	
}
