<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!-- 컨트롤러에서 넘긴 currentUri 값 사용 -->
<c:set var="uri" value="${currentUri}" />
<c:set var="uriWithoutCtx" value="${fn:substringAfter(uri, contextPath)}" />

<!-- 홈 메뉴 active 처리 -->
<c:set var="homeClass" value="" />
<c:if test="${fn:endsWith(uriWithoutCtx, '/') || fn:contains(uriWithoutCtx, '/main')}">
  <c:set var="homeClass" value="active" />
</c:if>

<!-- 마이페이지 메뉴 active 처리 -->
<c:set var="mypageClass" value="" />
<c:if test="${fn:contains(uriWithoutCtx, '/mypage')}">
  <c:set var="mypageClass" value="active" />
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
				<a href="#">
					<span class="material-symbols-outlined">cloud</span>
					<span>일상/교류</span>
				</a>
			</li>
			<li>
				<a href="#">
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
				<a href="#">
					<span class="material-symbols-outlined">sms</span>
					<span>채팅</span>
				</a>
			</li>
		</ul>
	</nav>
</section>


