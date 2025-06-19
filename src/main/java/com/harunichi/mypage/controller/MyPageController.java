package com.harunichi.mypage.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.harunichi.main.controller.MainController;
import com.harunichi.member.service.MemberService;
import com.harunichi.member.vo.MemberVo;

@Controller
public class MyPageController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MemberService memberService;
    
    @Autowired
    private SqlSession sqlSession;

    @RequestMapping(value = "/mypage", method = RequestMethod.GET)
    public String showMypage(@RequestParam(required = false) String id,  // 다른 사람 프로필 ID (없으면 내 마이페이지)
                             Locale locale,
                             Model model,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             HttpSession session) throws Exception {

        MemberVo pageOwner = null;  // 최종적으로 마이페이지 주인 정보

        if (id != null) {
            // 다른 사람 프로필을 보러 온 경우 (URL에 id 파라미터가 있는 경우)
            pageOwner = memberService.selectMemberById(id);
            if (pageOwner == null) {
                // 존재하지 않는 사용자 ID면 alert 후 뒤로가기
                sendAlertAndBack(response, "존재하지 않는 사용자입니다.");
                return null;
            }
        } else {
            // 내 마이페이지를 보러 온 경우 (id 파라미터가 없음)
            pageOwner = (MemberVo) session.getAttribute("member");
            if (pageOwner == null) {
                // 로그인 안 한 상태에서 내 마이페이지 접근 시 alert 후 뒤로가기
                sendAlertAndBack(response, "로그인이 필요합니다.");
                return null;
            }
        }

        // 마이페이지 주인 정보 JSP로 전달
        model.addAttribute("pageOwner", pageOwner);

        // 내 마이페이지인지 타인의 마이페이지인지 여부 flag 생성
        MemberVo loginMember = (MemberVo) session.getAttribute("member");
        boolean isMyPage = (loginMember != null && pageOwner.getId().equals(loginMember.getId()));
        model.addAttribute("isMyPage", isMyPage);
        
        
     // 팔로워, 팔로잉 수 조회
        int followerCount = sqlSession.selectOne("mapper.follow.getFollowerCount", pageOwner.getId());
        int followingCount = 0;
        if (loginMember != null) {
            followingCount = sqlSession.selectOne("mapper.follow.getFollowingCount", loginMember.getId());
        }
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("followingCount", followingCount);

        // 타일즈 definition 이름 반환 → /WEB-INF/tiles-defs.xml에서 name="/mypage"인 definition에 매핑
        return "/mypage";
    }

    // alert 창 띄우고 뒤로가기 스크립트 출력
    private void sendAlertAndBack(HttpServletResponse response, String msg) throws Exception {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(
            "<script>alert('" + msg + "'); history.back();</script>"
        );
        response.getWriter().flush();
    }
    
}
