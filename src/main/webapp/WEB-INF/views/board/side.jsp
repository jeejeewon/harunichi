<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<div class="board-side">
	<form action="${contextPath}/board/search" method="get">
		<input type="text" name="keyword" placeholder="작성자, 닉네임 또는 내용 검색"
			value="${keyword}">
		<button type="submit">검색</button>
	</form>
</div>
