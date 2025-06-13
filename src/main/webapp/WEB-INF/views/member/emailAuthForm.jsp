<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>이메일 인증</title>
	<!-- 스타일 및 폰트 -->
	<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" /><!-- 셀렉트 라이브러리 -->
	<link href='//spoqa.github.io/spoqa-han-sans/css/SpoqaHanSansNeo.css' rel='stylesheet' type='text/css'><!-- 폰트 -->
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet" type="text/css" media="screen"><!-- 공통스타일 -->
	<link href="${contextPath}/resources/css/member/emailAuthForm.css" rel="stylesheet" type="text/css" media="screen">
</head>
<body>
	<jsp:include page="../common/lightHeader.jsp" />
	<!-- 선택한 국가 정보 저장용 -->
	<input type="hidden" id="userNationality" value="">
	
	<section class="email-auth-wrap">
	
		<div class="email-auth-middle">
			<h2>이메일 인증</h2>
			<p>이메일 주소를 입력하고 인증을 진행해주세요.</p>
			<img src="${contextPath}/resources/icon/sky_email_icon.png" />
		</div>
		
		<form id="emailAuthForm" action="#" method="post" novalidate>
			<div class="form-group">
				<input type="email" id="email" name="email" placeholder="이메일 주소를 입력해주세요." required>
			</div>

			<button type="button" id="sendAuthCodeBtn">인증번호 발송</button>

			<div class="form-group" id="authCodeGroup" style="display: none;">
				<input type="text" id="authCode" name="authCode" placeholder="인증번호를 입력해주세요." required>
			</div>
			<div class="buttons">
				<button type="button" id="verifyAuthCodeBtn" style="display: none;">인증번호 확인</button>
				<button class="resend" type="button" id="resendAuthCodeBtn" style="display: none;">인증번호 재발송</button>
			</div>
		</form>
		
		<!-- 스피너 표시 -->
		<div id="loading-spinner" style="display: none; text-align: center; margin-top: 15px;">
  			<img src="${contextPath}/resources/icon/loading-circle2.gif" alt="로딩 중..." style="width: 32px;" />
  			<p style="color: #888;">잠시만 기다려주세요...</p>
		</div>
		
	</section>
	

	<!-- 스크립트 -->
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
	<script>
	$(document).ready(function() {
		
		// Select2 국가 이미지 표시
		function formatState(state) {
			if (!state.id) return state.text;
			return $(
				'<span><img src="' + state.element.dataset.image + '" class="country-icon" style="width: 18px; height: auto; margin-right: 5px; vertical-align: middle;" /> ' + state.text + '</span>'
			);
		}

		// 국가 선택 초기화
		$('#country-select').select2({
			minimumResultsForSearch: -1,
			templateResult: formatState,
			templateSelection: formatState
		}).on('change', function() {
			const selectedCountry = $(this).val();
			console.log("선택된 국가:", selectedCountry);

			// AJAX로 세션 저장
			$.ajax({
				url: '${contextPath}/main/selectCountry',
				type: 'POST',
				data: { nationality: selectedCountry },
				success: function() {
					console.log("국가 정보 세션 저장 성공!");
					toggleSocialLogin(selectedCountry);
				},
				error: function(xhr, status, error) {
					console.error("국가 정보 세션 저장 실패:", status, error);
					alert("국가 정보 저장에 실패했습니다.");
				}
			});
		});

		// URL 파라미터에서 국가 정보 추출
		const urlParams = new URLSearchParams(window.location.search);
		const nationality = urlParams.get('nationality');
		$('#userNationality').val(nationality);

		// 인증번호 발송 버튼
		$('#sendAuthCodeBtn').on('click', function() {
			const email = $('#email').val();
			const userNationality = $('#userNationality').val();

			if (email === "") {
				alert("이메일 주소를 입력해주세요!");
				return;
			}

			const $authButton = $(this);
			$authButton.hide();
			
			
			$('#loading-spinner').show(); //보내는중 스피너 표시
			
			$.ajax({
				url: '${contextPath}/mail/sendAuthEmail.do',
				type: 'POST',
				data: { email: email, nationality: userNationality },
				success: function(response) {
					
					$('#loading-spinner').hide();//스피너 제거
					
					if (response === "success") {
						alert("인증 메일이 발송되었습니다. 메일함을 확인해주세요.");
						$('#authCodeGroup').show();
						$('#verifyAuthCodeBtn').show();
						$('#resendAuthCodeBtn').show();
					} else {
						alert("메일 발송에 실패했습니다. 다시 시도해주세요.");
						$authButton.show();
					}
				},
				error: function(xhr, status, error) {
					
					$('#loading-spinner').hide();//스피너 제거
					
					console.error("메일 발송 중 오류 발생:", error);
					alert("메일 발송 중 오류가 발생했습니다.");
					$authButton.show();
				}
			});
		});

		// 인증번호 재발송, 인증확인 버튼 나타났다 사라졌다
		$('#resendAuthCodeBtn').on('click', function() {
			const email = $('#email').val();
			const $resendButton = $(this);
			const $verifyButton = $('#verifyAuthCodeBtn');

			if (email === "") {
				alert("이메일 주소를 입력해주세요!");
				return;
			}

			// 두 버튼 다 숨기기
			$resendButton.hide();
			$verifyButton.hide();
			
			$('#loading-spinner').show(); //보내는중 스피너 표시
			
			$.ajax({
				url: '${contextPath}/mail/sendAuthEmail.do',
				type: 'POST',
				data: { email: email, nationality: $('#userNationality').val() },
				success: function(response) {
					
					$('#loading-spinner').hide();//스피너 제거
					
					if (response === "success") {
						alert("인증 메일이 재발송되었습니다. 메일함을 확인해주세요.");
						// 두 버튼 다시 보이기
						$resendButton.show();
						$verifyButton.show();
					} else {
						alert("메일 재발송에 실패했습니다. 다시 시도해주세요.");
						// 두 버튼 다시 보이기
						$resendButton.show();
						$verifyButton.show();
					}
				},
				error: function(xhr, status, error) {
					
					$('#loading-spinner').hide();//스피너 제거
					
					console.error("메일 재발송 중 오류 발생:", error);
					alert("메일 재발송 중 오류가 발생했습니다.");
					$resendButton.show();
					$verifyButton.show();
				}
			});
		});

		// 인증번호 확인 버튼
		$('#verifyAuthCodeBtn').on('click', function() {
			const email = $('#email').val();
			const authCode = $('#authCode').val();

			if (authCode === "") {
				alert("인증번호를 입력해주세요!");
				return;
			}

			$.ajax({
				url: '${contextPath}/mail/verifyAuthCode.do',
				type: 'POST',
				data: { email: email, authCode: authCode },
				success: function(response) {
					if (response === "success") {
						alert("이메일 인증이 완료되었습니다!");
						$('#email').prop('disabled', true);
						$('#sendAuthCodeBtn').hide();
						$('#authCodeGroup').hide();
						$('#verifyAuthCodeBtn').hide();

						// 다음 페이지로 이동
						window.location.href = "${contextPath}/member/addMemberWriteForm.do";
					} else {
						alert("인증번호가 일치하지 않습니다. 다시 확인해주세요.");
						$('#authCode').val('').focus();
					}
				},
				error: function(xhr, status, error) {
					console.error("인증번호 확인 중 오류 발생:", error);
					alert("인증번호 확인 중 오류가 발생했습니다.");
				}
			});
		});
	});
	</script>
</body>
</html>
