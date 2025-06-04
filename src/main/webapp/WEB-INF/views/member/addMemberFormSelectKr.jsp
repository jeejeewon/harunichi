<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<%-- 한국인 회원가입 폼 --%>
<section class="register-options">
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
	// 이메일 인증 버튼 클릭시
	$(document).on('click', '#email-auth-btn', function () {
		const selectedNationality = $('#nationality-select').val(); // 국적 셀렉트박스가 상단에 있다면
		window.location.href = '<c:url value="/member/emailAuthForm.do"/>?nationality=' + selectedNationality;
	});

	// 이벤트 위임 방식으로 클릭 이벤트 연결 (동적 요소도 동작하게 하기 위해)
    $(document).on('click', '#kakao-register-btn', function () {
        console.log('[카카오로 회원가입] 클릭됨');
        
        if (typeof Kakao === 'undefined' || !Kakao.isInitialized()) {
            alert('Kakao SDK 초기화되지 않았습니다.');
            return;
        }

        Kakao.Auth.authorize({
            redirectUri: 'http://localhost:8090/harunichi/member/KakaoCallback.do',
            scope: 'profile_nickname,account_email,name,gender,birthday,birthyear,phone_number,shipping_address'
        });
    });

	// 네이버 클릭 테스트용
    $(document).on('click', '#naver-register-btn', function () {
        alert('네이버 로그인 기능은 아직 구현되지 않았습니다.');
    });
</script>
