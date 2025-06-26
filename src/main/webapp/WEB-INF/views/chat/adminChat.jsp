<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅 관리</title>
<link href="${contextPath}/resources/css/member/adminMember.css" rel="stylesheet" type="text/css" media="screen">
</head>
<body>
	<!-- 탭 -->
	<div class="tabs">
		<a href="${contextPath}/admin/chat" class="${activeTab eq 'chat' ? 'active' : ''}" >채팅방 관리</a>
		<a href="${contextPath}/admin/chat/adminMessage" class="${activeTab eq 'report' ? 'active' : ''}" onclick="alert('⚠️ 이 기능은 아직 준비 중입니다!'); return false;">⚠️ 운영자 메세지 전송</a>
		<a href="#" class="${activeTab eq 'report' ? 'active' : ''}" onclick="alert('⚠️ 이 기능은 아직 준비 중입니다!'); return false;">⚠️ 채팅방 신고 관리</a>
		<a href="#" class="${activeTab eq 'chatLog' ? 'active' : ''}" onclick="alert('⚠️ 이 기능은 아직 준비 중입니다!'); return false;">⚠️ 채팅 로그</a>	
		<a href="#" class="${activeTab eq 'ChatPolicies' ? 'active' : ''}" onclick="alert('⚠️ 이 기능은 아직 준비 중입니다!'); return false;">⚠️ 정책 설정</a>

		
	</div>

	<!-- 검색 폼 -->
	<form class="header-search-form" action="${contextPath}/admin/chat/chatRoomSearch" method="post">
		<select name="searchType">
			<option value="all" ${searchType == 'all' ? 'selected' : ''}>전체</option>
			<option value="chatType" ${searchType == 'chatType' ? 'selected' : ''}>타입</option>
			<option value="userId" ${searchType == 'userId' ? 'selected' : ''}>유저 ID</option>
			<option value="roomId" ${searchType == 'roomId' ? 'selected' : ''}>채팅방 ID</option>	
		</select>
		<input type="text" name="searchKeyword" value="${searchKeyword}" placeholder="검색어를 입력하세요" />
		<button type="submit">
			검색
		</button>
	</form>
	
	<!-- 채팅방 목록 테이블 -->
	<form action="${contextPath}/admin/chat/saveOrDelete" method="post">
		<table>
			<thead>
				<tr>
					<th><input type="checkbox" id="checkAll" onclick="toggleAll(this)"></th>
					<th>채팅방 ID</th>
					<th>타입</th>
					<th>타이틀</th>
					<th>인원</th>
					<th>참여 유저</th>
					<th>방장</th>
					<th>삭제여부</th>
					<th>강퇴여부</th>
					<th>프로필 이미지</th>
					<th>활동일자</th>
					<th>생성일자</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="chatRoom" items="${result.list}" varStatus="loop">
					<tr>
						<td><input type="checkbox" name="selectedIds" value="${chatRoom.roomId}" /></td>
						<td>${chatRoom.roomId}<input type="hidden" value="${chatRoom.roomId}" /></td>
						<td>${chatRoom.chatType}<input type="hidden" value="${chatRoom.chatType}" /></td>
						<td>
							<c:choose>
								<c:when test="${chatRoom.chatType eq 'group'}">
									<input type="text" value="${chatRoom.title}" />
								</c:when>
								<c:otherwise>
									<input type="text" value="${chatRoom.title}" disabled />
								</c:otherwise>
							</c:choose>					
						</td>
						<td>${chatRoom.userCount} / ${chatRoom.persons}<input type="hidden" value="${chatRoom.persons}" /></td>
						<td>${chatRoom.userId}<input type="hidden" value="${chatRoom.userId}" /></td>
						<td>${chatRoom.leader eq 1 ? '방장' : ''}<input type="hidden" value="${chatRoom.leader}" /></td>
						<td>${chatRoom.deleted ? 'Y' : ''}<input type="hidden" value="${chatRoom.deleted}" /></td>
						<td>${chatRoom.kicked ? 'Y' : ''}<input type="hidden" value="${chatRoom.kicked}" /></td>
						<td>
							<div class="profile-cell">
								<div class="table-img">
									<c:choose>
										<c:when test="${not empty chatRoom.profileImg}">
											<img id="profileImg_${chatRoom.roomId}" src="${contextPath}/images/chat/${chatRoom.profileImg}" alt="프로필 이미지" />
										</c:when>
										<c:otherwise>
											<img id="profileImg_${chatRoom.roomId}" src="${contextPath}/resources/icon/basic_profile.jpg" alt="기본 프로필" />
							        	</c:otherwise>
									</c:choose>
								</div>
								<button type="button" onclick="resetProfileImg('${chatRoom.roomId}')">초기화</button>
								<input type="hidden" value="${chatRoom.profileImg}" id="profileImgInput_${chatRoom.roomId}" />
							</div>
						</td>
						<td><fmt:formatDate value="${chatRoom.lastMessageTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td><fmt:formatDate value="${chatRoom.admissionTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					</tr>
				</c:forEach>
				<c:if test="${empty result.list}">
					<tr>
						<td colspan="16">검색 결과가 없습니다.</td>
					</tr>
				</c:if>
			</tbody>
		</table>
		<div class="submit-btn-class">
			<button type="submit" name="action" value="update" onclick="return confirm('변경한 사항을 저장하시겠습니까?');">저장</button>
			<button type="submit" name="action" value="delete" onclick="return confirm('선택한 채팅방을 삭제하시겠습니까?');">삭제</button>
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
	
		const contextPath = "${contextPath}";
		
		function toggleAll(source) {
		    const checkboxes = document.querySelectorAll('input[name="selectedIds"]');
		    for (const checkbox of checkboxes) {
		        checkbox.checked = source.checked;
		    }
		}

		function resetProfileImg(roomId) {
			const imgTag = document.getElementById("profileImg_" + roomId);
		    const inputTag = document.getElementById("profileImgInput_" + roomId);
		
		    imgTag.src = "${contextPath}/resources/icon/basic_profile.jpg";
		
		    inputTag.value = "";
		}

	</script>
</body>
</html>
