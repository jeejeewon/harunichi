<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<!-- <c:if test="${not empty sessionScope.member and sessionScope.member.nick eq board.boardWriter}">
</c:if> -->

<c:if test="${not empty param.id and param.id ne sessionScope.member.id}">
  <script type="text/javascript">
    alert('잘못된 접근입니다.');
    history.back();
  </script>
</c:if>

<!-- 스타일 css -->
<link href="${contextPath}/resources/css/member/mypage.css" rel="stylesheet" type="text/css" media="screen">
<!--세션 값 확인 
<pre>
sessionScope.member: ${sessionScope.member} <br>
sessionScope.member.id: ${sessionScope.member.id} <br>
sessionScope.member.nick: ${sessionScope.member.nick} <br>
sessionScope.member.email: ${sessionScope.member.email} <br>
sessionScope.member.profileImg: ${sessionScope.member.profileImg} <br>
</pre> 
-->
<section class="mypage-wrap">
	<div class="mypage-inner-header">
		<a href="javascript:void(0);" onclick="history.back();">
    		<img src="${contextPath}/resources/icon/back_icon.svg" alt="뒤로가기버튼">
		</a>
	</div>
	<div class="mypage-profile-area">
		<div class="profile-area-left">
			
			<c:choose>
			   	<c:when test="${not empty sessionScope.member.profileImg}">
			       	<img src="${pageContext.request.contextPath}/images/profile/${sessionScope.member.profileImg}" alt="프로필 이미지">
			   	</c:when>
			   	<c:otherwise>
			       	<img src="${contextPath}/resources/icon/basic_profile.jpg">
			   	</c:otherwise>
			</c:choose>
		</div>
		<div class="profile-area-middle">
			<div class="nick-and-email">
				<p>${sessionScope.member.nick}</p>
				<p>${sessionScope.member.email}</p>
			</div>
			<div class="follow-area">
				<a href="${contextPath}/followList.do?type=following&id=${sessionScope.member.id}" class="follow-btn">
					00팔로우중
				</a>
				<a class="follow-btn">
					00팔로워
				</a>
			</div>
		</div>
		<div class="profile-area-right">
			<a href="#"><img src="${contextPath}/resources/icon/chat_line_icon.svg" class="on-icons"></a>
			<button>팔로우하기</button><!-- 타인에게노출 -->
			<button>프로필 수정</button><!-- 나에게 노출 -->
		</div>
	</div>
	<div class="mypage-contents-area">
		
	</div>
</section>