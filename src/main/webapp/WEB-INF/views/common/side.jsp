<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<%-- 현재 요청 URI를 JSP에서 직접 가져오기 (원래 요청 URI를 속성에서 가져옴) --%>
<c:set var="uri" value="${requestScope['javax.servlet.forward.request_uri']}" />

<c:set var="uriWithoutCtx" value="${fn:substringAfter(uri, contextPath)}" />

<!-- 홈 메뉴 active 처리 -->
<c:set var="homeClass" value="" />
<c:if test="${fn:endsWith(uriWithoutCtx, '/') || fn:contains(uriWithoutCtx, '/main')}">
  <c:set var="homeClass" value="active" />
</c:if>

<!-- 일상/교류 메뉴 active 처리 -->
<c:set var="boardClass" value="" />
<c:if test="${fn:contains(uriWithoutCtx, '/board')}">
  <c:set var="mypageClass" value="active" />
</c:if>

<!-- 중고거래 메뉴 active 처리 -->
<c:set var="productClass" value="" />
<c:if test="${fn:contains(uriWithoutCtx, '/product')}">
  <c:set var="productClass" value="active" />
</c:if>

<!-- 마이페이지 메뉴 active 처리 -->
<c:set var="mypageClass" value="" />
<c:if test="${fn:contains(uriWithoutCtx, '/mypage') and empty param.id}">
  <c:set var="mypageClass" value="active" />
</c:if>

<!-- 채팅 메뉴 active 처리 -->
<c:set var="chatClass" value="" />
<c:if test="${fn:contains(uriWithoutCtx, '/chat')}">
  <c:set var="chatClass" value="active" />
</c:if>

<section class="menu-inner">
	<nav>
		<ul>
			<li>
			  <a href="${contextPath}/" class="${homeClass}">
			    <span class="material-symbols-outlined">home</span>
			    <span>홈</span>
			  </a>
			</li>
			<li>
				<a href="${contextPath}/board" class="${boardClass}">
					<span class="material-symbols-outlined">cloud</span>
					<span>일상/교류</span>
				</a>
			</li>
			<li>
				<a href="${contextPath}/product/list" class="${productClass}">
					<span class="material-symbols-outlined">shopping_bag</span>
					<span>중고거래</span>
				</a>
			</li>
			<li>
				<a href="#">
					<span class="material-symbols-outlined">local_fire_department</span>
					<span>인기글</span>
				</a>
			</li>
			<li>
	<!-- 		<a href="#">
					<span class="material-symbols-outlined">notifications</span>
					<span>내소식</span>
				</a>
				 -->
				<a href="${contextPath}/mypage" class="${mypageClass}">
					<span class="material-symbols-outlined">account_circle</span>
					<span>마이페이지</span>
				</a>
			</li>
			<li>
				<a href="${contextPath}/chat" class="${chatClass}">
					<span class="material-symbols-outlined">sms</span>
					<span>채팅</span>
				</a>
			</li>
		</ul>
	</nav>
</section>


