<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script src="http://code.jquery.com/jquery-latest.min.js"></script>

<div class="container board">
	board list임~

	<div class="search-bar">검색 바 영역</div>

	<div class="board-list">
		<p>전체 글 목록</p>
		<table>
			<thead>
				<tr>
					<th>번호</th>
					<th>내용</th>
					<th>날짜</th>
					<th>이미지</th>
					<th>좋아요</th>
					<th>조회수</th>
					<th>댓글</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="board" items="${boardList}">
					<tr>
						<td>${board.boardId}</td>
						<td>${board.boardCont}</td>
						<td>${board.boardDate}</td>
						<td>${board.boardImg}</td>
						<td>${board.boardLike}</td>
						<td>${board.boardCount}</td>
						<td>${board.boardRe}</td>
					</tr>
				</c:forEach>
				<c:if test="${empty boardList}">
					<tr>
						<td colspan="7">게시글이 없습니다.</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
</div>