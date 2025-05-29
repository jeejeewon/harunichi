package com.harunichi.chat.server;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harunichi.chat.service.ChatService;
import com.harunichi.chat.vo.ChatVo;

//웹소켓 채팅 서버 본체
//- 클라이언트들의 연결을 받고, 메시지를 중계하는 웹소켓 서버의 역할 수행
//- 클라이언트는 'ws://서버주소:포트/프로젝트명/ChatingServer' 와 같은 주소로 접속을 시도합니다. (ws://는 웹소켓 통신 규약)
@ServerEndpoint("/ChatingServer") // "/ChatingServer"라는 주소로 오는 웹소켓 요청은 이 클래스가 처리하겠다고 웹 서버에게 알려주는 설정
public class ChatServer {

	//서비스 빈 수동 주입
	private ChatService chatService;
	public ChatServer() {
		this.chatService = SpringContext.getBean(ChatService.class);
	}
	

	//현재 접속 중인 모든 클라이언트 목록 관리
	//Collections.synchronizedSet(...) 여러 클라이언트가 동시 사용시 문제가 생기지않도록 동기화 역할을 함
	private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
							
	
	//새로운 클라이언트가 접속했을때 실행되는 메소드
	//클라이언트가 웹소켓 연결에 성공하면, 서버는 자동으로 이 메소드를 실행
	//@param session 새로 연결된 클라이언트와의 '1:1 통신 채널' 정보 객체. 이 객체를 통해 해당 클라이언트와 통신할 수 있음
	@OnOpen
	public void onOpen(Session session) {		
		//새로운 클라이언트 접속시 접속자 명단에 추가
		clients.add(session); 
		System.out.println("✅ [서버로그] 새로운 사용자 입장! ID:" + session.getId() + " (현재 총 " + clients.size() + "명 접속중)");		
	}
	
	
	//클라이언트로부터 메세지를 받았을 때 실행되는 메소드
    //연결된 클라이언트 중 누군가가 서버로 메시지를 보내면, 서버는 자동으로 이 메소드를 불러 실행함
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		
		//어떤 클라이언트가 어떤 메시지를 보냈는지 서버 콘솔에 기록
        System.out.println("✉️ [서버 로그] 메시지 도착! 보낸 사람 ID:" +  session.getId() + ", 내용: \"" + message + "\"");        
        
        //JSON 파싱
        ObjectMapper mapper = new ObjectMapper();
        ChatVo chatMsg = mapper.readValue(message, ChatVo.class);
        
        //DB에 저장
        chatService.saveMessage(chatMsg);
        
        
        //접속자 명단(clients)을 수정하거나 사용하는 동안 다른 작업이 끼어들지 못하게 잠금(Lock)을 검
        //여러 클라이언트가 동시에 메시지를 보내거나 접속/종료할 때 접속자 명단이 꼬이는 것을 방지하는 안전 장치
        //이 블록 안의 작업이 끝날 때까지 다른 작업은 잠시 대기
        synchronized (clients) {
		
        	//현재 접속 중인 모든 클라이언트에게 받은 메시지를 전달하기 위해 '반복 작업'을 시작
        	System.out.println("✉️ [서버 로그] 메시지 전달 시작 (총 " + clients.size() + "명에게 확인 중)");
        	
            for (Session client : clients) {
            	
                //지금 확인 중인 클라이언트(client)가 메시지를 보낸 사람(session)과 '같은 사람인지' 확인 
            	//자신이 보낸 메시지를 다시 받지 않기 위함 (메아리방지)    
                if (!client.equals(session)) {
                	
                    //메시지를 보낸 사람 외의 다른 클라이언트에게 메시지 전송
                    //client.getBasicRemote(): 해당 클라이언트(client)에게 메시지를 보낼 수 있는 '기본 원격 제어기'를 얻음
                    //sendText(message): 얻은 원격 제어기를 사용하여 실제 텍스트 메시지(message)를 클라이언트의 웹 브라우저로 전송
                    System.out.println("     ㄴ[서버 로그] ID " + client.getId() + " 에게 메시지 전달 시도...");
                    client.getBasicRemote().sendText(message); //message 변경해야함. 윈도우 JSP에서 모든 정보를 넘기기때문에!!
                    System.out.println("     ㄴ[서버 로그] ID " + client.getId() + " 에게 메시지 전달 성공!");                    
                }                 
            }//for           
            System.out.println("✉️ [서버 로그] 메시지 전달 완료!");	
		}//synchronized
	}//onMessage
	
    
    //클라이언트와의 연결이 끊어졌을 때 실행되는 작업 (@OnClose)
    //클라이언트가 웹 브라우저를 닫거나, 인터넷 연결이 끊기거나, 어떤 이유로든 연결이 종료되면 서버는 자동으로 이 메소드를 불러 실행함
    //@param session 연결이 끊어진 클라이언트의 '1:1 통신 채널' 정보 객체.    
    @OnClose
    public void onClose(Session session) {
        //접속자 명단에서 떠나간 클라이언트의 정보(session)를 제거합니다.
        clients.remove(session);
        System.out.println("❌ [서버 로그] 사용자 퇴장! ID: " + session.getId() + " (현재 총 " + clients.size() + "명 접속 중)");
    }	
	       
   
    //웹소켓 통신 중 오류가 발생했을 때 실행되는 작업
    //메시지 전송 실패, 네트워크 불안정 등 예상치 못한 문제가 통신 중에 발생하면 서버는 자동으로 이 메소드를 불러 실행
    //@param e 발생한 오류에 대한 상세 정보가 담긴 객체. 오류의 종류, 원인, 발생 위치 등을 알 수 있음
    @OnError
    public void onError(Throwable e) {
        System.out.println("⚠️ [서버 로그] !!! 웹소켓 통신 오류 발생 !!!");
        //발생한 오류의 상세 내용을 콘솔에 출력
        e.printStackTrace();
    }
	
}
