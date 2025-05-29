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
		<div class="logo-area">
			<a href="${contextPath}"><img src="${contextPath}/resources/image/logo2.svg"></a>
		</div>
		<div class="addmemberform-middle">
			<img src="${contextPath}/resources/image/party-icon.png">
			<p>회원가입을 환영합니다!</p>
		</div>
		<!-- 국가를 선택하는 select가 있는 영역 -->
	    <div class="select-country-area">
	        <label for="nationality-select"></label>
	        <select id="nationality-select">
	        	<option value="">국적을 선택해 주세요</option>
	            <option value="KR" data-image="${contextPath}/resources/image/south-korea_icon.png">대한민국</option>
	            <option value="JP" data-image="${contextPath}/resources/image/japan_icon.png">일본</option>
	        </select>
	    </div>
	    <!-- 선택된 국적에 따른 회원가입 폼이 로딩될 영역 -->
	    <div id="registration-form-area" class="register-form-area">
	        <!-- Ajax로 로딩될 폼 내용이 들어갈 부분 -->
	    </div>
	    
	    <div class="login-ask-area">
	    	<p>이미 계정이 있으신가요?</p>
	   		<a href="#">로그인</a>
	    </div>
		</section>
	
	
	
	
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script><!-- 제이쿼리 -->
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script><!-- 셀렉트 라이브러리 -->
    <script>
        $(document).ready(function() {
            // Select2 초기화
            $('#nationality-select').select2({
                minimumResultsForSearch: -1, // 검색 기능 비활성화
                templateResult: formatState, // 결과 포맷 함수
                templateSelection: formatState // 선택된 항목 포맷 함수
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

            // 국가 선택 드롭다운 변경 이벤트 리스너
            $('#nationality-select').on('change', function() {
                var selectedNationality = $(this).val(); // 선택된 국적 값 (KR 또는 JP)

                if (selectedNationality) { // 국적이 선택되었을 때만 Ajax 요청
                    // Ajax 요청 보내기
                    $.ajax({
                        url: '<c:url value="/member/getRegistrationForm"/>', // 폼 내용을 가져올 서버 URL (이 URL은 나중에 서버에서 구현해야 해!)
                        type: 'GET', // 또는 POST
                        data: { nationality: selectedNationality }, // 선택된 국적 데이터를 서버로 전송
                        success: function(response) {
                            // 서버로부터 받은 HTML 응답을 영역에 넣기
                            $('#registration-form-area').html(response);
                        },
                        error: function(xhr, status, error) {
                            console.error("폼 로딩 중 오류 발생:", error);
                            $('#registration-form-area').html("<p>폼을 불러오는데 실패했습니다. 다시 시도해주세요.</p>");
                        }
                    });
                } else {
                    // 국적 선택이 초기화되면 폼 영역 비우기
                    $('#registration-form-area').empty();
                }
            });
        });
    </script>
</body>
</html>
