<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<!-- <c:if test="${not empty sessionScope.member and sessionScope.member.nick eq board.boardWriter}">
</c:if> -->

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
<body>
	<section class="mypage-wrap">
		<div class="mypage-inner-header">
			<a href="javascript:void(0);" onclick="history.back();">
	    		<img src="${contextPath}/resources/icon/back_icon.svg" alt="뒤로가기버튼">
			</a>
		</div>
		<div class="mypage-profile-area">
			<div class="profile-area-left">
				<c:choose>
				    <c:when test="${not empty pageOwner.profileImg}">
				        <img src="${contextPath}/images/profile/${pageOwner.profileImg}" alt="프로필 이미지">
				    </c:when>
				    <c:otherwise>
				        <img src="${contextPath}/resources/icon/basic_profile.jpg" alt="기본 이미지">
				    </c:otherwise>
				</c:choose>
			</div>
			<div class="profile-area-middle">
				<div class="nick-and-email">
					<span>${pageOwner.nick}</span>
					<span>${pageOwner.email}</span>
				</div>
				<div class="join-date">
					<img src="${contextPath}/resources/icon/calendar_icon.svg"><p>가입일: <fmt:formatDate value="${pageOwner.joindate}" pattern="yyyy년 M월 d일" /></p>
				</div>
				<div class="follow-area">
				    <a href="${contextPath}/mypageFollow.do?id=${pageOwner.id}&type=following" id="followingCount">
				    	<span>${followingCount}</span> 팔로우중
				    </a>
				    <a href="${contextPath}/mypageFollow.do?id=${pageOwner.id}&type=follower" id="followerCount">
				    	<span>${followerCount}</span> 팔로워
				    </a>
				</div>
				<div class="chat-edit-follow">
					<!-- 타인 프로필이면 채팅하기 버튼 표시 -->
					<c:if test="${not isMyPage}">
					    <a href="#" class="chat-btn"><img src="${contextPath}/resources/icon/chat_line_icon.svg" class="on-icons"></a>
					</c:if>
					<!-- 나의 마이페이지면 수정 버튼 표시 -->
					<c:if test="${isMyPage}">
					    <a href="${contextPath}/member/updateMyInfoForm.do" class="edit-btn">프로필 수정</a>
					</c:if>
					<!-- 타인 프로필이면 팔로우 버튼 표시 -->
					<c:if test="${not isMyPage}">
					    <button id="followBtn" class="follow-btn" onclick="follow('${contextPath}', '${pageOwner.id}')">팔로우</button>
					</c:if>
				</div>
			</div>
		</div>
		<div class="mypage-contents-area">
			
		</div>
	</section>
	
	<script type="text/javascript">
		document.addEventListener("DOMContentLoaded", function() {
		    const contextPath = '${contextPath}';
		    const followeeId = '${pageOwner.id}';
		    const btn = document.getElementById("followBtn");
		
		    // 페이지 로드 시 현재 팔로우 상태를 서버에 요청해서 초기 버튼 상태를 설정
		    fetch(contextPath + "/follow/isFollowing?followeeId=" + encodeURIComponent(followeeId))
		        .then(response => response.json())
		        .then(isFollowing => {
		            if (isFollowing) {
		                // 이미 팔로우 중인 경우 버튼을 '팔로잉' 상태로 세팅
		                setFollowingButton(contextPath, followeeId);
		            }
		        });
		});
		
		// '팔로잉' 상태 버튼 세팅 함수 (마우스 올리면 '언팔로우', 클릭하면 언팔로우 실행)
		function setFollowingButton(contextPath, followeeId) {
		    const btn = document.getElementById("followBtn");
		    btn.textContent = "팔로잉";
		    btn.onclick = null; // 기존 클릭 이벤트 제거
		    btn.onmouseenter = function() {
		        btn.textContent = "언팔로우";
		    };
		    btn.onmouseleave = function() {
		        btn.textContent = "팔로잉";
		    };
		    btn.onclick = function() {
		        unfollow(contextPath, followeeId);
		    };
		}
		
		// '팔로우' 상태 버튼 세팅 함수 (클릭하면 팔로우 실행)
		function setFollowButton(contextPath, followeeId) {
		    const btn = document.getElementById("followBtn");
		    btn.textContent = "팔로우";
		    btn.onclick = function() {
		        follow(contextPath, followeeId);
		    };
		    btn.onmouseenter = null;
		    btn.onmouseleave = null;
		}
		
		// 팔로우 요청을 서버로 보내는 함수
		function follow(contextPath, followeeId) {
		    fetch(contextPath + "/follow/add", {
		        method: "POST",
		        headers: { "Content-Type": "application/x-www-form-urlencoded" },
		        body: "followeeId=" + encodeURIComponent(followeeId)
		    })
		    .then(response => response.text())
		    .then(data => {
		        if (data === "success") {
		            // 서버에서 팔로우 성공 응답 -> 버튼 상태와 팔로워 수 갱신
		            setFollowingButton(contextPath, followeeId);
		            updateFollowerCount(contextPath, followeeId);
		            updateFollowingCount(contextPath);
		        } else {
		            alert("팔로우 실패");
		        }
		    })
		    .catch(() => alert("서버 오류"));
		}
		
		// 언팔로우 요청을 서버로 보내는 함수 (확인창 포함)
		function unfollow(contextPath, followeeId) {
		    if (confirm("언팔로우하시겠습니까?")) {
		        fetch(contextPath + "/follow/remove", {
		            method: "POST",
		            headers: { "Content-Type": "application/x-www-form-urlencoded" },
		            body: "followeeId=" + encodeURIComponent(followeeId)
		        })
		        .then(response => response.text())
		        .then(data => {
		            if (data === "success") {
		                // 언팔로우 성공 -> 버튼 상태와 팔로워 수 갱신
		                setFollowButton(contextPath, followeeId);
		                updateFollowerCount(contextPath, followeeId);
		                updateFollowingCount(contextPath);
		            } else {
		                alert("언팔로우 실패");
		            }
		        })
		        .catch(() => alert("서버 오류"));
		    }
		}
		
		// 현재 팔로워 수를 서버에서 받아와 화면에 갱신하는 함수
		function updateFollowerCount(contextPath, followeeId) {
		    fetch(contextPath + "/follow/followerCount?followeeId=" + encodeURIComponent(followeeId))
		        .then(response => response.text())
		        .then(count => {
		            // 팔로워 수를 표시하는 요소의 텍스트를 업데이트
		            document.getElementById("followerCount").textContent = count + " 팔로워";
		        });
		}
		
		// 현재 팔로잉 수를 서버에서 받아와 하면에 갱신하는 함수
		function updateFollowingCount(contextPath) {
		    fetch(contextPath + "/follow/followingCount")
		        .then(response => response.text())
		        .then(count => {
		            document.getElementById("followingCount").textContent = count + " 팔로우중";
		        });
		}

	</script>

</body>