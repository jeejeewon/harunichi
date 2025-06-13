<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅창</title>
<link href="${contextPath}/resources/css/chat/chatWindow.css" rel="stylesheet" >
</head>
<body>
	<div class="chat-center-wrapper">
		<div id="chatContainer">
			<div id="chatTop">
				<div class="chat-top-left">
					<a href="#">
						<img class="profile-img" src="../resources/images/chat/profile1.png" alt="프로필사진">
					</a>
					<div class="room-info">
						<span class="room-title" id="receiverId">${title}<span class="user-count">(${count})</span></span>
					</div>		
				</div>
				<div class="chat-top-right">
				    <button class="disconnect-btn" onclick="disconnect();">×</button>
				</div>		
			</div>
			<!-- 대화창, 수신된 메세지와 전송한 메세지가 표시 되는 영역 -->
			<div id="messageContainer"></div>
	
			<div id="inputContainer">
				<!-- 메세지 입력창,  키보드 이벤트 발생시 enterKey() 함수 호출 -->
				<input type="text" id="chatMessage" onkeyup="enterKey();">
	
				<!-- 메세지 전송 버튼 , 클릭시 sendMessage() 함수 호출 -->
				<button id="sendBtn" onclick="sendMessage();">전송</button>
			</div>
		</div>
	</div>
	
</body>

<script type="text/javascript">
	/*
		상태 유지
	   - 웹소켓은 브라우저와 서버 간의 연결 상태를 readyState 속성으로 유지하여, 현재 연결 상태를 확인하고 조작할 수 있습니다.
	   - 연결 상태는 CONNECTING, OPEN, CLOSING, CLOSED의 네 가지 상태로 표현됩니다.
	*/	
								
	var chatWindow, chatMessage, senderId;
	var webSocket;
	var receiverId;
	var roomId = "${roomId}";	 //채팅방 ID 저장
	var senderId = "${sessionScope.id}";
	
	window.onload = function() {
		chatWindow = document.getElementById("messageContainer");	//대화 내용이 표시될 대화창 영역
		chatMessage = document.getElementById("chatMessage");		//메세지 입력창	
		receiverId = document.getElementById("receiverId").value;	//받는 사람 ID 저장								

		//과거 메세지 불러오기
		fetch("${pageContext.request.contextPath}/chat/history?roomId=" + roomId)
			.then(response => response.json())
			.then(messages => {
				messages.forEach(msg => {
					const sender = msg.senderId;
					const message = msg.message;
					const time = formatTime(new Date(msg.sentTime));
					
					chatWindow.innerHTML += "<div class='" + (sender === senderId ? "my-msg" : "other-msg") + "'>" 
										  + "<div class='message'>" 
										  + message + "</div>"
										  + "<span class='time'>" + time + "</span>" + "</div>";
				});
				chatWindow.scrollTop = chatWindow.scrollHeight;				
				//과거 메세지 출력 후 웹소켓 연결
				connectWebSocket();				
			});
	}
	
	
	//보낸 시간 포맷팅
	function formatTime(date) {
		const d = date ? new Date(date) : new Date(); // 인자 없으면 지금 시간
		const hours = d.getHours();
		const minutes = d.getMinutes().toString().padStart(2, '0');
		const ampm = hours >= 12 ? "오후" : "오전";
		const displayHour = hours % 12 === 0 ? 12 : hours % 12;
		return ampm + " " + displayHour + ":" + minutes;
	}
	
	
	
	//웹소켓 연결
	function connectWebSocket(){
		//웹 소켓 객체 생성 : JSP의 application내장객체를 통해 요청할 채팅 서버페이지 주소로 웹소켓을 만들어 연결 
		webSocket = new WebSocket("<%=application.getInitParameter("CHAT_ADDR")%>/ChattingServer?roomId=" + roomId);

		//서버에 웹소켓 통로 연결이 성공적으로 이루어진 이벤트가 발생했을때 호출되는 이벤트 핸들러 함수 설정
		//대화창에 연결 성공 메세지를 보여주기 위해 출력
		webSocket.onopen = function(event) {
			chatWindow.innerHTML += "<p class='chat-text'>채팅방에 입장하였습니다.</p><br/>";
			chatWindow.scrollTop = chatWindow.scrollHeight;
		};

		//웹소켓 통로와 연결된 서버페이지와의 연결이 종료될때의 이벤트가 발생하면 호출되는 이벤트 핸들러 함수 설정
		//대화창에 연결 종료 메세지를 출력
		webSocket.onclose = function(event) {
			chatWindow.innerHTML += "<p class='chat-text'>채팅방 연결이 종료되었습니다.</p><br/>";
			chatWindow.scrollTop = chatWindow.scrollHeight;
		};

		//웹소켓 통로와 통신중 오류가 발생하는 이벤트가 일어나면 호출되는 이벤트 핸들러 함수 설정
		//오류 발생시 알림창에 오류메시지 표시하고 대화창에 에러 메세지 출력
		webSocket.onerror = function(event) {
			alert(event.data);
			chatWindow.innerHTML += "<p class='chat-text'>채팅 중에 에러가 발생하였습니다.</p><br/>";
			chatWindow.scrollTop = chatWindow.scrollHeight;
		};

		//서버페이지에서 웹소켓 통로를 통해 클라이언트가 보낸 메세지를 보내어서 여기로 수신했을때(메아리)
		webSocket.onmessage = function(event) {
			
			console.log(event.data);
			//수신된 데이터 '대화명 | 메세지'를 '|'기준으로 분리하여 배열로 저장
			var message = event.data.split("|");
			//대화명을 sender 변수에저장
			var sender = message[0];
			//메세지 내용을 content 변수에 저장
			var content = message[1];
			
			var time = formatTime();

			if (content != "") {			
				//대화창에 '대화명 : 메세지' 형식으로 표시
				chatWindow.innerHTML += "<div class='other-msg'>"
					  + "<div class='message'>" + content +  "</div>"
					  + "<span class='time'>" + time + "</span>"
					  + "</div>";		
			}

			//새메세지가 대화창에 추가되면 대화창의 스크롤막대바를 가장 아래로 이동시켜 
			//사용자가 새 메세지를 볼수 있도록 설정
			chatWindow.scrollTop = chatWindow.scrollHeight;
		};
		
	}
	
	
	//1000바이트까지 입력 가능하도록 하는 함수
	function getByteLength(str) {
		return new Blob([str]).size;
	}
	document.getElementById("chatMessage").addEventListener("input", function () {
		let msg = this.value;
		let maxByte = 1000;
		
		//입력한 메세지가 1000바이트를 넘는다면 뒤에 입력한 내용을 잘라냄
		while (getByteLength(msg) > maxByte) {
		  msg = msg.slice(0, -1); // 뒤에서부터 잘라냄
		}		
		this.value = msg;
	});
	
	
	//메세지 전송 함수 : 채팅 사용자가 메세지 전송 버튼을 클릭하거나 엔터 키를 눌렀을때 호출
	function sendMessage() {
		
		//아무것도 적지 않고 전송할 경우
		if (chatMessage.value == "") { return; }
		
		//새로운 채팅!
		const chatData = {
							roomId : "${roomId}",
							chatType : "${param.chatType}", //개인채팅인지, 단체채팅인지
							senderId : senderId, //보낸 사람
							receiverId : "${param.receiverId}",
							message : chatMessage.value //메세지	
		};
		
		//사용자가 입력한 메세지를 대화창에서 얻어 오른쪽 정렬로 디자인 추가
		chatWindow.innerHTML += "<div class='my-msg'>"
							  + "<div class='message'>" + chatMessage.value + "</div>"
							  + "<span class='time'>" + formatTime() + "</span>"
							  + "</div>";

		//웹 소켓 통로를 통해 메세지를 서버페이지로 전송
		webSocket.send(JSON.stringify(chatData));

		//메세지 입력 창 내용 초기화
		chatMessage.value = "";

		//대화창 스크롤 막대바를 맨 아래로 이동하여 새로운 메세지가 나타나면 보이게 설정
		chatWindow.scrollTop = chatWindow.scrollHeight;
	}

	
	//메세지 입력창에서 Enter키를 누르고 땠을 경우
	//자동으로 sendMessage함수를 호출하도록처리
	function enterKey() {
		//Enter키의 키코드값 = 13
		if (window.event.keyCode == 13) {
			sendMessage();
		}
	}
	
	
	//서버와 웹 소켓 통로 연결을 종료하는 함수 : 사용자가 '채팅종료' 버튼을 클릭했을때 호출
	function disconnect() {
		webSocket.close(); //웹 소켓 통로연결 끊기 ( 웹브라우저, 서버페이지 연결 끊김 )
		window.location.href="<%=request.getContextPath()%>/chat/main";
	}


</script>
</html>