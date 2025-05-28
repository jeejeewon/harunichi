package com.harunichi.main.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String showMainPage(Locale locale, Model model) {
		logger.info("메인페이지입니다.", locale);
		
		//여기에 로직넣기. 예) Model에 담은 데이터 조회하기 등등
		
		return "/main/main";
	}
	
}
