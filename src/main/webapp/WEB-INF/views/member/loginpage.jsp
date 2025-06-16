<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
	<!-- 스타일 및 폰트 -->
	<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" /><!-- 셀렉트 라이브러리 -->
	<link href='//spoqa.github.io/spoqa-han-sans/css/SpoqaHanSansNeo.css' rel='stylesheet' type='text/css'><!-- 폰트 -->
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet" type="text/css" media="screen"><!-- 공통스타일 -->
    <link href="${contextPath}/resources/css/member/loginpage.css" rel="stylesheet" type="text/css" media="screen">
</head>
<body>
	<section class="loginpage-wrap">
		<div class="header-area">
			<a href="${contextPath}"><img src="${contextPath}/resources/icon/logo2.svg"></a>
	        <select id="country-select" name="country">
		     	<option value="kr" data-image="${contextPath}/resources/icon/south-korea_icon.png"${selectedCountry == 'kr' ? 'selected' : ''}>Korea</option>
		        <option value="jp" data-image="${contextPath}/resources/icon/japan_icon.png"${selectedCountry == 'jp' ? 'selected' : ''}>Japan</option>
		     </select>
		</div>
		<div class="loginpage-middle">
			<p>로그인</p>
		</div>
		<form action="${contextPath}/member/login.do" method="post">
			<input type="text" id="userId" name="id" placeholder="아이디" required>
			<input type="password" id="userPw" name="password" placeholder="비밀번호" required>
			<p class="error_message" style="display: none;">아이디 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요.</p>
			<button type="submit" class="login-btn">로그인</button>			
        	<div id="socialLogin">
        		<p class="separator">
  					<span>또는</span>
				</p>
				<button type="button" id="kakao-login-btn">
        			<img src="${contextPath}/resources/icon/kakao_icon.png" alt="카카오 아이콘">
        			<span>카카오로 로그인</span>
    			</button>
    			<button type="button" id="naver-login-btn">
        			<img src="${contextPath}/resources/icon/naver_icon.svg" alt="네이버 아이콘">
        			<span>네이버로 로그인</span>
    			</button>
        	</div>
	        <div class="find-join-links">
	            <a href="${contextPath}/member/findId">아이디 찾기</a>
	            <a href="${contextPath}/member/findPw">비밀번호 찾기</a>
	            <a href="${contextPath}/member/addMemberForm.do">회원가입</a>
	        </div>
    	</form>
	</section>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script><!-- 제이쿼리 -->
	<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script><!-- 셀렉트 라이브러리 -->
	<script src="https://developers.kakao.com/sdk/js/kakao.js"></script><!-- 카카오 SDK -->
	<script>
		//국가선택 로직
		$(document).ready(function() {
			// Kakao SDK 초기화
            if (typeof Kakao !== 'undefined' && !Kakao.isInitialized()) {
                Kakao.init('8da16305d90fb5864eea32886df24211');
                console.log('Kakao SDK initialized:', Kakao.isInitialized());
            }
			
			$('#country-select').select2({
				minimumResultsForSearch: -1,
				templateResult: formatState,
				templateSelection: formatState
			});

			function formatState (state) {
				if (!state.id) {
					return state.text;
				}
				var $state = $(
					'<span><img src="' + state.element.dataset.image + '" class="country-icon" style="width: 18px; height: auto; margin-right: 5px; vertical-align: middle;" /> ' + state.text + '</span>'
				);
				return $state;
			}

			// 페이지 로드 시 현재 선택된 국가에 따라 버튼 표시
			var initialCountry = $('#country-select').val();
			toggleSocialLogin(initialCountry);

			// 셀렉트 박스 변경 시
			$('#country-select').on('change', function() {
				var selectedCountry = $(this).val();

				console.log("선택된 국가:", selectedCountry);

				// 서버로 선택된 국가 정보 보내기 (세션 저장)
				$.ajax({
					url: '${contextPath}/main/selectCountry',
					type: 'POST',
					data: { nationality: selectedCountry },
					success: function(response) {
						console.log("국가 정보 세션 저장 성공!");
						// 세션 저장 성공 후, 소셜 로그인 버튼 표시 여부 업데이트
						toggleSocialLogin(selectedCountry);
					},
					error: function(xhr, status, error) {
						console.error("국가 정보 세션 저장 실패:", status, error);
						alert("국가 정보 저장에 실패했습니다.");
					}
				});
			});

			// 소셜 로그인 버튼을 보이고 숨기는 함수
			function toggleSocialLogin(country) {
				if (country === 'kr') {
					// 한국일 경우 버튼 보이기
					$('#socialLogin').show();
				} else {
					// 일본 등 다른 국가일 경우 버튼 숨기기
					$('#socialLogin').hide();
				}
			}
			
			//로그인버튼 클릭시 함수 (아이디비번이 맞으면 메인으로, 맞지않으면 에러메시지 띄워주기)
			$('form').on('submit', function(e) {
				e.preventDefault(); // 폼 기본 제출 막기
				
				$.post($(this).attr('action'), $(this).serialize(), function(result) {
			    	if (result === 'success') {
			        	location.href = '/harunichi'; // 메인페이지로 이동
			        } else {
			        	$('.error_message').show();
			        }
			       }).fail(function() {
			            alert("서버 통신 오류");
			        });
			    });
		});
		
		//카카오로로그인 버튼 클릭시
		$(document).on('click', '#kakao-login-btn', function () {
		    console.log('[카카오로 로그인] 클릭됨');

		    if (typeof Kakao === 'undefined' || !Kakao.isInitialized()) {
		        alert('Kakao SDK 초기화되지 않았습니다.');
		        return;
		    }

		    Kakao.Auth.authorize({
	            redirectUri: 'http://localhost:8090/harunichi/member/KakaoCallback.do',
	            state: 'login',
	            scope: 'profile_nickname,account_email,name,gender,birthday,birthyear,phone_number,shipping_address'
	        });
		});
		
		//네이버로로그인 버튼 클릭시
		$(document).on('click', '#naver-login-btn', function () {
			
			const clientId = 'v80rEgQ4aPt_g050ZNtj';
		    const redirectUri = 'http://localhost:8090/harunichi/member/NaverCallback.do';
		    const state = 'login'; // 로그인요청인지 회원가입요청인지 구분하기 위해서 설정함

		    const encodedRedirectUri = encodeURIComponent(redirectUri);
			const naverLoginUrl = "https://nid.naver.com/oauth2.0/authorize"
				+ "?response_type=code"
				+ "&client_id=" + clientId
				+ "&redirect_uri=" + encodedRedirectUri
				+ "&state=" + state;
		    
		    window.location.href = naverLoginUrl; 
		});
	</script>
</body>
</html>
