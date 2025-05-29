<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>      
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅 메인 페이지</title>
</head>
<script type="text/javascript">
//로그인 상황을 가정하기 위한 변수 저장 -----------------------------------------------나중에 수정해야함
	var id = "hong";
	var nick = "홍반장";	
</script>

<body>

	<h2>채팅목록</h2>
	
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