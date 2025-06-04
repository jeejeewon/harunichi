<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>      
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅 메인 페이지</title>
<style type="text/css">
/* 친구추천 */
#chatMainCon{
	display: flex;
	align-items: center;      
	justify-content: center;   
	gap: 30px;                
}
.profile-con {	
	display: flex;
	width: 280px;
	height: 330px;
	flex-direction: column;
	align-items: center;
	justify-content: center; 
	text-align: center;
	border: 1px solid #ccc;
	border-radius: 10px;
	padding: 20px 0;
	margin: 0 10px;
}
.profile-con:hover{
	transform: translateY(-8px); 
	box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}
.profile-img {
	width: 140px;
	height: 140px;
	border-radius: 100%;
	object-fit: cover;
}
.nick {
	font-weight: bold;
	font-size: 26px;
	margin: 10px 0;
}
#recText {
	font-size: 30px;
	color: #a3daff;
	font-weight: bold;
	margin-bottom: 40px;
}
.chat-slider-container {
	width: 900px;
	overflow-x: hidden; /* 가로 스크롤만 숨기고 */
	overflow-y: visible; /* 세로는 보이게! */
	position: relative; /* 자식 요소의 위치 기준 */
	padding: 10px 0 20px 0; /* 살짝 위 공간 줘도 괜찮음 */
}
.profile-list {
	display: flex;
	transition: transform 0.4s ease;
	list-style: none; 
}
.profile-list li {
	flex: 0 0 280px; /* 카드 하나의 너비 고정 */
}
.do-chat-btn {
	display: flex;
	justify-content: center; 
	align-items: center;    
	text-align: center;
	border-radius: 8px;	
	font-size:20px;
	color: #fff;
	width: 150px;
	height: 40px;
	background-color: #a3daff;
	margin-top: 15px;
	text-decoration: none;
}
.do-chat-btn:hover { 
	background-color: #53a5dc; 
}
.btn {
	font-size: 40px;
	color: #a3daff;
}
.btn:hover {
	color: #53a5dc; 
}


/* 오픈채팅 */
.open-chat-img {
	width: 60px;
	height: 60px;
	border-radius: 50%;
	background-color: #d6eef5; 
	object-fit: cover;
}
.open-chat-list {
	list-style: none;
	padding: 0;
	margin: 0;
}
.open-chat-item {
	display: flex;
	align-items: center;
	gap: 15px;
	padding: 12px 0;
	border-bottom: 1px solid #e0e0e0;
	cursor: pointer;
}
.open-chat-info {
	display: flex;
	flex-direction: column;
	justify-content: center;
}
.open-chat-title {
	font-weight: bold;
	color: #444;
	margin: 0;
}
.open-chat-content {
	font-size: 0.9rem;
	color: #888;
	margin-top: 4px;
}
#openChatCon {
  display: flex;
  justify-content: center;  /* 가로 중앙 */
  flex-direction: column;
  padding: 0 90px 0 80px;
}
.open-chat-item:hover {
  background-color: #f0f8ff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-3px); /* 살짝 올라감 */
}
</style>
</head>
<script type="text/javascript">
//로그인 상황을 가정하기 위한 변수 저장 -----------------------------------------------나중에 수정해야함
//	var id = "hong";
//	var nick = "홍반장";	
</script>

<body>
	<form id="chatForm" action="<%=request.getContextPath()%>/chat/window" method="POST">
		<!-- 나중에 세션에서 값 가져와야함 -->			
		<input id="id" name="id" type="text" placeholder="아이디">
		<input id="nick" name="nick" type="text" placeholder="닉네임">
<!-- 	<c:set var="id" value="${sessionScope.id}" />
		<c:set var="nick" value="${sessionScope.nick}" /> 	
		<input type="hidden" name="id" value="${id}">
		<input type="hidden" name="nick" value="${nick}">-->
		<input type="hidden" id="receiverId" name="receiverId" value=""> 
		
		<!-- 개인채팅일 경우! 나중에 단체채팅과 구분할 조건값 필요 -->
		<input type="hidden" id="chatType" name="chatType" value="personal">
	</form>	
	<div style="margin-bottom: 80px;">
	<p id="recText">채팅친구추천</p>
		<div id="chatMainCon">		
			<a href="#" class="btn pre">◀</a>
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
			<a href="#" class="btn next">▶</a>
		</div>
	</div>
	<div>
	<p id="recText">오픈채팅방</p>
	<a href="#">채팅방만들기</a>
		<div id="openChatCon">
			<ul class="open-chat-list">
				<li>
					<div class="open-chat-item">
						<a href="#">
							<img class="open-chat-img" src="../resources/images/chat/profile4.png" alt="오픈채팅방 프로필사진">						
						</a>	
						<div class="open-chat-info">
							<p class="open-chat-title">채팅방 제목 (인원수)</p>
							<p class="open-chat-content">채팅방 소개</p>
						</div>
					</div>
				</li>
				<li>
					<div class="open-chat-item">
						<a href="#">
							<img class="open-chat-img" src="../resources/images/chat/profile5.png" alt="오픈채팅방 프로필사진">						
						</a>
						<div class="open-chat-info">						
							<p class="open-chat-title">채팅방 제목 (인원수)</p>
							<p class="open-chat-content">채팅방 소개</p>						
						</div>
					</div>
				</li>
				<li>
					<div class="open-chat-item">
						<a href="#">
							<img class="open-chat-img" src="../resources/images/chat/profile6.png" alt="오픈채팅방 프로필사진">						
						</a>
						<div class="open-chat-info">
							<p class="open-chat-title">채팅방 제목 (인원수)</p>
							<p class="open-chat-content">채팅방 소개</p>
						</div>
					</div>
				</li>								
			</ul>
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
	const cardWidth = 280 + 20; // 카드 너비 + gap
	const visibleCards = 3;	
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
	
	
</script>

</html>