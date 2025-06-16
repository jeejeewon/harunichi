<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<link href="${contextPath}/resources/css/board.css" rel="stylesheet"
	type="text/css">


<div class="container board">

	<h1>${board.boardCont}</h1>

	<div class="post-info">
		<div class="user-profile">
			<div class="user-pic">
				<img
					src="https://ca-fe.pstatic.net/web-mobile/static/default-image/user/profile-80-x-80.svg">
			</div>
			<div class="user-name">${board.boardWriter}</div>
		</div>
		<div class="post-meta">
			<span class="item-date" data-date="${board.boardDate}"></span> | <span>조회수
				${board.boardCount}</span> | <span>댓글 수 ${board.boardRe}</span>
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

	<div class="comment-section">
		<p>댓글(${board.boardRe})</p>

		<p>댓글 목록</p>
		<div class="conmment-list">
			<c:forEach var="reply" items="${replyList}">
				<div class="comment" id="reply-${reply.replyId}">
					<%-- 댓글 div에 ID 추가 --%>
					<div class="comment-author">${reply.replyWriter}</div>
					<div class="comment-content">${reply.replyCont}</div>
					<div class="comment-date">${reply.replyDate}</div>				
					<c:if
						test="${not empty sessionScope.member and sessionScope.member.nick eq reply.replyWriter}">
						<div class="comment-actions">
							<button class="modify-btn" data-reply-id="${reply.replyId}">수정</button>
							<button class="delete-btn" data-reply-id="${reply.replyId}"
								data-board-id="${board.boardId}">삭제</button>
						</div>
					</c:if>

					<%-- 댓글 수정 폼 (처음에는 숨김) --%>
					<div class="modify-form" style="display: none;">
						<textarea class="modify-textarea">${reply.replyCont}</textarea>
						<button class="save-modify-btn" data-reply-id="${reply.replyId}">저장</button>
						<button class="cancel-modify-btn">취소</button>
					</div>
				</div>
			</c:forEach>
		</div>

		<c:if test="${not empty sessionScope.id}">
			<%-- 댓글 작성 폼 추가 --%>
			<div class="comment-form">
				<h3>댓글 작성</h3>
				<form action="${contextPath}/board/reply/write" method="post">
					<input type="hidden" name="replyWriter"
						value="${sessionScope.member.nick}"> <input type="hidden"
						name="boardId" value="${board.boardId}">
					<textarea name="replyCont" placeholder="댓글을 입력하세요." required></textarea>
					<button type="submit">등록</button>
				</form>
			</div>
		</c:if>
		<c:if test="${empty sessionScope.id}">
			<p>로그인 후 댓글 작성 가능합니다.</p>
		</c:if>
	</div>
</div>

<%-- 날짜 포맷팅 스크립트 등 필요한 스크립트 추가 --%>
<script>
	// item-date 클래스를 가진 요소들을 찾아서 날짜 포맷을 보기 좋게 바꿔주는 스크립트
	// 예: 2023-08-22T10:30:00 -> 2023.08.22 10:30
	document.querySelectorAll('.item-date').forEach(
			function(element) {
				var rawDate = element.getAttribute('data-date');
				if (rawDate) {
					var date = new Date(rawDate);
					var year = date.getFullYear();
					var month = ('0' + (date.getMonth() + 1)).slice(-2);
					var day = ('0' + date.getDate()).slice(-2);
					var hours = ('0' + date.getHours()).slice(-2);
					var minutes = ('0' + date.getMinutes()).slice(-2);
					element.textContent = year + '.' + month + '.' + day + '. '
							+ hours + ':' + minutes;
				}
			});
</script>

<%-- 댓글 삭제 이벤트 & 댓글 수정 AJAX --%>
<script>
    // 임시 member_id 설정 (JSP 변수와 동일하게)
    var currentUserId = "admin"; // <<<<<<< 임시 member_id 설정

    // 삭제 버튼 클릭 이벤트
    document.querySelectorAll('.delete-btn').forEach(button => {
        button.addEventListener('click', function() {
            var replyId = this.getAttribute('data-reply-id');
            var boardId = this.getAttribute('data-board-id');

            if (confirm('정말 이 댓글을 삭제하시겠습니까?')) {
                // 삭제 요청을 보낼 폼 생성 (POST 방식으로)
                var form = document.createElement('form');
                form.setAttribute('method', 'post');
                form.setAttribute('action', '${pageContext.request.contextPath}/board/deleteReply');

                var replyIdField = document.createElement('input');
                replyIdField.setAttribute('type', 'hidden');
                replyIdField.setAttribute('name', 'replyId');
                replyIdField.setAttribute('value', replyId);
                form.appendChild(replyIdField);

                var boardIdField = document.createElement('input'); 
                boardIdField.setAttribute('type', 'hidden');
                boardIdField.setAttribute('name', 'boardId');
                boardIdField.setAttribute('value', boardId);
                form.appendChild(boardIdField);
             
                document.body.appendChild(form);
                form.submit();
            }
        });
    });

    // 수정 버튼 클릭 이벤트
    document.querySelectorAll('.modify-btn').forEach(button => {
        button.addEventListener('click', function() {
            var replyId = this.getAttribute('data-reply-id');
            var commentDiv = document.getElementById('reply-' + replyId);
            var contentDiv = commentDiv.querySelector('.comment-content');
            var actionsDiv = commentDiv.querySelector('.comment-actions');
            var modifyFormDiv = commentDiv.querySelector('.modify-form');
            var modifyTextarea = modifyFormDiv.querySelector('.modify-textarea');

            // 현재 내용을 textarea에 설정하고 수정 폼을 보이게 함
            modifyTextarea.value = contentDiv.innerText.trim();
            contentDiv.style.display = 'none'; 
            actionsDiv.style.display = 'none'; 
            modifyFormDiv.style.display = 'block';
        });
    });

    // 수정 저장 버튼 클릭 이벤트 (AJAX 사용)
    document.querySelectorAll('.save-modify-btn').forEach(button => {
        button.addEventListener('click', function() {
            var replyId = this.getAttribute('data-reply-id');
            var commentDiv = document.getElementById('reply-' + replyId);
            var modifyFormDiv = commentDiv.querySelector('.modify-form');
            var modifyTextarea = modifyFormDiv.querySelector('.modify-textarea');
            var newReplyCont = modifyTextarea.value;

            // AJAX 요청 보내기
            fetch('${pageContext.request.contextPath}/board/updateReply', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded', // 또는 'application/json'
                },
                body: 'replyId=' + encodeURIComponent(replyId) + '&replyCont=' + encodeURIComponent(newReplyCont)
                // JSON 형식으로 보내려면 body: JSON.stringify({ replyId: replyId, replyCont: newReplyCont })
                // 그리고 Controller에서 @RequestBody 사용
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    // 성공 시 댓글 내용 업데이트 및 폼 숨김
                    var contentDiv = commentDiv.querySelector('.comment-content');
                    var actionsDiv = commentDiv.querySelector('.comment-actions');
                    contentDiv.innerText = newReplyCont; 
                    contentDiv.style.display = 'block';
                    actionsDiv.style.display = 'block'; 
                    modifyFormDiv.style.display = 'none';
                    alert(data.message);
                } else {
                    alert(data.message); 
                    // 실패 시에도 폼을 숨기고 원래 내용 표시
                    var contentDiv = commentDiv.querySelector('.comment-content');
                    var actionsDiv = commentDiv.querySelector('.comment-actions');
                    contentDiv.style.display = 'block';
                    actionsDiv.style.display = 'block';
                    modifyFormDiv.style.display = 'none';
                }
            })
            .catch((error) => {
                console.error('Error:', error);
                alert('댓글 수정 중 오류가 발생했습니다.');
                // 오류 시에도 폼을 숨기고 원래 내용 표시 
                var contentDiv = commentDiv.querySelector('.comment-content');
                var actionsDiv = commentDiv.querySelector('.comment-actions');
                contentDiv.style.display = 'block';
                actionsDiv.style.display = 'block';
                modifyFormDiv.style.display = 'none';
            });
        });
    });

    // 수정 취소 버튼 클릭 이벤트
    document.querySelectorAll('.cancel-modify-btn').forEach(button => {
        button.addEventListener('click', function() {
            var commentDiv = this.closest('.comment'); // 가장 가까운 .comment 부모 찾기
            var contentDiv = commentDiv.querySelector('.comment-content');
            var actionsDiv = commentDiv.querySelector('.comment-actions');
            var modifyFormDiv = commentDiv.querySelector('.modify-form');

            // 폼 숨기고 원래 내용과 액션 버튼 다시 보임
            contentDiv.style.display = 'block';
            actionsDiv.style.display = 'block';
            modifyFormDiv.style.display = 'none';
        });
    });

</script>