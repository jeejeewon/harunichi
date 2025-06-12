<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
    <!-- Select2 CSS (국가 선택 드롭다운에 Select2 사용한다면 필요) -->
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" /><!-- 셀렉트 라이브러리 -->
    <link href='//spoqa.github.io/spoqa-han-sans/css/SpoqaHanSansNeo.css' rel='stylesheet' type='text/css'><!-- 폰트 -->
    <link href="${contextPath}/resources/css/member/addMemberForm.css" rel="stylesheet" type="text/css" media="screen">
</head>
<body>
	<section class="addmemberform-wrap">
		<div class="header-area">
			<a href="${contextPath}"><img src="${contextPath}/resources/icon/logo2.svg"></a>
	        <select id="country-select" name="country">
		     	<option value="kr" data-image="${contextPath}/resources/icon/south-korea_icon.png"${selectedCountry == 'kr' ? 'selected' : ''}>Korea</option>
		        <option value="jp" data-image="${contextPath}/resources/icon/japan_icon.png"${selectedCountry == 'jp' ? 'selected' : ''}>Japan</option>
		     </select>
		</div>
		<div class="addmemberform-middle">
			<img src="${contextPath}/resources/icon/party-icon.png">
			<p>회원가입을 환영합니다!</p>
		</div>
		<!-- 국가를 선택하는 select가 있는 영역 -->
	    <div class="select-country-area">
	        <label for="nationality-select"></label>
	        <select id="nationality-select">
	        	<option value="">국적을 선택해 주세요</option>
	            <option value="kr" data-image="${contextPath}/resources/icon/south-korea_icon.png">대한민국</option>
	            <option value="jp" data-image="${contextPath}/resources/icon/japan_icon.png">일본</option>
	        </select>
	    </div>
	    <!-- 선택된 국적에 따른 회원가입 폼이 로딩될 영역 -->
	    <div id="registration-form-area" class="register-form-area">
	        <!-- Ajax로 로딩될 폼 내용이 들어갈 부분 -->
	    </div>
	    
	    <div class="login-ask-area">
	    	<p>이미 계정이 있으신가요?</p>
	   		<a href="${contextPath}/member/loginpage.do">로그인</a>
	    </div>
		</section>
 <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script src="https://developers.kakao.com/sdk/js/kakao.min.js"></script>
    <script>
        $(document).ready(function() {
            // Kakao SDK 초기화
            if (typeof Kakao !== 'undefined' && !Kakao.isInitialized()) {
                Kakao.init('8da16305d90fb5864eea32886df24211');
                console.log('Kakao SDK initialized:', Kakao.isInitialized());
            }

            // Select2 공통 formatState 함수
            function formatState(state) {
                if (!state.id) return state.text;
                return $('<span><img src="' + state.element.dataset.image + '" class="country-icon" style="width: 18px; height: auto; margin-right: 5px; vertical-align: middle;" /> ' + state.text + '</span>');
            }

            // 상단(헤더) 국가 선택 Select2 초기화 및 이벤트 핸들러
            $('#country-select').select2({
                minimumResultsForSearch: -1,
                templateResult: formatState,
                templateSelection: formatState
            }).on('change', function() {
                var selectedCountry = $(this).val();
                console.log("선택된 국가:", selectedCountry);

                // 서버로 선택된 국가 정보 보내기 (세션 저장)
                $.ajax({
                    url: '${contextPath}/main/selectCountry',
                    type: 'POST',
                    data: { nationality: selectedCountry },
                    success: function(response) {
                        console.log("국가 정보 세션 저장 성공!");
                        toggleSocialLogin(selectedCountry); // 세션 저장 성공 후, 소셜 로그인 버튼 표시
                    },
                    error: function(xhr, status, error) {
                        console.error("국가 정보 세션 저장 실패:", status, error);
                        alert("국가 정보 저장에 실패했습니다.");
                    }
                });
            });

            // 페이지 로드 시 현재 선택된 국가에 따라 버튼 표시
            var initialCountry = $('#country-select').val();
            toggleSocialLogin(initialCountry);

            // (하단) 회원가입폼 Select2 초기화 및 이벤트 핸들러
            $('#nationality-select').select2({
                minimumResultsForSearch: -1,
                templateResult: formatState,
                templateSelection: formatState
            }).on('change', function() {
                var selectedNationality = $(this).val();

                if (selectedNationality) {
                    $.ajax({
                        url: '<c:url value="/member/getRegistrationForm"/>',
                        type: 'GET',
                        data: { nationality: selectedNationality },
                        success: function(response) {
                            $('#registration-form-area').html(response);
                        },
                        error: function(xhr, status, error) {
                            console.error("폼 로딩 중 오류 발생:", error);
                            $('#registration-form-area').html("<p>폼을 불러오는데 실패했습니다. 다시 시도해주세요.</p>");
                        }
                    });
                } else {
                    $('#registration-form-area').empty();
                }
            });
        });

        // toggleSocialLogin 함수
        function toggleSocialLogin(countryCode) {
            // 소셜 로그인 버튼 표시 로직
            if (countryCode === 'kr') {
                // 한국
            } else if (countryCode === 'jp') {
                // 일본
            }
        }
    </script>

    
</body>
</html>
