<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:forEach var="board" items="${boardList}">
	<div class="list-item">
		<div class="item">
			<div class="item-cate">
				<span>${board.boardCate}</span>
			</div>
			<div class="item-head">
				<div class="user-profile">
					<div class="user-pic">
						<%-- member 프로필 사진 가져오기 --%>
						<c:if test="${empty board.boardWriterImg}">
							<img
								src="https://ca-fe.pstatic.net/web-mobile/static/default-image/user/profile-80-x-80.svg">
						</c:if>
						<c:if test="${not empty board.boardWriterImg}">
							<img id="profileImage" src="${board.boardWriterImg}"
								alt="선택한 프로필 이미지">
						</c:if>
					</div>
					<div class="user-name">${board.boardWriter}</div>
					<div class="item-date" data-date="${board.boardDate}"></div>
				</div>
				<div class="item-more">
					<div class="btn-more">
						<img width="20" height="20"
							src="https://img.icons8.com/ios-glyphs/20/more.png" alt="more" />
					</div>
					<ul class="popup">
						<li><a>링크 복사</a></li>
						<c:if
							test="${not empty sessionScope.member and sessionScope.member.nick eq board.boardWriter}">
							<li><a
								href="${contextPath}/board/editForm?boardId=${board.boardId}">수정하기</a>
							</li>
							<li>
								<form action="${contextPath}/board/delete" method="post"
									style="display: inline;">
									<input type="hidden" name="boardId" value="${board.boardId}">
									<button type="submit" onclick="return confirm('정말 삭제하시겠습니까?');">삭제하기</button>
								</form>
							</li>
						</c:if>
					</ul>
				</div>
			</div>
			<a href="${contextPath}/board/view?boardId=${board.boardId}"
				class="item-content">
				<p>
					<c:out value="${board.boardCont}" escapeXml="false" />
				</p>
				<div class="img-wrap">
					<c:forEach var="imageFileName" items="${board.imageFiles}">
						<c:if test="${not empty imageFileName}">
							<div class="img-thumb">
								<img
									src="${contextPath}/resources/images/board/${imageFileName}"
									alt="게시글 이미지">
							</div>
						</c:if>
					</c:forEach>
				</div>
			</a>
			<div class="item-info">
				<div class="like  ${likedPosts[board.boardId] ? 'liked' : ''}"
					data-board-id="${board.boardId}">
					<svg xmlns="http://www.w3.org/2000/svg"
						class="${isLiked ? 'liked' : ''}">
							    <path
							d="M12 20a1 1 0 0 1-.437-.1C11.214 19.73 3 15.671 3 9a5 5 0 0 1 8.535-3.536l.465.465.465-.465A5 5 0 0 1 21 9c0 6.646-8.212 10.728-8.562 10.9A1 1 0 0 1 12 20z" />
							  </svg>
					<p>${board.boardLike}</p>
				</div>
				<div class="reply">
					<img width="25" height="25"
						src="https://img.icons8.com/material-outlined/25/speech-bubble-with-dots.png"
						alt="speech-bubble-with-dots" />
					<p>${board.boardRe}</p>
				</div>
			</div>
		</div>
	</div>
</c:forEach>
<c:if test="${empty boardList}">
	<div class="empty">
		<p>게시글이 없습니다.</p>
	</div>
</c:if>

<script>
$('.btn-more').on('click', function(e) {
	e.stopPropagation(); // 이벤트 버블링 방지
	var $popup = $(this).siblings('.popup');
	$('.popup').not($popup).hide(); // 다른 popup은 닫기
	$popup.toggle(); // 현재 popup toggle
});

$('.popup').on('click', function(e) {
	e.stopPropagation();
});

$(document).on('click', function() {
	$('.popup').hide();
});
</script>