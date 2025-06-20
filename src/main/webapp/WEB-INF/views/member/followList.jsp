<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<!-- 스타일 css -->
<link href="${contextPath}/resources/css/member/followList.css" rel="stylesheet" type="text/css" media="screen">

<body>
	<section class="follow-wrap">
	    <div class="follow-inner-header">
			<a href="javascript:void(0);" onclick="history.back();">
	    		<img src="${contextPath}/resources/icon/back_icon.svg" alt="뒤로가기버튼">
			</a>
			<a class="follow-inner-header-user-info" href="${contextPath}/mypage?id=${pageOwner.id}">
				<p>${pageOwner.nick}</p>
				<span>${pageOwner.email}</span>
			</a>
		</div>
	
	    <div class="follow-tabs">
			<a href="${contextPath}/follow/mypageFollow?id=${pageOwner.id}&type=following" class="${activeTab == 'following' ? 'active' : ''}">
			  <span>${followingCount}</span> 팔로우중
			</a>
			
			<a href="${contextPath}/follow/mypageFollow?id=${pageOwner.id}&type=follower" class="${activeTab == 'follower' ? 'active' : ''}">
			  <span>${followerCount}</span> 팔로워
			</a>
		</div>

	    <div class="follow-list">
	        <c:forEach var="follow" items="${followList}">
	            <div class="follow-item">
	            <a href="${contextPath}/mypage?id=${follow.id}">
	             <img src="${contextPath}/images/profile/${follow.profileImg != null ? follow.profileImg : 'basic_profile.jpg'}" alt="프로필">
	                <div class="follow-info">
	                    <span>${follow.nick}</span>
	                    <span>${follow.email}</span>
	                </div>
	            </a>
	               
	            </div>
	        </c:forEach>
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
		    btn.classList.remove("follow", "unfollow");
		    btn.classList.add("following");
		
		    btn.onclick = null; // 기존 클릭 이벤트 제거
		
		    btn.onmouseenter = function() {
		        btn.textContent = "언팔로우";
		        btn.classList.remove("following");
		        btn.classList.add("unfollow");
		    };
		
		    btn.onmouseleave = function() {
		        btn.textContent = "팔로잉";
		        btn.classList.remove("unfollow");
		        btn.classList.add("following");
		    };
		
		    btn.onclick = function() {
		        unfollow(contextPath, followeeId);
		    };
		}
		
		function setFollowButton(contextPath, followeeId) {
		    const btn = document.getElementById("followBtn");
		    btn.textContent = "팔로우";
		    btn.classList.remove("following", "unfollow");
		    btn.classList.add("follow");
		
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