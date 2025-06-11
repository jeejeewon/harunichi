<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<link href="${contextPath}/resources/css/board.css" rel="stylesheet"
	type="text/css">

<div class="container board">

	<a href="${contextPath}/board/postForm">새 게시글 작성</a>

	<div class="board-list">
		<div class="list-wrap">
			<c:forEach var="board" items="${boardList}">
				<div class="list-item">
					<div class="item">
						<p>글 번호 ${board.boardId}</p>
						<div class="item-cate">
							<span>${board.boardCate}</span>
						</div>
						<div class="user-profile">
							<div class="user-pic">
								<img
									src="https://ca-fe.pstatic.net/web-mobile/static/default-image/user/profile-80-x-80.svg">

							</div>
							<div class="user-name">${board.boardWriter}</div>
							<div class="item-date" data-date="${board.boardDate}"></div>
							<div class="item-more">
								<ul class="popup">
									<li><a>링크 복사</a></li>
									<li><a href="${contextPath}/board/editForm?boardId=${board.boardId}">수정하기</a></li>
									<li><a href="${contextPath}/board/">삭제하기</a></li>
								</ul>
							</div>
						</div>
						<a href="${contextPath}/board/view?boardId=${board.boardId}"
							class="item-content">
							<p>${board.boardCont}</p>
						</a>
						<div class="img-wrap">
							<c:forEach var="imageFileName" items="${board.imageFiles}">
								<c:if test="${not empty imageFileName}">
									<div class="img-thumb">
										<img
											src="${contextPath}/resources/images/board/${imageFileName}"
											alt="게시글 이미지" width="100">
									</div>
								</c:if>
							</c:forEach>
						</div>
						<div class="item-info">
							<p>${board.boardLike}</p>
							<p>${board.boardRe}</p>
						</div>
					</div>
				</div>
			</c:forEach>
			<c:if test="${empty boardList}">
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

    if (diffSeconds < 60) {
        return diffSeconds + "초 전";
    } else if (diffSeconds < 3600) {
        return Math.floor(diffSeconds / 60) + "분 전";
    } else if (diffSeconds < 86400) {
        return Math.floor(diffSeconds / 3600) + "시간 전";
    } else if (diffSeconds < 2592000) { // 30 days
        return Math.floor(diffSeconds / 86400) + "일 전";
    } else if (diffSeconds < 31536000) { // 365 days
        return Math.floor(diffSeconds / 2592000) + "달 전";
    } else {
        return Math.floor(diffSeconds / 31536000) + "년 전";
    }
}

document.addEventListener('DOMContentLoaded', (event) => {
    document.querySelectorAll('.item-date').forEach(span => {
        const dateString = span.dataset.date;
        const postDate = new Date(dateString);

        if (isNaN(postDate.getTime())) {
            console.error("Invalid date:", dateString);
            span.textContent = "Invalid Date";
            return;
        }

        const timestamp = postDate.getTime();
        span.textContent = formatTimeAgo(timestamp);
    });
});

// 시간이 지남에 따라 자동으로 업데이트되도록 setInterval을 사용
// 1분마다 업데이트
setInterval(() => {
    document.querySelectorAll('.item-date').forEach(span => {
        const timestamp = parseInt(span.dataset.timestamp);
        span.textContent = formatTimeAgo(timestamp);
    });
}, 60000); // 60000 밀리초 = 1분
</script>