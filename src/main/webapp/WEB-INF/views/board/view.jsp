<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<link href="${contextPath}/resources/css/board.css" rel="stylesheet"type="text/css">


<div class="container board-view"> 

	<h1>${board.boardCont}</h1> 

    <div class="post-info">
        <div class="user-profile">
            <div class="user-pic">
                <img src="https://ca-fe.pstatic.net/web-mobile/static/default-image/user/profile-80-x-80.svg">
            </div>
            <div class="user-name">${board.boardWriter}</div>
        </div>
        <div class="post-meta">
            <span class="item-date" data-date="${board.boardDate}"></span> |
            <span>조회수 ${board.boardCount}</span> |
            <span>댓글 수 ${board.boardRe}</span>
        </div>
    </div>

	<div class="post-content">
		<p>${board.boardCont}</p>
	</div>

    <div class="post-images">
        <c:forEach var="imageFileName" items="${board.imageFiles}">
            <c:if test="${not empty imageFileName}">
                <div class="post-img-thumb">
                    <img src="${contextPath}/resources/images/board/${imageFileName}"
                         alt="게시글 이미지"> 
                </div>
            </c:if>
        </c:forEach>
    </div>

	<div class="post-actions">      
        <button>좋아요 (${board.boardLike})</button>
        <button onclick="location.href='${contextPath}/board/list'">목록으로</button> 
	</div>
 
    <div class="comments-section">
        <h2>댓글 (${board.boardRe})</h2>
        <div class="comments-section">
    <h2>댓글(${board.boardRe})</h2>

    <%-- 여기에 댓글 목록 보여주는 코드 --%>
    <%-- 댓글 목록을 반복문을 사용하여 표시 --%>
    <%-- <c:forEach var="reply" items="${replyList}"> --%>
        <%-- <div class="comment"> --%>
            <%-- <div class="comment-author">${reply.replyWriter}</div> --%>
            <%-- <div class="comment-content">${reply.replyCont}</div> --%>
            <%-- <div class="comment-date">${reply.replyDate}</div> --%>
        <%-- </div> --%>
    <%-- </c:forEach> --%>

    <%-- 댓글 작성 폼 추가 --%>
    <div class="comment-form">
        <h3>댓글 작성</h3>
        <%-- 댓글 작성을 위한 폼. 액션 URL은 댓글을 처리할 컨트롤러 매핑 주소입니다. --%>
        <form action="${contextPath}/reply/write" method="post">
            <%-- 어떤 게시글에 대한 댓글인지 넘겨줍니다. --%>
            <input type="hidden" name="boardId" value="${board.boardId}">
            <%-- 댓글 내용을 입력하는 부분 --%>
            <textarea name="replyCont" placeholder="댓글을 입력하세요." required></textarea>
            <%-- 댓글 작성 버튼 --%>
            <button type="submit">댓글 등록</button>
        </form>
    </div>
</div>
    </div>
    
</div>

<%-- 날짜 포맷팅 스크립트 등 필요한 스크립트 추가 --%>
<script>
    // item-date 클래스를 가진 요소들을 찾아서 날짜 포맷을 보기 좋게 바꿔주는 스크립트
    // 예: 2023-08-22T10:30:00 -> 2023.08.22 10:30
    document.querySelectorAll('.item-date').forEach(function(element) {
        var rawDate = element.getAttribute('data-date');
        if (rawDate) {
            var date = new Date(rawDate);
            var year = date.getFullYear();
            var month = ('0' + (date.getMonth() + 1)).slice(-2);
            var day = ('0' + date.getDate()).slice(-2);
            var hours = ('0' + date.getHours()).slice(-2);
            var minutes = ('0' + date.getMinutes()).slice(-2);
            element.textContent = year + '.' + month + '.' + day + '. ' + hours + ':' + minutes;
        }
    });
</script>