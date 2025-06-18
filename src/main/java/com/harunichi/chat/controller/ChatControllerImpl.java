package com.harunichi.chat.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.harunichi.chat.service.ChatService;
import com.harunichi.chat.vo.ChatRoomVo;
import com.harunichi.chat.vo.ChatVo;
import com.harunichi.common.util.LoginCheck;
import com.harunichi.member.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/chat")
public class ChatControllerImpl implements ChatController {
	
	@Autowired
	private ChatService chatService;
	
	@Override
	@RequestMapping("/main")
	public String chatMain(HttpServletRequest request, 
						   HttpServletResponse response, HttpSession session, Model model) throws Exception{		
		System.out.println("chatController의 chatMain 메소드 실행 -------------");
	
		MemberVo member = (MemberVo) session.getAttribute("member");
		
		String id = null;
		String nick = null;
				
		//로그인 했다면?
		if(member != null && member.getId() != null && !member.getId().isEmpty()) {
			log.info("나의 채팅방 정보 조회중....");	
			id = member.getId();
			nick = member.getNick();			
			try {							
				//참여중인 채팅방 정보 조회
				List<ChatRoomVo> myChatList = chatService.selectMyChatList(id);
				model.addAttribute("myChatList", myChatList);
				
				//참여중인 채팅방 프로필 사진 조회
				for(ChatRoomVo vo : myChatList) {
					
					//개인채팅방은 상대방 프로필 사진이 보이도록 설정
					if(vo.getChatType().equals("personal")) {
						
					}
					//오픈채팅방은 방장이 설정한 프로필 사진으로 설정
				}
				
				//참여중인 채팅의 메세지 정보 조회
				List<ChatVo> myChatMessage = new ArrayList<ChatVo>();
				for(ChatRoomVo chatRoomVo : myChatList) {
					String roomId = chatRoomVo.getRoomId();										
					myChatMessage.add(chatService.selectMyChatMessage(roomId));							
				}
				model.addAttribute("myChatMessage", myChatMessage);								
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("⚠" + id + "의 채팅 목록을 조회할 수 없습니다.");
			}	
		}
		//DB에서 채팅친구추천 리스트 조회
		List<MemberVo> memberList = chatService.selectMembers(id);		
		model.addAttribute("memberList", memberList);
		
		//오픈채팅방 최신 메세지 담을 변수
		String message = "";
		List<ChatVo> messageList = new ArrayList<ChatVo>();
				
		try {
			//오픈 채팅방 리스트 조회
			List<ChatRoomVo> openChatList = chatService.selectOpenChat();
			
			//최신 채팅 메세지 정보 조회
			for(ChatRoomVo vo : openChatList) {				
				String roomId = vo.getRoomId();				
				ChatVo myChatMessage = chatService.selectMyChatMessage(roomId);
				messageList.add(myChatMessage);
			}
			model.addAttribute("messageList", messageList);
			model.addAttribute("openChatList", openChatList);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("⚠오픈 채팅 목록을 조회할 수 없습니다.");
		}
		return "/chatMain";
	}

	@Override
	@RequestMapping(value = "/window", method = RequestMethod.GET)
	public String loginChek (HttpServletRequest request, 
			   HttpServletResponse response, Model model, HttpSession session) throws Exception{		
		System.out.println("GET chatController의 chatWindow 메소드 실행 -------------");
		
		if (!LoginCheck.loginCheck(session, request, response)) {
		    return null; 
		}
		
		String roomId = request.getParameter("roomId");
		
		return "/chatWindow";
	}

	
	@Override
	@RequestMapping(value = "/window", method = RequestMethod.POST)
	public String chatWindow (HttpServletRequest request, 
			   HttpServletResponse response, Model model, HttpSession session) throws Exception{		
		System.out.println("POST chatController의 chatWindow 메소드 실행 -------------");
		
		if (!LoginCheck.loginCheck(session, request, response)) {
		    return null; 
		}
		
		MemberVo member = (MemberVo) session.getAttribute("member");
		
		//채팅방 고유 ID 확인 후 신규채팅일 경우 DB에 저장
		String senderId = member.getId();
		String chatType = request.getParameter("chatType");
		String receiverId = null;
		if(chatType.equals("personal")) {
			receiverId = request.getParameter("receiverId");	
		}		
		int persons = 0;
		String chatTitle = request.getParameter("title");		
		if(chatType.equals("group")) {
			persons = Integer.parseInt(request.getParameter("persons"));
		}		
		String roomId = "";
		
		//개인채팅일 경우
		if(chatType.equals("personal")) {	
			roomId = chatService.selectRoomId(senderId, receiverId, chatType);	
			model.addAttribute("roomId", roomId);
		//단체채팅 생성이므로 바로 채팅방 ID 생성	
		}else { 
			ChatRoomVo vo = new ChatRoomVo();
			vo.setUserId(senderId);
			vo.setChatType(chatType);
			vo.setTitle(chatTitle);
			vo.setPersons(persons);			
			roomId = chatService.insertRoomId(vo);
			model.addAttribute("roomId", roomId);
		}
		
		//채팅방 참여 인원 조회
		int count = chatService.selectUserCount(roomId);		
		model.addAttribute("count", count);
		
		//채팅방 타이틀 조회
		String title; 
		
		//채팅방 타이틀이 없을 경우 상대방 유저 닉네임 사용(개인채팅)
		if(chatType.equals("personal")) {
			MemberVo vo = chatService.selectNick(receiverId);
			model.addAttribute("title", vo.getNick());	
			model.addAttribute("profileImg", vo.getProfileImg());	
		}else {
			//단체 채팅일 경우 지정한 타이틀 표시
			title = chatService.selectTitle(roomId);
			model.addAttribute("title", title);
		}
		
		return "/chatWindow";	
	}
		
	
	//과거 채팅 내역 불러오기
	@Override
	@RequestMapping("/history")
	@ResponseBody
	public List<ChatVo> selectChatHistory(@RequestParam String roomId){
		System.out.println("chatController의 selectChatHistory 메소드 실행 -------------");
		return chatService.selectChatHistory(roomId);
	}
	
	
}
