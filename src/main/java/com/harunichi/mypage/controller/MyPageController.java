package com.harunichi.mypage.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.harunichi.main.controller.MainController;
import com.harunichi.member.service.MemberService;

@Controller
public class MyPageController {
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	@RequestMapping(value = {"/mypage"}, method = RequestMethod.GET)
	public String showMypage(Locale locale, Model model, HttpServletRequest request) {
		
		String viewName = (String) request.getAttribute("viewName");
		System.out.println("컨트롤러에서 가져온 viewName: " + viewName);
		
		return "/mypage"; 
	}
	
}
