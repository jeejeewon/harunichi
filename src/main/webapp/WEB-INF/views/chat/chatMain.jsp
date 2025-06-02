<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>      
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅 메인 페이지</title>
<style type="text/css">
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
.profile-list {
	display: flex;
	gap: 20px; 
	list-style: none; 
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
	var id = "hong";
	var nick = "홍반장";	
</script>

<body>
	<div style="margin-bottom: 80px;">
	<p id="recText">채팅친구추천</p>
		<div id="chatMainCon">		
			<a href="#" class="btn pre">◀</a>
			<ul class="profile-list">						
				<li>
					<div class="profile-con">
						<a href="#"> <!-- 클릭시 상대방 프로필 정보 -->
							<img class="profile-img" src="../resources/images/chat/profile1.PNG" alt="프로필사진">
						</a>
						<p class="nick">닉네임</p>
						<p>안녕하세요~!!!</p>
						<a href="#" class="do-chat-btn">채팅하기</a>
					</div>
				</li>
				<li>
					<div class="profile-con">
						<a href="#"> <!-- 클릭시 상대방 프로필 정보 -->
							<img class="profile-img" src="../resources/images/chat/profile2.PNG" alt="프로필사진">
						</a>
						<p class="nick">닉네임</p>
						<p>안녕하세요~!!!</p>
						<a href="#" class="do-chat-btn">채팅하기</a>
					</div>
				</li>
				<li>
					<div class="profile-con">
						<a href="#"> <!-- 클릭시 상대방 프로필 정보 -->
							<img class="profile-img" src="../resources/images/chat/profile3.PNG" alt="프로필사진">
						</a>
						<p class="nick">닉네임</p>
						<p>안녕하세요~!!!</p>
						<a href="#" class="do-chat-btn">채팅하기</a>
					</div>
				</li>
			</ul>	
			<a href="#" class="btn next">▶</a>
		</div>
	</div>
	<div>
	<p id="recText">오픈채팅방</p>
		<div id="openChatCon">
			<ul class="open-chat-list">
				<li>
					<div class="open-chat-item">
						<a href="#">
							<img class="open-chat-img" src="../resources/images/chat/profile4.PNG" alt="오픈채팅방 프로필사진">						
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
							<img class="open-chat-img" src="../resources/images/chat/profile5.PNG" alt="오픈채팅방 프로필사진">						
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
							<img class="open-chat-img" src="../resources/images/chat/profile6.PNG" alt="오픈채팅방 프로필사진">						
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


	
	<!-- 로그인한 사용자의 id와 닉네임, 프로필 사진명을 가져옴 -->
				
	<form id="chatForm" action="<%=request.getContextPath()%>/chat/window">
		<!-- 나중에 세션에서 값 가져와야함 -->	
		<c:set var="id" value="hong" />
		<c:set var="nick" value="홍반장" />
		<input type="hidden" name="id" value="${id}">
		<input type="hidden" name="nick" value="${nick}">
		<input type="hidden" name="receiverId" value="kim"> <!-- 임시 대상자 받는 사람 -->
		
		<!-- 개인채팅일 경우! 나중에 단체채팅과 구분할 조건값 필요 -->
		<input type="hidden" name="chatType" value="personal">
	</form>	
	
	<button onclick="chatOpen();" type="button">채팅 참여</button>
</body>

<script type="text/javascript">

	function chatOpen(){
		
		var id = "${nick}";
			
		//입력한 대화명을 파라미터로전달한 ChatWindow.jsp를 새롭게 팝업창에 보여줌
		//location.href = "window?chatId=" + id + "";
		
		document.getElementById("chatForm").submit();
		
	}	
	
</script>

</html>