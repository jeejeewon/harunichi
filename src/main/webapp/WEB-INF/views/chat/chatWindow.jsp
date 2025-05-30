<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅창</title>
<link rel="stylesheet" href="../resources/css/chat.css">
<script src="../resources/js/chat.js"></script>
</head>
<body>

	<!-- 현재 채팅하는 사람의 대화명을 input에 표시 , 읽기전용으로 설정하여 수정 불가  -->
	대화명 : <input type="text" id="chatId" value="${param.nick}" readonly>
	
	<!-- 채팅 종료 버튼 클릭시 disconnect()함수 호출 -->
	<button id="closeBtn" onclick="disconnect();">채팅 종료</button>
	
	<!-- 대화창, 수신된 메세지와 전송한 메세지가 표시 되는 영역 -->
	<div id="chatWindow"></div>
	
	<div>
		<!-- 메세지 입력창,  키보드 이벤트 발생시 enterKey() 함수 호출 -->
		<input type="text" id="chatMessage" onkeyup="enterKey();">
		
		<!-- 메세지 전송 버튼 , 클릭시 sendMessage() 함수 호출 -->
		<button id="sendBtn" onclick="sendMessage();">전송</button>		
	</div>
	
</body>

<script type="text/javascript">
	/*
		상태 유지
	   - 웹소켓은 브라우저와 서버 간의 연결 상태를 readyState 속성으로 유지하여, 현재 연결 상태를 확인하고 조작할 수 있습니다.
	   - 연결 상태는 CONNECTING, OPEN, CLOSING, CLOSED의 네 가지 상태로 표현됩니다.
	*/	
	
	//웹 소켓 객체 생성 : JSP의 application내장객체를 통해 요청할 채팅 서버페이지 주소로 웹소켓을 만들어 연결 
	var webSocket = new WebSocket("<%=application.getInitParameter("CHAT_ADDR")%>/ChatingServer");
	
	//웹 소켓 채팅에 사용할 HTML(DOM)요소들( 대화창, 메세지 입력창, 대화명)을 저장할 변수들								
	var chatWindow, chatMessage, chatId;		

	
    window.onload = function(){						
   		//대화 내용이 표시될 대화창 영역 얻기 
		chatWindow = document.getElementById("chatWindow");		
   		
   		//메세지 입력창 요소 얻기 
   		chatMessage = document.getElementById("chatMessage");
   		
   		//채팅 하는 사용자의 대화명 요소에서 대화명값 얻기 
   		chatId = document.getElementById("chatId").value;
	}
    
    
    
    //메세지 전송 함수 : 채팅 사용자가 메세지 전송 버튼을 클릭하거나 엔터 키를 눌렀을때 호출
    function sendMessage(){
		
    	//새로운 채팅!
    	const chatData = {   	
    			roomId : "hongRoom",				//채팅방 Id 임시!!!
    			chatType : "${param.chatType}",		//개인채팅인지, 단체채팅인지
    			senderId : chatId,			//보낸 사람
    			senderNic : "${param.nick}",
    			message : chatMessage.value		//메세지	
    	};
    	
    	//사용자가 입력한 메세지를 대화창에서 얻어 오른쪽 정렬로 디자인 추가
    	chatWindow.innerHTML += "<div class='myMsg'>" + chatMessage.value + "</div>";
    	
    	//웹 소켓 통로를 통해 메세지를 서버페이지로 전송
    	webSocket.send(JSON.stringify(chatData));
    	
    	//메세지 입력 창 내용 초기화
    	chatMessage.value = "";
    	
    	//대화창 스크롤 막대바를 맨 아래로 이동하여 새로운 메세지가 나타나면 보이게 설정
    	chatWindow.scrollTop = chatWindow.scrollHeight;   	
    }
    
    
    
	//서버와 웹 소켓 통로 연결을 종료하는 함수 : 사용자가 '채팅종료' 버튼을 클릭했을때 호출
	function disconnect(){
		webSocket.close(); //웹 소켓 통로연결 끊기 ( 웹브라우저, 서버페이지 연결 끊김 )
	}
    
    
    
    //메세지 입력창에서 Enter키를 누르고 땠을 경우
    //자동으로 sendMessage함수를 호출하도록처리
	function enterKey(){
		//Enter키의 키코드값 = 13
		if(window.event.keyCode == 13){
			sendMessage();
		}		
	}
    
    
    
//WebSocket 웹소켓 통로에 여러 이벤트가 발생했을때 자동으로 처리하는 이벤트핸들러 설정
    
	//서버에 웹소켓 통로 연결이 성공적으로 이루어진 이벤트가 발생했을때 호출되는 이벤트 핸들러 함수 설정
	//대화창에 연결 성공 메세지를 보여주기 위해 출력
	webSocket.onopen = function(event){ chatWindow.innerHTML += "웹소켓통로를 통한 서버페이지에 연결되었습니다.<br/>"; };  
	
	
	//웹소켓 통로와 연결된 서버페이지와의 연결이 종료될때의 이벤트가 발생하면 호출되는 이벤트 핸들러 함수 설정
	//대화창에 연결 종료 메세지를 출력
	webSocket.onclose = function(event){ chatWindow.innerHTML += "웹 소켓통로를 통한 서버페이지와의 연결 종료되었습니다.<br/>"; };
	
	
	//웹소켓 통로와 통신중 오류가 발생하는 이벤트가 일어나면 호출되는 이벤트 핸들러 함수 설정
	//오류 발생시 알림창에 오류메시지 표시하고 대화창에 에러 메세지 출력
	webSocket.onerror = function(event){
		alert(event.data);  
		chatWindow.innerHTML += "채팅 중에 에러가 발생하였습니다.<br/>";
	};
	
	
	//서버페이지에서 웹소켓 통로를 통해 클라이언트가 보낸 메세지를 보내어서 여기로 수신했을때(메아리)
	webSocket.onmessage = function(event){		
		
		console.log(event.data);
		
		//수신된 데이터 '대화명 | 메세지'를 '|'기준으로 분리하여 배열로 저장
		var message = event.data.split("|");
		
		//대화명을 sender 변수에저장
		var sender = message[0];
		
		//메세지 내용을 content 변수에 저장
		var content = message[1];
		
		if(content != ""){		
			//대화창에 '대화명 : 메세지' 형식으로 표시
			chatWindow.innerHTML += "<div>" + sender + " : " + content + "</div>";							
		}
		
		//새메세지가 대화창에 추가되면 대화창의 스크롤막대바를 가장 아래로 이동시켜 
		//사용자가 새 메세지를 볼수 있도록 설정
	    chatWindow.scrollTop = chatWindow.scrollHeight; 	
	};    

	
	
	
	
	
</script>
</html>