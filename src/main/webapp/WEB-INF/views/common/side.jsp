<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"
    isELIgnored="false"
    %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />

<section class="menu-inner">
	<nav>
		<ul>
			<li>
				<a href="#">
					<i class="fa-solid fa-cloud"></i>
					<span class="material-symbols-outlined">home</span>
					<span>홈</span>
				</a>
			</li>
			<li>
				<a href="#">
					<i class="fa-solid fa-cloud"></i>
					<span class="material-symbols-outlined">cloud</span>
					<span>일상/교류</span>
				</a>
			</li>
			<li>
				<a href="${contextPath}/product/list">
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
				<a href="#">
					<span class="material-symbols-outlined">notifications</span>
					<span>내소식</span>
				</a>
			</li>
			<li>
				<a href="${contextPath}/chat/main.do">
					<span class="material-symbols-outlined">sms</span>
					<span>채팅</span>
				</a>
			</li>
		</ul>
	</nav>
</section>


