package com.harunichi.admin.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.harunichi.follow.service.FollowService;
import com.harunichi.member.service.MemberService;

@Controller
@RequestMapping("/admin/member")
public class AdminMemberController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminMemberController.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private FollowService followService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String showMemberList(HttpServletRequest request, Model model,
                                 @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
                                 @RequestParam(value = "page", defaultValue = "1") int page) {
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("activeTab", "member");

        Map<String, Object> result = memberService.getMemberList(searchKeyword, page);
        model.addAttribute("result", result);
        model.addAttribute("searchKeyword", searchKeyword);

        logger.debug("회원 목록 페이지 요청 - 검색어: {}, 페이지: {}", searchKeyword, page);

        return "/admin/member";
    }

    @RequestMapping(value = "/follow", method = RequestMethod.GET)
    public String showFollowList(HttpServletRequest request, Model model,
                                 @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
                                 @RequestParam(value = "page", defaultValue = "1") int page) {
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("activeTab", "follow");

        Map<String, Object> result = followService.getFollowList(searchKeyword, page);
        model.addAttribute("result", result);
        model.addAttribute("searchKeyword", searchKeyword);

        logger.debug("팔로우 목록 페이지 요청 - 검색어: {}, 페이지: {}", searchKeyword, page);

        return "/admin/member";
    }
}
