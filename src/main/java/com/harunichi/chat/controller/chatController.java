package com.harunichi.chat.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class chatController {

	@RequestMapping(value = "/chatMain")
	public String chatMain(HttpServletRequest request, 
						   HttpServletResponse response) throws Exception{
		
		System.out.println("chatController의 chatMain 메소드 실행 -------------");
		
		return "/chatMain";
		
	}
	
	
	@RequestMapping(value = "/chatWindow")
	public String chatWindow (HttpServletRequest request, 
			   HttpServletResponse response) throws Exception{
		
		System.out.println("chatController의 chatWindow 메소드 실행 -------------");
		
		return "/chatWindow";
		
	}
	
	
	
	
}
