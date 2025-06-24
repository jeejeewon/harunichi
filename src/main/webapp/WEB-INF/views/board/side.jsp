<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<div class="board-side">
	<div class="search-box">
		<form action="${contextPath}/board/search" method="get" class="search-form">
			<input type="text" name="keyword" placeholder="원하는 글을 찾아보세요"
				value="${keyword}">
			<button type="submit">
				<img src="/harunichi/resources/icon/search_icon.svg" alt="검색">
			</button>
		</form>
	</div>

	<div class="hots-list">
		<h3>실시간 HOT</h3>
		<ul>
		<c:forEach var="top" items="${top5List}">
			<li>
				<a href="${contextPath}/board/view?boardId=${top.boardId}">
				<p class="item-cate">${top.boardCate}</p>
				<div class="item-content">${top.boardCont}</div>
				</a>			
			</li>
		</c:forEach>
		</ul>	
	</div>
</div>