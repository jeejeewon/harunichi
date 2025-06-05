<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<script src="http://code.jquery.com/jquery-latest.min.js"></script>

<h1>게시글 등록</h1>
<form action="${contextPath}/board/post" method="post"
	enctype="multipart/form-data">

	<table>
		<tr>
			<td>작성자:</td>
			<td><input type="text" name="boardWriter"></td>
		</tr>
		<tr>
			<td>내용:</td>
			<td><textarea name="boardCont"></textarea></td>
		</tr>
		<tr>
			<td>첨부 이미지:</td>
			<%-- 설명을 변경했습니다 --%>
			<td><input type="file" name="imageFiles" accept="image/*"
				multiple></td>
			<%-- multiple 속성 추가 --%>
		</tr>
	</table>
	<input type="submit" value="등록">
</form>