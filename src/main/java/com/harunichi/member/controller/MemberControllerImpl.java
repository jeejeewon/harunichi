package com.harunichi.member.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.harunichi.member.vo.MemberVo;

@Controller("memberController")
@RequestMapping(value="/member")
public class MemberControllerImpl implements MemberController{

	@Override//로그인메소드
	public ModelAndView login(Map<String, String> loginMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}

	@Override//로그아웃메소드
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}
	
	@Override//회원가입 폼 보여주는 메소드
	@RequestMapping(value = "/addMemberForm.do", method = RequestMethod.GET)
	public ModelAndView addMemberForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// String viewName=(String)request.getAttribute("viewName"); // 인터셉터에서 가져오는 코드 주석 처리
		String viewName = "member/addMemberForm"; // 논리적인 뷰 이름 직접 지정!
		ModelAndView mav = new ModelAndView(viewName);
        System.out.println("컨트롤러에서 리턴하는 viewName: " + viewName); // 콘솔 출력도 바꿔서 확인!
		return mav;
	}
	// 국적에 따른 회원가입 폼 내용을 반환하는 메소드
    @RequestMapping(value = "/getRegistrationForm", method = RequestMethod.GET)
    public String getRegistrationForm(@RequestParam("nationality") String nationality, Model model) {
        if ("KR".equals(nationality)) {
            return "member/addMemberFormSelectKr"; // KR선택시 addMemberFormKr
        } else if ("JP".equals(nationality)) {
            return "member/addMemberFormSelectJp"; // JP선택시 addMemberFormJp
        } else {
            // 잘못된 국적 값이 넘어왔을 경우
            return "error/invalidNationality";
        }
    }

	@Override//회원가입메소드
	public ResponseEntity addMember(MemberVo member, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}

	@Override//아이디 중복확인 메소드
	public ResponseEntity overlapped(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}
	
}
