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
import org.springframework.web.multipart.MultipartFile;
import com.harunichi.chat.service.ChatServiceImpl;
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
	private ChatServiceImpl chatServiceImpl;
	
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
				List<ChatRoomVo> myChatList = chatServiceImpl.selectMyChatList(id);
				model.addAttribute("myChatList", myChatList);
									
				//상대방 프로필 담을 변수
				List<MemberVo> profileList = new ArrayList<MemberVo>();	
				MemberVo memberProfile = new MemberVo();
				
				//상대방 프로필 사진이 없을 경우 기본 이미지 사용
				String basicProfileImg = "/harunichi/resources/icon/basic_profile.jpg";
				
				//참여중인 채팅방 프로필 사진 조회
				for(ChatRoomVo vo : myChatList) {
					
					//개인채팅방은 상대방 프로필 사진이 보이도록 설정
					if(vo.getChatType().equals("personal")) {			
						//개인 채팅 상대방 ID 조회
						String chatMemberId = chatServiceImpl.selectChatMemberId(id, vo.getRoomId());					
						//상대방 프로필 조회
						memberProfile = chatServiceImpl.selectProfile(chatMemberId);					
						
						String profileImg = null;
						profileImg = memberProfile.getProfileImg();				
							
						//상대방 프로필 없을 경우 기본 이미지 보여주기
						if(profileImg == null || profileImg == "") {							
							memberProfile.setProfileImg(basicProfileImg);
							profileList.add(memberProfile);	
						}
											
					//오픈채팅방은 방장이 설정한 프로필 사진으로 설정		
					}else {
						memberProfile = null;
					}
					
					profileList.add(memberProfile);	
				}
				
				model.addAttribute("profileList", profileList);	
				
				//참여중인 채팅의 메세지 정보 조회
				List<ChatVo> myChatMessage = new ArrayList<ChatVo>();
				for(ChatRoomVo chatRoomVo : myChatList) {
					String roomId = chatRoomVo.getRoomId();										
					myChatMessage.add(chatServiceImpl.selectMyChatMessage(roomId));							
				}
				model.addAttribute("myChatMessage", myChatMessage);								
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("⚠" + id + "의 채팅 목록을 조회할 수 없습니다.");
			}	
		}
		//DB에서 채팅친구추천 리스트 조회
		List<MemberVo> memberList = chatServiceImpl.selectMembers(id);		
		model.addAttribute("memberList", memberList);
		
		//오픈채팅방 최신 메세지 담을 변수
		String message = "";
		List<ChatVo> messageList = new ArrayList<ChatVo>();
				
		try {
			//오픈 채팅방 리스트 조회
			List<ChatRoomVo> openChatList = chatServiceImpl.selectOpenChat();
			
			//최신 채팅 메세지 정보 조회
			for(ChatRoomVo vo : openChatList) {				
				String roomId = vo.getRoomId();				
				ChatVo myChatMessage = chatServiceImpl.selectMyChatMessage(roomId);
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

	
	@RequestMapping(value = "createOpenChat", method = RequestMethod.POST)
	public String createOpenChat(HttpServletRequest request, HttpServletResponse response, 
			   				   Model model, HttpSession session,
			   				   @RequestParam("imgUpload") MultipartFile file) throws Exception{	
		log.info("chatController의 createOpenChat 메소드 실행 -------------");
			
		//로그인 유무 확인
		if (!LoginCheck.loginCheck(session, request, response)) { return null; }
		
		//프로필 이미지 파일이 없으면 리턴
		if(file.isEmpty()) { return null; }
		
		//이미지 파일을 C드라이브에 저장
		String fileName = chatServiceImpl.chatProfileImgUpload(file);
					
		MemberVo member = (MemberVo) session.getAttribute("member");
		String senderId = member.getId();
		int	persons = Integer.parseInt(request.getParameter("persons"));
		
		ChatRoomVo vo = new ChatRoomVo();
		vo.setUserId(senderId);
		vo.setPersons(persons);
		vo.setProfileImg(fileName);
		vo.setTitle(request.getParameter("title"));
		vo.setChatType(request.getParameter("chatType"));
		
		//채팅방 ID 생성 후 DB에 채팅방 정보 저장
		String roomId = chatServiceImpl.insertRoomId(vo);
		model.addAttribute("roomId", roomId);

		//채팅방 참여 인원 조회
		int count = chatServiceImpl.selectUserCount(roomId);		
		model.addAttribute("count", count);
			
		//채팅방 타이틀 조회
		String title = chatServiceImpl.selectTitle(roomId);
		model.addAttribute("title", title);
				
		return "/chatWindow";		
	}
	
	
	
	@Override
	@RequestMapping(value = "/window", method = RequestMethod.POST)
	public String chatWindow (HttpServletRequest request, 
			   HttpServletResponse response, Model model, HttpSession session) throws Exception{		
		System.out.println("POST chatController의 chatWindow 메소드 실행 -------------");
		
		if (!LoginCheck.loginCheck(session, request, response)) { return null; }
		
		MemberVo member = (MemberVo) session.getAttribute("member");
		
		//채팅방 고유 ID 확인 후 신규채팅일 경우 DB에 저장
		String senderId = member.getId();
		String chatType = request.getParameter("chatType");
		String receiverId = request.getParameter("receiverId");
	
		String roomId = chatServiceImpl.selectRoomId(senderId, receiverId, chatType);	
		model.addAttribute("roomId", roomId);		
				
		//채팅방 참여 인원 조회
		int count = chatServiceImpl.selectUserCount(roomId);		
		model.addAttribute("count", count);
		
		//채팅방 타이틀이 없을 경우 상대방 유저 닉네임 사용(개인채팅)
		MemberVo vo = chatServiceImpl.selectProfile(receiverId);
		model.addAttribute("title", vo.getNick());	
		model.addAttribute("profileImg", vo.getProfileImg());	

		return "/chatWindow";	
	}
		
	
	//과거 채팅 내역 불러오기
	@Override
	@RequestMapping("/history")
	@ResponseBody
	public List<ChatVo> selectChatHistory(@RequestParam String roomId){
		System.out.println("chatController의 selectChatHistory 메소드 실행 -------------");
		return chatServiceImpl.selectChatHistory(roomId);
	}
	
	
}
