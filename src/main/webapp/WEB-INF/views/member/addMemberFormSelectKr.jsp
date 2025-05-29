<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<%-- 한국인 회원가입 폼 --%>

<section class="kr-register-options">
    <%-- 이메일 인증 버튼 --%>
    <button type="button" id="email-auth-btn">
		<img src="${contextPath}/resources/image/email_icon.svg" alt="이메일 아이콘">
        <span>이메일로 인증받기</span>
    </button>

    <%-- 카카오로 가입 버튼 --%>
    <button type="button" id="kakao-register-btn">
        <img src="${contextPath}/resources/image/kakao_icon.png" alt="카카오 아이콘">
        <span>카카오로 회원가입</span>
    </button>

    <%-- 네이버로 가입 버튼 --%>
    <button type="button" id="naver-register-btn">
        <img src="${contextPath}/resources/image/naver_icon.svg" alt="네이버 아이콘">
        <span>네이버아이디로 가입</span>
    </button>
    
</section>



<script>
    $(document).ready(function() {
        // [이메일로 인증받기] 버튼 클릭 이벤트
        $('#email-auth-btn').on('click', function() {
            // TODO: 이 버튼 클릭 시 이메일 인증 페이지(emailAuthForm.jsp)로 이동하는 로직 추가
            alert('이메일 인증 페이지로 이동!'); // 임시 알림
            window.location.href = '<c:url value="/harunichi/member/emailAuthForm.do"/>'; // 예시 URL, 나중에 컨트롤러와 타일즈 설정 필요
        });

        // [카카오로 회원가입] 버튼 클릭 이벤트 (나중에 구현)
        $('#kakao-register-btn').on('click', function() {
            alert('카카오 로그인/가입 로직 시작!');
            // TODO: 카카오 API 연동 로직
        });

        // [네이버아이디로 가입] 버튼 클릭 이벤트 (나중에 구현)
        $('#naver-register-btn').on('click', function() {
            alert('네이버 로그인/가입 로직 시작!');
            // TODO: 네이버 API 연동 로직
        });
    });
</script>
