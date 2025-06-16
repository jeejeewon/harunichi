package com.harunichi.chat.controller;

import java.io.Console;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harunichi.chat.service.ChatService;
import com.harunichi.chat.vo.ChatRoomVo;
import com.harunichi.chat.vo.ChatVo;
import com.harunichi.member.vo.MemberVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/chat")
public class ChatController {
	
	@Autowired
	private ChatService chatService;	
	
	
	@RequestMapping("/login")
	public String loginTest(HttpServletRequest request, HttpServletResponse response) throws Exception{		
		System.out.println("chatController의 loginTest 메소드 실행 -------------");		
		return "/chat/loginTest";		
	}
	
	
	
	@RequestMapping("/main")
	public String chatMain(HttpServletRequest request, 
						   HttpServletResponse response, HttpSession session, Model model) throws Exception{		
		System.out.println("chatController의 chatMain 메소드 실행 -------------");
	
		MemberVo member = (MemberVo) session.getAttribute("member");
		
		String id = (String)session.getAttribute("id");	
		String nick = member.getNick();
		
		//DB에서 채팅친구추천 리스트 조회
		List<MemberVo> memberList = chatService.selectMembers(id);		
		model.addAttribute("memberList", memberList);
		
		//오픈 채팅방 리스트 조회
		List<ChatRoomVo> openChatList = chatService.selectOpenChat();
		model.addAttribute("openChatList", openChatList);
		
		return "/chatMain";		
	}
	
	
	@RequestMapping(value = "/window", method = RequestMethod.POST)
	public String chatWindow (HttpServletRequest request, 
			   HttpServletResponse response, Model model, HttpSession session) throws Exception{		
		System.out.println("chatController의 chatWindow 메소드 실행 -------------");
		
		//채팅방 고유 ID 확인 후 신규채팅일 경우 DB에 저장
		String senderId = (String)session.getAttribute("id");		
		String chatType = request.getParameter("chatType");
		String receiverId = null;
		if(chatType == "personal") {
			receiverId = request.getParameter("receiverId");	
			System.out.println(receiverId.toString());
		}
		
		int persons = 0;
		
		System.out.println(senderId.toString());	
		System.out.println(chatType.toString());
		
		
		
		String chatTitle = request.getParameter("title");		
		if(chatType.equals("group")) {
			persons = Integer.parseInt(request.getParameter("persons"));
		}	
		
		String roomId = "";
		
		//개인채팅일 경우
		if(chatType.equals("personal")) {		
			roomId = chatService.selectRoomId(senderId, receiverId, chatType);		
			model.addAttribute("roomId", roomId);
		}else { //단체채팅 생성이므로 바로 채팅방 ID 생성
			ChatRoomVo vo = new ChatRoomVo();
			vo.setUserId(senderId);
			vo.setChatType(chatType);
			vo.setTitle(chatTitle);
			vo.setPersons(persons);
			
			roomId = chatService.insertRoomId(vo);
			model.addAttribute("roomId", roomId);
		}
		
		//채팅방 참여 인원 확인
		int count = chatService.selectUserCount(roomId);		
		model.addAttribute("count", count);
		
		//채팅방 타이틀 제목 확인
		String title; 
		
		//채팅방 타이틀이 없을 경우 상대방 유저 닉네임 사용(개인채팅)
		if(chatType.equals("personal")) {
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
