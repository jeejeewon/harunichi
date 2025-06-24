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

    // 마이페이지 메인
    @RequestMapping(value = "/mypage", method = RequestMethod.GET)
    public String showMypage(@RequestParam(required = false) String id,
                             Locale locale,
                             Model model,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             HttpSession session) throws Exception {

        MemberVo pageOwner = null;

        if (id != null) {
            pageOwner = memberService.selectMemberById(id);
            if (pageOwner == null) {
                sendAlertAndBack(response, "존재하지 않는 사용자입니다.");
                return null;
            }
        } else {
            pageOwner = (MemberVo) session.getAttribute("member");
            if (pageOwner == null) {
                sendAlertAndBack(response, "로그인이 필요합니다.");
                return null;
            }
        }

        model.addAttribute("pageOwner", pageOwner);

        MemberVo loginMember = (MemberVo) session.getAttribute("member");
        boolean isMyPage = (loginMember != null && pageOwner.getId().equals(loginMember.getId()));
        model.addAttribute("isMyPage", isMyPage);

        int followerCount = sqlSession.selectOne("mapper.follow.getFollowerCount", pageOwner.getId());
        int followingCount = sqlSession.selectOne("mapper.follow.getFollowingCount", pageOwner.getId());
        
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("followingCount", followingCount);

        return "/mypage";
    }

    // 나의 거래글 (partial view)
    @RequestMapping("/product/myList")
    public String myProductList(HttpSession session, Model model) {
        String memberId = ((MemberVo) session.getAttribute("member")).getId();
        model.addAttribute("productList", sqlSession.selectList("mapper.product.selectMyProducts", memberId));
        return "mypage/partial/myProductList";
    }

    // 좋아요한 거래글 (partial view)
    @RequestMapping("/like/myLike")
    public String myLikeProductList(HttpSession session, Model model) {
        String memberId = ((MemberVo) session.getAttribute("member")).getId();
        model.addAttribute("likeProductList", sqlSession.selectList("mapper.product.selectMyLikedProducts", memberId));
        return "mypage/partial/myLikeProductList";
    }

    // 나의 주문 내역 (partial view)
    @RequestMapping("/payment/orders")
    public String myOrders(HttpSession session, Model model) {
        String memberId = ((MemberVo) session.getAttribute("member")).getId();
        model.addAttribute("orderList", sqlSession.selectList("mapper.payment.selectMyOrders", memberId));
        return "mypage/partial/myOrders";
    }

    // 공용 alert + history.back
    private void sendAlertAndBack(HttpServletResponse response, String msg) throws Exception {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write("<script>alert('" + msg + "'); history.back();</script>");
        response.getWriter().flush();
    }

}
