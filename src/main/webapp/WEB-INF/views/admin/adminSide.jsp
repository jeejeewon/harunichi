<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%-- 현재 요청 URI를 JSP에서 직접 가져오기 (원래 요청 URI를 속성에서 가져옴) --%>
<c:set var="uri" value="${requestScope['javax.servlet.forward.request_uri']}" />

<c:set var="uriWithoutCtx" value="${fn:substringAfter(uri, contextPath)}" />

<!-- 홈 메뉴 active -->
<c:set var="homeClass" value="" />
<c:if test="${uriWithoutCtx eq '/admin' || uriWithoutCtx eq '/admin/'}">
  <c:set var="homeClass" value="active" />
</c:if>

<!-- 일상/교류 메뉴 active -->
<c:set var="boardClass" value="" />
<c:if test="${fn:contains(uriWithoutCtx, '/board')}">
  <c:set var="memberClass" value="active" />
</c:if>

<!-- 중고거래 관리 메뉴 active -->
<c:set var="productClass" value="" />
<c:if test="${fn:contains(uriWithoutCtx, '/product')}">
  <c:set var="productClass" value="active" />
</c:if>

<!-- 회원 관리 메뉴 active -->
<c:set var="memberClass" value="" />
<c:if test="${fn:contains(uriWithoutCtx, '/member')}">
  <c:set var="memberClass" value="active" />
</c:if>

<!-- 채팅 메뉴 active 처리 -->
<c:set var="chatClass" value="" />
<c:if test="${fn:contains(uriWithoutCtx, '/chat')}">
  <c:set var="chatClass" value="active" />
</c:if>

<section class="side-wrap">
	<div class="logo-area">
		<img src="${contextPath}/resources/icon/admin_logo.png">
	</div>
	<div class="menu-area">
		<nav>
			<ul>

				<li>
					<a href="${contextPath}/admin" class="${homeClass}">
						<span class="material-symbols-outlined">home</span>
						<span>홈</span>
					</a>
				</li>
				<li>
					<a href="${contextPath}/admin/board" class="${boardClass}">
						<span class="material-symbols-outlined">cloud</span>
						<span>일상/교류 관리</span>
					</a>
				</li>
				<li>
					<a href="${contextPath}/admin/product" class="${productClass}">
						<span class="material-symbols-outlined">shopping_bag</span>
						<span>중고거래 관리</span>
					</a>
				</li>
				<li>
					<a href="${contextPath}/admin/인기글">
						<span class="material-symbols-outlined">local_fire_department</span>
						<span>인기글 관리</span>
					</a>
				</li>
				<li>
					<a href="${contextPath}/admin/member" class="${memberClass}">
						<span class="material-symbols-outlined">account_circle</span>
						<span>마이페이지 관리</span>
					</a>
				</li>
				<li>
					<a href="${contextPath}/admin/chat" class="${chatClass}">
						<span class="material-symbols-outlined">sms</span>
						<span>채팅 관리</span>
					</a>
				</li>
			</ul>
		</nav>
	</div>
	<div class="back-home">
		<a href="${contextPath}">홈페이지로 돌아가기</a>
	</div>
</section>

	
