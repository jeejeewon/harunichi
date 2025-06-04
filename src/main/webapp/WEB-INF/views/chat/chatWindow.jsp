<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅창</title>
<style type="text/css">
body {
	margin: 0;
	height: 100vh;
	display: flex;
	justify-content: center;
	align-items: center;
}

/* 채팅 전체 화면 */
#chatContainer {
	width: 100%;
	height: 100%;
	display: flex;
	flex-direction: column;
	background-color: white;
	align-items: center;
}
/* 대화창 */
#messageContainer {
	width: 80%;
	height: 90%;
	padding: 10px;
	overflow: scroll;
	padding: 5px;
	display: flex;
  	flex-direction: column;
}
/* 채팅알림 */
.chat-text {
	text-align: center;
	background-color: #fcfcfc;
	margin: 5px 0;
}
/* 하단 입력창 전체 */
#inputContainer {
	width: 80%;
	display: flex;
	align-content: center;
	border: 1px solid #d1d1d1;
	padding: 5px 10px;
	margin-top: 10px;
	align-items: center;
}
/* 메세지 입력창 input */
#chatMessage {
	flex: 1;
	border: none;
	outline: none;
	font-size: 14px;
	padding: 8px;
	height: 100px;
}
/* 전송 버튼 */
#sendBtn {
	position: relative;
	background-color: #d4edf7;
	border: none;
	padding: 8px 20px;
	margin-left: 10px;
	border-radius: 15px;
	cursor: pointer;
	font-weight: bold;
	height: 60px;
}

#closeBtn {
	margin-bottom: 3px; /* 종료 버튼의 하단 여백 설정 */
	position: relative; /* 종료 버튼 위치 조정을 위해 relative 포지션 설정 */
	top: 2px; /* 종료 버튼을 약간 아래로 이동 */
	left: -2px; /* 종료 버튼을 약간 왼쪽으로 이동 */
}

#senderId {
	width: 158px; /* 대화명 입력창 너비 설정 */
	height: 24px; /* 대화명 입력창 높이 설정 */
	border: 1px solid #AAAAAA; /* 대화명 입력창 테두리 설정 */
	background-color: #EEEEEE; /* 대화명 입력창 배경색 설정 */
}

/* 메세지 */
.my-msg, .other-msg {
  max-width: 60%;
  padding: 8px 12px;
  margin: 4px 0;
  border-radius: 10px;
  position: relative;
  display: inline-block;
  word-break: break-word;
  color: black;
}
.my-msg {
	background-color: #d4edf7;
	align-self: flex-end;
}
.other-msg {
	background-color: #f1f0f0;
	align-self: flex-start;
}
.my-msg .time,
.other-msg .time {
  display: block;
  font-size: 0.75em;
  color: #666;
  text-align: right;
  margin-top: 5px;
}

</style>
</head>
<body>
	<div id="chatContainer">
		<div id="chatTop">
			<!-- 현재 채팅하는 사람의 대화명을 input에 표시 , 읽기전용으로 설정하여 수정 불가  -->
			나의 닉네임 : <input type="text" id="senderId" value="${param.id}" readonly><br>
			상대방 닉네임 : <input type="text" id="receiverId" value="${param.receiverId}" readonly><br>		
	
			<!-- 채팅 종료 버튼 클릭시 disconnect()함수 호출 -->
			<button id="closeBtn" onclick="disconnect();">채팅 종료</button>
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
</body>

<script type="text/javascript">
	/*
		상태 유지
	   - 웹소켓은 브라우저와 서버 간의 연결 상태를 readyState 속성으로 유지하여, 현재 연결 상태를 확인하고 조작할 수 있습니다.
	   - 연결 상태는 CONNECTING, OPEN, CLOSING, CLOSED의 네 가지 상태로 표현됩니다.
	*/	
								
	var chatWindow, chatMessage, senderId;
	var webSocket;
	var time;
	var receiverId, roomId;	

	window.onload = function() {
		chatWindow = document.getElementById("messageContainer");	//대화 내용이 표시될 대화창 영역
		chatMessage = document.getElementById("chatMessage");		//메세지 입력창
		senderId = document.getElementById("senderId").value;		//채팅 하는 사용자의 대화명 요소에서 대화명값 얻기 
		
		receiverId = document.getElementById("receiverId").value;	//받는 사람 ID 저장
		roomId = "${roomId}";										//채팅방 ID 저장
//   	console.log("id : " + "${param.id}");
//   	console.log("nick : " + "${param.nick}");
//   	console.log("receiverId : " + "${param.receiverId}");

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
		webSocket = new WebSocket("<%=application.getInitParameter("CHAT_ADDR")%>/ChatingServer");

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

			if (content != "") {			
				//대화창에 '대화명 : 메세지' 형식으로 표시
				chatWindow.innerHTML += "<div class='other-msg'>"
					  + "<div class='message'>" + content +  "</div>"
					  + "<span class='time'>" + formatTime(time) + "</span>"
					  + "</div>";		
			}

			//새메세지가 대화창에 추가되면 대화창의 스크롤막대바를 가장 아래로 이동시켜 
			//사용자가 새 메세지를 볼수 있도록 설정
			chatWindow.scrollTop = chatWindow.scrollHeight;
		};
		
	}
	

	
	
	//메세지 전송 함수 : 채팅 사용자가 메세지 전송 버튼을 클릭하거나 엔터 키를 눌렀을때 호출
	function sendMessage() {
		

		if (chatMessage.value != "") {
			//보낸 시간 출력
			time = formatTime();			
		}else{ return; } //아무것도 적지 않고 전송할 경우
		
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
							  + "<span class='time'>" + time + "</span>"
							  + "</div>";

		//웹 소켓 통로를 통해 메세지를 서버페이지로 전송
		webSocket.send(JSON.stringify(chatData));

		//메세지 입력 창 내용 초기화
		chatMessage.value = "";

		//대화창 스크롤 막대바를 맨 아래로 이동하여 새로운 메세지가 나타나면 보이게 설정
		chatWindow.scrollTop = chatWindow.scrollHeight;
	}

	//서버와 웹 소켓 통로 연결을 종료하는 함수 : 사용자가 '채팅종료' 버튼을 클릭했을때 호출
	function disconnect() {
		webSocket.close(); //웹 소켓 통로연결 끊기 ( 웹브라우저, 서버페이지 연결 끊김 )
	}

	//메세지 입력창에서 Enter키를 누르고 땠을 경우
	//자동으로 sendMessage함수를 호출하도록처리
	function enterKey() {
		//Enter키의 키코드값 = 13
		if (window.event.keyCode == 13) {
			sendMessage();
		}
	}


</script>
</html>