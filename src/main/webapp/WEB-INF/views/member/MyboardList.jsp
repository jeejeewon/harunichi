<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<%-- 게시글이든 거래글이든 공통 리스트 변수 설정 --%>
<c:set var="list" value="${not empty boardList ? boardList : likeProductList}" />
<c:set var="likedMap" value="${likedPosts}" />

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<link href="${contextPath}/resources/css/board.css" rel="stylesheet" type="text/css">

<div class="container board main">
	<div class="board-list">
		<div class="list-wrap">
			<c:forEach var="board" items="${list}">
				<div class="list-item">
					<div class="item">
						<div class="item-cate">
							<span>${board.boardCate}</span>
						</div>
						<div class="user-profile">
							<div class="user-pic">
								<c:choose>
									<c:when test="${empty profileImgPath}">
										<img src="https://ca-fe.pstatic.net/web-mobile/static/default-image/user/profile-80-x-80.svg">
									</c:when>
									<c:otherwise>
										<img id="profileImage" src="${profileImgPath}" alt="프로필 이미지">
									</c:otherwise>
								</c:choose>
							</div>
							<div class="user-name">${board.boardWriter}</div>
							<div class="item-date" data-date="${board.boardDate}"></div>
						</div>

						<a href="${contextPath}/board/view?boardId=${board.boardId}" class="item-content">
							<p>${board.boardCont}</p>
							<div class="img-wrap">
								<c:forEach var="imageFileName" items="${board.imageFiles}">
									<c:if test="${not empty imageFileName}">
										<div class="img-thumb">
											<img src="${contextPath}/resources/images/board/${imageFileName}" alt="게시글 이미지" width="100">
										</div>
									</c:if>
								</c:forEach>
							</div>
						</a>

						<div class="item-info">
							<div class="like ${likedMap[board.boardId] ? 'liked' : ''}" data-board-id="${board.boardId}">
								<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
									<path d="M12 20a1 1 0 0 1-.437-.1C11.214 19.73 3 15.671 3 9a5 5 0 0 1 8.535-3.536l.465.465.465-.465A5 5 0 0 1 21 9c0 6.646-8.212 10.728-8.562 10.9A1 1 0 0 1 12 20z" />
								</svg>
							</div>
							<p>좋아요 ${board.boardLike}</p>
							<p>댓글수 ${board.boardRe}</p>
						</div>
					</div>
				</div>
			</c:forEach>

			<c:if test="${empty list}">
				<div class="empty">
					<p>게시글이 없습니다.</p>
				</div>
			</c:if>
		</div>
	</div>
</div>

<script>
function formatTimeAgo(timestamp) {
	const now = new Date();
	const postDate = new Date(timestamp);
	const diffSeconds = Math.floor((now - postDate) / 1000);

	if (diffSeconds >= 86400) {
		const year = postDate.getFullYear();
		const month = ('0' + (postDate.getMonth() + 1)).slice(-2);
		const day = ('0' + postDate.getDate()).slice(-2);
		return year + '.' + month + '.' + day + '.';
	}

	if (diffSeconds < 60) return diffSeconds + "초 전";
	if (diffSeconds < 3600) return Math.floor(diffSeconds / 60) + "분 전";
	return Math.floor(diffSeconds / 3600) + "시간 전";
}

function updateTimeElements() {
	document.querySelectorAll('.item-date').forEach(span => {
		const dateString = span.dataset.date;
		const postDate = new Date(dateString);
		if (isNaN(postDate.getTime())) {
			span.textContent = "Invalid Date";
			return;
		}
		span.dataset.timestamp = postDate.getTime();
		span.textContent = formatTimeAgo(postDate.getTime());
	});
}

document.addEventListener('DOMContentLoaded', updateTimeElements);

setInterval(() => {
	document.querySelectorAll('.item-date').forEach(span => {
		const timestamp = parseInt(span.dataset.timestamp);
		if (!isNaN(timestamp)) {
			span.textContent = formatTimeAgo(timestamp);
		}
	});
}, 60000);

// 좋아요 버튼 클릭 이벤트 (AJAX)
$('.like').on('click', function () {
	const $likeBtn = $(this);
	const boardId = $likeBtn.data('board-id');
	const isLoggedIn = ${not empty sessionScope.member};

	if (!isLoggedIn) {
		alert('좋아요 기능은 로그인 후 이용 가능합니다.');
		if (confirm('로그인 페이지로 이동하시겠습니까?')) {
			window.location.href = '${contextPath}/member/loginForm';
		}
		return;
	}

	const isLiked = $likeBtn.hasClass('liked');
	const url = isLiked ? '${contextPath}/board/like/cancel' : '${contextPath}/board/like';

	$.ajax({
		url: url,
		type: 'POST',
		data: { boardId: boardId },
		success: function (response) {
			if (response === 'login') {
				alert('로그인 후 이용 가능합니다.');
				window.location.href = '${contextPath}/member/loginForm';
			} else if (response === 'error') {
				alert('처리 중 오류가 발생했습니다.');
			} else {
				$likeBtn.toggleClass('liked');
				$likeBtn.siblings('p:contains("좋아요")').text('좋아요 ' + response);
			}
		},
		error: function () {
			alert('서버 오류');
		}
	});
});
</script>
