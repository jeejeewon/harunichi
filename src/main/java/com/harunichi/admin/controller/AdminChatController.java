package com.harunichi.admin.controller;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.harunichi.admin.controller.AdminMemberController.MemberListWrapper;
import com.harunichi.chat.service.ChatService;
import com.harunichi.member.vo.MemberVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/chat")
public class AdminChatController {
	
	@Autowired
	ChatService chatService;

	//채팅방 검색
	@RequestMapping(value = "")
	public String searchChatRoomList(HttpServletRequest request, Model model,
									 @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
									 @RequestParam(value = "searchType", defaultValue = "all") String searchType,
									 @RequestParam(value = "page", defaultValue = "1") int page) {
		
		model.addAttribute("currentUri", request.getRequestURI());
		model.addAttribute("activeTab", "chat");
		
	    Map<String, Object> result = chatService.searchChatRoomList(searchKeyword, searchType, page);
	    model.addAttribute("result", result);
	    model.addAttribute("searchKeyword", searchKeyword);
	    model.addAttribute("searchType", searchType); 
	
		return "/admin/chat";
	}
	

	
	
	
	
	
}
    
	
  
