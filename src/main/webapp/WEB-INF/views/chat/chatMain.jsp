<%@page import="com.harunichi.common.util.LoginCheck"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅 메인 페이지</title>
<link href="${contextPath}/resources/css/chat/chatMain.css" rel="stylesheet" >
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body>
	<form id="chatForm" action="<%=request.getContextPath()%>/chat/window" method="POST">
		<input type="hidden" id="receiverId" name="receiverId"> 
		<input type="hidden" id="receiverNick" name="receiverNick"> 		
		<!-- 개인채팅일 경우! 나중에 단체채팅과 구분할 조건값 필요 -->
		<input type="hidden" id="chatType" name="chatType" value="personal">
	</form>	
	<div style="margin-bottom: 80px;">
	<p id="recText">채팅친구추천</p>
		<div id="chatMainCon">		
			<a href="#" class="btn pre"><i class="bi bi-arrow-left"></i></a>
			<div class="chat-slider-container">
				<ul class="profile-list">
					<c:forEach var="member" items="${memberList}">						
						<li>
							<div class="profile-con">
								<a href="#"> <!-- 클릭시 상대방 프로필 정보 -->
									<img class="profile-img" src="<c:url value='/resources/images/chat/${member.profileImg}'/>" alt="프로필사진">
								</a>
								<p class="nick">${member.nick}</p>
								<p><span style="color: #a3daff; font-weight: bold; ">LIKE </span>${member.myLike}</p>
								<a href="#" class="do-chat-btn" data-id="${member.id}" onclick="chatOpen(this);">채팅하기</a>
							</div>
						</li>
					</c:forEach>						
				</ul>
			</div>	
			<a href="#" class="btn next"><i class="bi bi-arrow-right"></i></a>
		</div>
	</div>
	<div>
	<div id="openTitle">
		<p id="recText">오픈채팅방</p>
		<a href="#" id="newOpenChatBtn" onclick="openModal()">만들기</a>
	</div>
		<div id="openChatCon">
			<ul class="open-chat-list">
				<c:forEach var="openChat" items="${openChatList}">
					<li data-room-id="${openChat.roomId}" onclick="doOpenChat(this);">
						<div class="open-chat-item">
							<a id="doOpenChat" href="#" >
								<img class="open-chat-img" src="../resources/images/chat/profile4.png" alt="오픈채팅방 프로필사진">						
							</a>	
							<div class="open-chat-info">
								<p class="open-chat-title">${openChat.title} <span>(<span>${openChat.userCount} / </span>${openChat.persons})</span></p>
								<p class="open-chat-content">채팅방 소개</p>
							</div>
						</div>
					</li>
				</c:forEach>					
			</ul>
		</div>	
	</div>
	<!-- 모달창 영역 -->
	<div id="myModal" class="modal">
	  <div class="modal-content">
	    <span class="close" onclick="closeModal()">&times;</span>
	    <h2>오픈채팅방 만들기</h2>
	    <form action="<%=request.getContextPath()%>/chat/window" id="newChatForm" method="POST">
		    <label>채팅방 제목</label>	    
		    <input id="openChatTitle" name="title" class="open-chat-form" type="text" maxlength="20">
		    <p>최대 20자까지 입력 가능합니다.</p>
		    <label>채팅방 인원</label>		    
		    <input id="openChatPersons" name="persons" class="open-chat-form" type="number" min="2" max="8">
		    <p>최대 8명까지 입장 가능합니다.</p>
		    <div class="modal-btn-wrap">
			    <button class="modal-btn" onclick="confirmAction()">만들기</button>
			    <button class="modal-btn" type="button" onclick="closeModal()">취소</button>
		    </div>
		    <input type="hidden" name="chatType" value="group">
	    </form>
	  </div>
	</div>
</body>

<script type="text/javascript">

	function chatOpen(btn){
		//'채팅하기'버튼이 눌린 카드의 멤버 ID를 input에 저장
		const receiverId = btn.getAttribute("data-id");
		document.getElementById("receiverId").value = receiverId;
		document.getElementById("chatForm").submit();		
	}	
	
	
	//추천친구 캐러셀
	let currentIndex = 0;
	
	const list = document.querySelector(".profile-list");
	const items = document.querySelectorAll(".profile-list li");
	const cardWidth = 230 + 20; // 카드 너비 + gap
	const visibleCards = 4;	
	const totalCards = items.length;
	const maxIndex = totalCards - visibleCards;
	
	document.querySelector(".btn.next").addEventListener("click", (e) => {
	  e.preventDefault();
	  if (currentIndex < maxIndex) {
	    currentIndex++;
	    updateSlide();
	  }
	});
	
	document.querySelector(".btn.pre").addEventListener("click", (e) => {
	  e.preventDefault();
	  if (currentIndex > 0) {
	    currentIndex--;
	    updateSlide();
	  }
	});
	
	function updateSlide() {
		const moveX = currentIndex * cardWidth;
		list.style.transform = "translateX(-" + moveX + "px)";
	}
	
	
	//모달창
	function openModal() {
		
		event.preventDefault();
		  const userId = '<%= session.getAttribute("id") == null ? "" : session.getAttribute("id") %>';

		  if (!userId) {
		    location.href = "<%= request.getContextPath() %>/chat/window";
		    return;
		  }
	  document.getElementById("myModal").style.display = "block";
	}

	function closeModal() {
	  document.getElementById("myModal").style.display = "none";
	}

	function confirmAction() {	
		document.getElementById("newChatForm").submit();	
	}

	
	//생성된 오픈 채팅에 참여하는 함수
	function doOpenChat(btn){
		console.log("클릭함");	
		
		const roomId = btn.getAttribute("data-room-id");
		
		location.href = "<%= request.getContextPath()%>/chat/window?roomId=" + roomId;
		
	}
		

	
	
</script>

</html>