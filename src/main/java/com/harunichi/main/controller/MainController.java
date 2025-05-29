package com.harunichi.main.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	//http://localhost:8090/harunichi 요청시 메인페이지, 또는
	//http://localhost:8090/harunichi/main 요청시 메인페이지
	@RequestMapping(value = {"/", "/main"}, method = RequestMethod.GET)
	public String showMainPage(Locale locale, Model model, HttpServletRequest request) {
		
		logger.info("메인페이지입니다.", locale);
		
		// 인터셉터가 넣어둔 viewName 가져오기
		String viewName = (String) request.getAttribute("viewName");
		System.out.println("컨트롤러에서 가져온 viewName: " + viewName);
		
		logger.info("MainController - showMainPage() 메소드 종료. Returning view name: /main");
		return "/main"; 
	}
	
}
