<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 게시글 관리</title>
<link href="${contextPath}/resources/css/adminBoard.css" rel="stylesheet" type="text/css" media="screen">
</head>
<body>

<!-- 검색 폼 -->
<form class="header-search-form" action="${currentUri}" method="get">
	<select name="searchType">
		<option value="all" ${searchType == 'all' ? 'selected' : ''}>전체</option>
		<option value="writer" ${searchType == 'writer' ? 'selected' : ''}>작성자</option>
		<option value="content" ${searchType == 'content' ? 'selected' : ''}>내용</option>
		<option value="category" ${searchType == 'category' ? 'selected' : ''}>카테고리</option>
	</select>
	<input type="text" name="searchKeyword" value="${searchKeyword}" placeholder="검색어를 입력하세요" />
	<button type="submit">검색</button>
</form>

<!-- 게시글 목록 테이블 -->
<form action="${contextPath}/board/admin/saveOrDelete" method="post">
	<table border="1" cellspacing="0" cellpadding="5" width="100%">
		<thead>
			<tr>
				<th><input type="checkbox" id="checkAll" onclick="toggleAll(this)" /></th>
				<th>번호</th>
				<th>작성자</th>
				<th>내용</th>
				<th>카테고리</th>
				<th>작성일</th>
				<th>조회수</th>
				<th>좋아요</th>
				<th>댓글 수</th>
				<th>이미지1</th>
				<th>이미지2</th>
				<th>이미지3</th>
				<th>이미지4</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="board" items="${boardList}" varStatus="loop">
				<tr>
					<td><input type="checkbox" name="selectedIds" value="${board.boardId}" /></td>
					<td>${board.boardId}<input type="hidden" name="boards[${loop.index}].boardId" value="${board.boardId}" /></td>
					<td><input type="text" name="boards[${loop.index}].boardWriter" value="${board.boardWriter}" readonly /></td>
					<td><input type="text" name="boards[${loop.index}].boardCont" value="${board.boardCont}" /></td>
					<td>
						<select name="boards[${loop.index}].boardCate">
							<option value="일상" ${board.boardCate == '일상' ? 'selected' : ''}>일상</option>
							<option value="생활정보" ${board.boardCate == '생활정보' ? 'selected' : ''}>생활정보</option>
						</select>
					</td>
					<td><fmt:formatDate value="${board.boardDate}" pattern="yyyy-MM-dd HH:mm" /></td>
					<td>${board.boardCount}</td>
					<td>${board.boardLike}</td>
					<td>${board.boardRe}</td>
					<td>
						<c:choose>
							<c:when test="${not empty board.boardImg1}">
								<img src="${contextPath}/images/board/${board.boardImg1}" alt="이미지1" style="width:50px;" />
							</c:when>
							<c:otherwise>없음</c:otherwise>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${not empty board.boardImg2}">
								<img src="${contextPath}/images/board/${board.boardImg2}" alt="이미지2" style="width:50px;" />
							</c:when>
							<c:otherwise>없음</c:otherwise>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${not empty board.boardImg3}">
								<img src="${contextPath}/images/board/${board.boardImg3}" alt="이미지3" style="width:50px;" />
							</c:when>
							<c:otherwise>없음</c:otherwise>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${not empty board.boardImg4}">
								<img src="${contextPath}/images/board/${board.boardImg4}" alt="이미지4" style="width:50px;" />
							</c:when>
							<c:otherwise>없음</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty result.list}">
				<tr>
					<td colspan="13">검색 결과가 없습니다.</td>
				</tr>
			</c:if>
		</tbody>
	</table>

	<div class="submit-btn-class">
		<button type="submit" name="action" value="update" onclick="return confirm('변경한 내용을 저장하시겠습니까?');">저장</button>
		<button type="submit" name="action" value="delete" onclick="return confirm('선택한 게시글을 삭제하시겠습니까?');">삭제</button>
	</div>
</form>

<!-- 페이징 -->
<div class="pagination">
	<c:set var="startPage" value="${result.currentPage - (result.currentPage - 1) % 5}" />
	<c:set var="endPage" value="${startPage + 4}" />
	<c:set var="totalPage" value="${(result.totalCount + result.pageSize - 1) / result.pageSize}" />

	<c:if test="${startPage > 1}">
		<a href="${currentUri}?page=${startPage - 1}&searchKeyword=${searchKeyword}&searchType=${searchType}">&laquo;</a>
	</c:if>

	<c:forEach var="p" begin="${startPage}" end="${endPage}">
		<c:if test="${p <= totalPage}">
			<a href="${currentUri}?page=${p}&searchKeyword=${searchKeyword}&searchType=${searchType}" class="${p == result.currentPage ? 'active-page' : ''}">${p}</a>
		</c:if>
	</c:forEach>

	<c:if test="${endPage < totalPage}">
		<a href="${currentUri}?page=${endPage + 1}&searchKeyword=${searchKeyword}&searchType=${searchType}">&raquo;</a>
	</c:if>
</div>

<script>
	function toggleAll(source) {
	    const checkboxes = document.querySelectorAll('input[name="selectedIds"]');
	    for (const checkbox of checkboxes) {
	        checkbox.checked = source.checked;
	    }
	}
</script>

</body>
</html>
