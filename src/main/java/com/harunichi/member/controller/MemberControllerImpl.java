package com.harunichi.member.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.harunichi.member.vo.MemberVo;
import com.harunichi.test.controller.TestController;

@Controller("memberController")
@RequestMapping(value="/member")
public class MemberControllerImpl implements MemberController{
	
	
	private static final Logger logger = LoggerFactory.getLogger(MemberControllerImpl.class);
	
	
	@Override //요청 페이지 보여주는 메소드
	@RequestMapping(value = {"/loginpage.do", "/addMemberForm.do"}, method = RequestMethod.GET)
	public ModelAndView showForms(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	    String viewName = (String) request.getAttribute("viewName");
	    ModelAndView mav = new ModelAndView(viewName);
	    
	    logger.debug("Returning viewName: " + viewName); // Logger로 디버깅 메시지 출력
	    
	    return mav;
	    //"/loginpage.do"요청시->/loginpage.jsp보여줌
	    //"/addMemberForm.do"요청시->/addMemberForm.jsp보여줌
	}

	
	@Override//로그인메소드
	public ModelAndView login(Map<String, String> loginMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Attempting login with parameters: " + loginMap); // 로그인 시도 로그
		return null;
	}

	
	@Override//로그아웃메소드
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("User logged out."); // 로그아웃 로그
		return null;
	}
	
	
	// 국적에 따른 회원가입 폼 내용을 반환하는 메소드
    @RequestMapping(value = "/getRegistrationForm", method = RequestMethod.GET)
    public String getRegistrationForm(@RequestParam("nationality") String nationality, Model model) {
        if ("KR".equals(nationality)) {
        	logger.info("Returning Korean registration form."); // KR 선택
            return "member/addMemberFormSelectKr"; // KR선택시 addMemberFormKr
        } else if ("JP".equals(nationality)) {
        	logger.info("Returning Japanese registration form."); // JP 선택
            return "member/addMemberFormSelectJp"; // JP선택시 addMemberFormJp
        } else {
        	logger.warn("Invalid nationality: " + nationality); // 잘못된 국적
            // 잘못된 국적 값이 넘어왔을 경우
            return "error/invalidNationality";
        }
    }

    
	@Override//회원가입메소드
	public ResponseEntity addMember(MemberVo member, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Attempting to add member: " + member); // 회원가입 시도 로그
		return null;
	}

	
	@Override//아이디 중복확인 메소드
	public ResponseEntity overlapped(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("Checking if ID is overlapped: " + id); // 아이디 중복 확인 로그
		return null;
	}
	
	
}
