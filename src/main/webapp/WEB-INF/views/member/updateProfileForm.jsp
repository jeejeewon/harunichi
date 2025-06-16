<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보 수정</title>
<link href="${contextPath}/resources/css/addMemberWriteForm.css" rel="stylesheet" type="text/css" />
<link href="${contextPath}/resources/css/profileImgAndMyLikeSetting.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="member-info-modification-wrap">
    <h2>회원정보 수정</h2>
    <form action="${contextPath}/member/updateMemberProcess.do" method="post" enctype="multipart/form-data">
        <!-- 프로필 이미지 설정 -->
        <div>
			<c:if test="${empty profileImgPath}">
			  	<img id="profileImage" src="${contextPath}/resources/icon/basic_profile.jpg" alt="기본 프로필 이미지">
			</c:if>
			<c:if test="${not empty profileImgPath}">
			  	<img id="profileImage" src="${profileImgPath}" alt="선택한 프로필 이미지">
			</c:if>
			<div class="profile-img-upload">
			    <label for="profileImg" class="custom-file-label">프로필 이미지 선택</label>
			    <input type="file" id="profileImg" name="profileImg" accept="image/*" onchange="previewImage(this)"><!-- 이미지 파일만 첨부할수있게 설정 -->
			</div>
        </div>
        
        			<input type="hidden" id="userNationality" value="${sessionScope.memberVo.contry}" /><!-- 주소api를 위해 국가 정보를 가져오는 히든인풋 -->
			<div class="member-form-inner">
				<p>*필수입력</p>
				<div id="required-form">
					<div class="form-group">
							<input type="text" name="id" id="id" placeholder="아이디(영문, 숫자 조합)" oninput="this.value = this.value.replace(/[^a-zA-Z0-9]/g, '');">
							<div id="idCheckText">
								<span class="error" id="error-id"></span>
								<span class="error" id="id-check-result"></span>
								<span class="info" id="id-check-required">아이디 중복확인을 해주세요</span>
							</div>
							<button type="button" id="checkIdBtn">중복확인</button>
					</div>
					<div class="form-group">
						<input type="password" name="pass" id="pass" placeholder="비밀번호(6자 이상)" >
						<span class="error" id="error-pass"></span>
					</div>
					
					<div class="form-group">
						<input type="password" id="passConfirm" placeholder="비밀번호 확인" >
						<span class="password-match-icon" id="password-match-icon"></span>
						<span class="error" id="error-passConfirm"></span>
					</div>
			
					<div class="form-group">
						<input type="text" name="name" id="name" placeholder="이름" >
						<span class="error" id="error-name"></span>
					</div>
			
					<div class="form-group">
						<input type="text" name="nick" id="nick" placeholder="닉네임">
						<span class="error" id="error-nick"></span>
					</div>
			
					<div class="form-group">
						<input type="text" name="year" id="year" maxlength="8" placeholder="생년월일 8자리">
						<span class="error" id="error-year"></span>
					</div>
				</div>
			</div>
			<hr>
			<div class="member-form-inner">
				<p>선택입력</p>
				<div id="other-form">
					<div class="form-group gender-group">
					    <input type="radio" id="male" name="gender" value="male">
					    <label for="male">남성</label>
					
					    <input type="radio" id="female" name="gender" value="female">
					    <label for="female">여성</label>
					
					    <input type="radio" id="none" name="gender" value="">
					    <label for="none">선택안함</label>
					</div>
					
					<div class="form-group">
						<input type="text" name="tel" placeholder="전화번호 ('-'를 제외한 숫자)" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*?)\..*/g, '$1');" />
						<span class="error" id="error-tel"></span>
					</div>
			
					<!-- 주소 입력 필드 (읽기 전용) -->
					<div class="form-group">
					    <input type="text" id="address" name="address" placeholder="주소 (주소찾기 버튼으로 설정)" readonly tabindex="-1" onfocus="this.blur();">
					    <button type="button" id="searchAddressBtn">주소 찾기</button>
					    <span class="error" id="error-address"></span>
					</div>
					<!-- 상세주소 입력란 (초기에는 비활성화) -->
					<div class="form-group">
						<input type="text" id="detailAddress" name="detailAddress" placeholder="상세 주소" disabled>
					</div>
				</div>
			</div>
			<div class="form-group">
				<button type="submit" id="nextBtn" disabled>수정완료</button>
			</div>
			
			
    </form>
</div>
</body>
</html>
