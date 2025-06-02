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
						<td><img src="/board/upload/${board.boardImg1}" alt="Image 1"></td>
						<td><img src="/board/upload/${board.boardImg2}" alt="Image 2"></td>
						<td><img src="/board/upload/${board.boardImg3}" alt="Image 3"></td>
						<td><img src="/board/upload/${board.boardImg4}" alt="Image 4"></td>
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