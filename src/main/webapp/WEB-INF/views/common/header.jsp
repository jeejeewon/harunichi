<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"
    isELIgnored="false"
    %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />



<section class="header-inner">

    <a href="${contextPath}" class="header-logo"><img src="${contextPath}/resources/image/logo2.svg"></a>
    
    <div class="header-main-content">
    	<form class="header-search-form" action="/search" method="get">
        	<input type="text" placeholder="검색어를 입력하세요">
       	 	<button type="submit">
				<img src="${contextPath}/resources/image/search_icon.svg" alt="검색">
			</button>
    	</form>
	    <nav class="header-menu">
	        <ul>
	            <li>
	                <select id="country-select" name="country">
	                    <option value="ko" data-image="${contextPath}/resources/image/south-korea_icon.png"${selectedCountry == 'ko' ? 'selected' : ''}>Korea</option>
	                    <option value="jp" data-image="${contextPath}/resources/image/japan_icon.png"${selectedCountry == 'jp' ? 'selected' : ''}>Japan</option>
	                </select>
	            </li>
	            <li>
	            	<c:if test="${not empty sessionScope.id}">
	            	 	<a href="#" class="profile-area">
		            		<div class="profile-image">
								<%-- 세션에 profileImg 값이 있을 경우 이미지 경로 사용, 없으면 기본 이미지 --%>
                                <img src="${not empty sessionScope.profileImg ? sessionScope.profileImg : contextPath}/resources/image/basic_profile.jpg">
		            		</div>
		            		<%-- 세션에 nick 값이 있을 경우 닉네임 표시, 없으면 "익명" 또는 다른 기본값 표시 --%>
                            <span>${not empty sessionScope.nick ? sessionScope.nick : '익명'}</span>
		            	</a>
	            	</c:if>
	            	<%-- 세션에 id 값이 없을 경우 (비회원 상태) --> 로그인 링크 노출 --%>
	            	<c:if test="${empty sessionScope.id}">
                        <a href="${contextPath}/member/loginpage.do">
                            <span>로그인</span>
                        </a>
                    </c:if>
	            </li>
	            <li><a href="#"><img src="${contextPath}/resources/image/chat_icon.svg"></a></li>
	            <li><a href="#"><img src="${contextPath}/resources/image/bell_icon.svg"></a></li>
	            <li><a href="#"><img src="${contextPath}/resources/image/grid_icon.svg"></a></li>
	        </ul>
	    </nav>
    </div>
    
</section>