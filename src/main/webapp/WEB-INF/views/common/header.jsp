<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"
    isELIgnored="false"
    %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<section class="header-inner">

    <a href="#" class="header-logo"><img src="${contextPath}/resources/image/logo2.svg"></a>
    
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
	                <label for="country-select">lang</label>
	                <select id="country-select" name="country">
	                    <option value="ko" data-image="${contextPath}/resources/image/south-korea_icon.png">Korea</option>
	                    <option value="jp" data-image="${contextPath}/resources/image/japan_icon.png">Japan</option>
	                </select>
	            </li>
	            <li>
		            <a href="#" class="profile-area">
		            	<div class="profile-image">
		            		<img src="${contextPath}/resources/image/basic_profile.jpg">
		            	</div>
		            	<span>user</span><!-- 세션에서 유저닉네임 가져와서 추가할 예정 -->
		            </a>
	            </li>
	            <li><a href="#"><img src="${contextPath}/resources/image/chat_icon.svg"></a></li>
	            <li><a href="#"><img src="${contextPath}/resources/image/bell_icon.svg"></a></li>
	            <li><a href="#"><img src="${contextPath}/resources/image/grid_icon.svg"></a></li>
	        </ul>
	    </nav>
    </div>
    
</section>