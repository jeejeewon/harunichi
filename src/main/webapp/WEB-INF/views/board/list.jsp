<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<script src="http://code.jquery.com/jquery-latest.min.js"></script>

<div class="container board">

	<a href="${contextPath}/board/postForm">새 게시글 작성</a>

	<div class="board-list">
		<table>
			<thead>
				<tr>
					<th>ID</th>
					<th>작성자</th>
					<th>제목</th>
					<th>작성일</th>
					<th>좋아요</th>
					<th>조회수</th>
					<th>댓글수</th>
					<th>이미지1</th>
					<th>이미지2</th>
					<th>이미지3</th>
					<th>이미지4</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="board" items="${boardList}">
					<tr>
						<td>${board.boardId}</td>
						<td>${board.boardWriter}</td>
						<td>${board.boardCont}</td>
						<td>${board.boardDate}</td>
						<td>${board.boardLike}</td>
						<td>${board.boardCount}</td>
						<td>${board.boardRe}</td>
						<c:forEach var="imageFileName" items="${board.imageFiles}">
							<c:if test="${not empty imageFileName}">
								<td><img
									src="${contextPath}/resources/images/board/${imageFileName}"
									alt="게시글 이미지" width="100"></td>
							</c:if>
						</c:forEach>
					</tr>
				</c:forEach>
				<c:if test="${empty boardList}">
					<tr>
						<td colspan="10">게시글이 없습니다.</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
</div>