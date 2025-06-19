<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>member</title>
</head>
<body>
	<h2>회원 관리</h2>
	<!-- 검색 폼 : list.do로 검색어와 typ=member 전달 -->
	<form class="header-search-form" action="${contextPath}/admin/list.do" method="get">
		<input type="hidden" name="type" value="member">
        <input type="text" name="searchKeyword" value="${searchKeyword}" placeholder="검색어를 입력하세요">
       	 	<button type="submit">
				<img src="${contextPath}/resources/icon/search_icon.svg" alt="검색">
			</button>
    </form>

    <!-- 회원 목록 테이블 -->
    <table>
    	<thead>
    		<tr>
    			<th><input type="checkbox" id="checkAll"></th><!-- 전체 선택용 체크박스 -->
    			<th>아이디</th>
    			<th>비밀번호</th>
    			<th>이름</th>
    			<th>닉네임</th>
    			<th>국적</th>
    			<th>생년월일</th>
    			<th>성별</th>
    			<th>이메일</th>
    			<th>전화번호</th>
    			<th>주소</th>
    			<th>관심사</th>
    			<th>프로필 이미지</th>
    			<th>팔로워 수</th><!-- 팔로우 테이블은 따로 있음. 이쪽에서는 팔로워수 수정불가! 팔로워 관리 페이지에서 수정할수있음 -->
    			<th>가입일</th>
    			<th>카카오ID</th>
    			<th>네이버ID</th>
    		</tr>
    	</thead>
    	<tbody>
    		<!-- 회원 데이터 출력 -->
    		<c:forEach var="member" items="${result.list}">
    			<tr>
    				<td><input type="checkbox" name="selectedIds" value="${member.id}"></td>
    				<td>${member.id}</td>
    				<td>${member.pass}</td>
    				<td>${member.name}</td>
    				<td>${member.nick}</td>
    				<td>${member.contry}</td>
    				<td>${member.year}</td>
    				<td>${member.gender}</td>
    				<td>${member.email}</td>
    				<td>${member.tel}</td>
    				<td>${member.address}</td>
    				<td>${member.myLike}</td>
    				<td>
                    	<c:if test="${not empty member.profileImg}">
                    		<img src="${contextPath}/images/profile/${member.profileImg}" alt="프로필 이미지" width="30">
                    	</c:if>
                	</td>
    				<td>${member.follower}</td>
    				<td>${member.joindate}</td>
    				<td>${member.kakao_id}</td>
    				<td>${member.naver_id}</td>
    			</tr>
    		</c:forEach>
    		<!-- 데이터가 없을 경우 -->
    		<c:if test="${empty result.list}">
    			<tr>
    				<td colspan="17">등록된 회원이 없습니다.</td>
    			</tr>
    		</c:if>
    	</tbody>
    </table>
    
    <!-- 페이징 -->
	<div class="pagination">
	    <c:set var="startPage" value="${result.currentPage - (result.currentPage-1) % 5}" />
	    <c:set var="endPage" value="${startPage + 4}" />
	    <c:set var="totalPage" value="${(result.totalCount / result.pageSize) + (result.totalCount % result.pageSize > 0 ? 1 : 0)}" />
	
	    <!-- 이전 페이지 그룹 화살표 -->
	    <c:if test="${startPage > 1}">
	        <a href="?type=member&page=${startPage - 1}&searchKeyword=${searchKeyword}">&laquo;</a>
	    </c:if>
	
	    <!-- 페이지 번호 최대 5개 -->
	    <c:forEach var="p" begin="${startPage}" end="${endPage}">
	        <c:if test="${p <= totalPage}">
	            <a href="?type=member&page=${p}&searchKeyword=${searchKeyword}" 
	               style="${p == result.currentPage ? 'font-weight:bold;' : ''}">
	               ${p}
	            </a>
	        </c:if>
	    </c:forEach>
	
	    <!-- 다음 페이지 그룹 화살표 -->
	    <c:if test="${endPage < totalPage}">
	        <a href="?type=member&page=${endPage + 1}&searchKeyword=${searchKeyword}">&raquo;</a>
	    </c:if>
	</div>
</body>
</html>