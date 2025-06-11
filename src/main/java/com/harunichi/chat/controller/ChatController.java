package com.harunichi.chat.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harunichi.chat.service.ChatService;
import com.harunichi.chat.vo.ChatVo;
import com.harunichi.member.vo.MemberVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/chat")
public class ChatController {
	
	@Autowired
	ChatService chatService;
	
	
	
	@RequestMapping("/login")
	public String loginTest(HttpServletRequest request, HttpServletResponse response) throws Exception{		
		System.out.println("chatController의 loginTest 메소드 실행 -------------");		
		return "/chat/loginTest";		
	}
	
	
	
	@RequestMapping("/main")
	public String chatMain(HttpServletRequest request, 
						   HttpServletResponse response, HttpSession session, Model model) throws Exception{		
		System.out.println("chatController의 chatMain 메소드 실행 -------------");
	
		//로그인 했다고 가정하에 작성 (추후 삭제 필요)
		String id = "user2";
		String nick = "유저2";
		session.setAttribute("id", id);
		session.setAttribute("nick", nick);
		
		//DB에서 친구추천 리스트 조회
		List<MemberVo> memberList = chatService.selectMembers(id);
		
		model.addAttribute("memberList", memberList);
		
		return "/chatMain";		
	}
	
	
	@Transactional
	@RequestMapping(value = "/window", method = RequestMethod.POST)
	public String chatWindow (HttpServletRequest request, 
			   HttpServletResponse response, Model model) throws Exception{		
		System.out.println("chatController의 chatWindow 메소드 실행 -------------");
		
		//채팅방 고유 ID 확인 (신규 채팅인지?)
		String senderId = request.getParameter("id");
		String receiverId = request.getParameter("receiverId");		
		String roomId = chatService.selectRoomId(senderId, receiverId);		
		model.addAttribute("roomId", roomId);

		//채팅방 참여 인원 확인
		String userList = chatService.selectUserCount(roomId);
		int count = userList.split(",").length;
		model.addAttribute("count", count);
		
		//채팅방 타이틀 제목 확인
		String title; 
		
		//개인 채팅일 경우 상대방 닉네임 표시
		if(count <= 2) {
			title = chatService.selectNick(receiverId);
			model.addAttribute("title", title);	
		}else {
			//단체 채팅일 경우 지정한 타이틀 표시
			title = chatService.selectTitle(roomId);
			model.addAttribute("title", title);
		}
		
		return "/chatWindow";	
	}
	
	
	
	//과거 채팅 내역 불러오기
	@RequestMapping("/history")
	@ResponseBody
	public List<ChatVo> selectChatHistory(@RequestParam String roomId){
		System.out.println("chatController의 selectChatHistory 메소드 실행 -------------");
		return chatService.selectChatHistory(roomId);
	}
	
	
}
