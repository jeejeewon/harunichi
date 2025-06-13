<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보 입력</title>
	<!-- 스타일 및 폰트 -->
	<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" /><!-- 셀렉트 라이브러리 -->
	<link href='//spoqa.github.io/spoqa-han-sans/css/SpoqaHanSansNeo.css' rel='stylesheet' type='text/css'><!-- 폰트 -->
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet" type="text/css" media="screen"><!-- 공통스타일 -->
<style>
.error {
	color: red;
	font-size: 0.9em;
}
</style>
</head>
<body>
	<h1>회원가입 폼</h1>

	<form id="memberForm" action="addMemberProcess.do" method="post">
		<div class="form-group">
			<label for="id">아이디:</label>
			<div style="display: flex; gap: 10px;">
				<input type="text" name="id" id="id" oninput="this.value = this.value.replace(/[^a-zA-Z0-9]/g, '');">
				<button type="button" id="checkIdBtn">중복확인</button>
			</div>
			<span class="error" id="error-id"></span>
			<span class="error" id="id-check-result" style="font-weight: bold;"></span>
		</div>

		<div class="form-group">
			<label for="pass">비밀번호:</label>
			<input type="password" name="pass" id="pass">
			<span class="error" id="error-pass"></span>
		</div>
		
		<div class="form-group">
			<label for="passConfirm">비밀번호 확인:<span>*</span></label>
			<input type="password" id="passConfirm">
			<span class="password-match-icon" id="password-match-icon"></span>
			<span class="error" id="error-passConfirm"></span>
		</div>

		<div class="form-group">
			<label for="name">이름:<span>*</span></label>
			<input type="text" name="name" id="name">
			<span class="error" id="error-name"></span>
		</div>

		<div class="form-group">
			<label for="nick">닉네임:<span>*</span></label>
			<input type="text" name="nick" id="nick">
			<span class="error" id="error-nick"></span>
		</div>

		<div class="form-group">
			<label for="year">생년월일:<span>*</span></label>
			<input type="date" name="year" id="year">
			<span class="error" id="error-year"></span>
		</div>
		
		<div class="form-group">
			<label for="gender">성별</label> <select name="gender" id="gender">
				<option value="">선택 안함</option>
				<option value="male">남성</option>
				<option value="female">여성</option>
			</select>
		</div>
		
		<div class="form-group">
			<label for="tel">전화번호</label>
			<input type="text" name="tel" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*?)\..*/g, '$1');" />
			<span class="error" id="error-tel"></span>
		</div>

		<div class="form-group">
			<label for="address">주소:</label>
			<input type="text" id="address" name="address" placeholder="예: 서울특별시 강남구 역삼동 123-45">
			<span class="error" id="error-address"></span>
		</div>

		<div class="form-group">
			<button type="submit" id="nextBtn" disabled>다음</button>
		</div>
	</form>
	
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script>
	$(document).ready(function() {
        // 아이디 중복 확인 상태를 저장하는 변수
        let checkIdConfirmed = false;

        // 필수 입력 필드 목록
        const $requiredFields = $('#id, #pass, #passConfirm, #name, #nick, #year');

        // '다음' 버튼 jQuery 객체
        const $nextBtn = $('#nextBtn');
        
     	// 비밀번호 및 비밀번호 확인 필드 jQuery 객체
        const $passInput = $('#pass');
        const $passConfirmInput = $('#passConfirm');
        // 비밀번호 일치 아이콘 span
        const $passwordMatchIcon = $('#password-match-icon');

        // 페이지 로드 시 '다음' 버튼 비활성화 상태로 시작
        $nextBtn.prop('disabled', true);

        // 모든 필수 필드가 채워졌는지 확인하는 함수
        function areRequiredFieldsFilled() {
            let allFilled = true;
            $requiredFields.each(function() {
                if ($(this).val().trim() === "") {
                    allFilled = false;
                    return false; // each 루프 중단
                }
            });
            return allFilled;
        }

     	// 비밀번호 일치 여부를 확인하고 아이콘을 업데이트하는 함수
        function updatePasswordMatchIcon() {
            const pass = $passInput.val();
            const passConfirm = $passConfirmInput.val();
            const $errorSpan = $('#error-passConfirm'); // 에러 메시지 span

            // 에러 메시지 초기화
            $errorSpan.text('');
            $passwordMatchIcon.text(''); // 아이콘 초기화
            $passwordMatchIcon.removeClass('match mismatch'); // 클래스 초기화

            if (passConfirm.trim() === "") {
                // 비밀번호 확인 필드가 비어있으면 아무것도 표시 안 함
                return false; // 일치하지 않음
            } else if (pass === passConfirm) {
                // 비밀번호가 일치하면 체크 표시
                $passwordMatchIcon.text('✅'); // 체크 이모지 또는 '✓'
                $passwordMatchIcon.addClass('match');
                return true; // 일치함
            } else {
                // 비밀번호가 일치하지 않으면 엑스 표시 또는 에러 메시지
                $passwordMatchIcon.text('❌'); // 엑스 이모지 또는 '✗'
                $passwordMatchIcon.addClass('mismatch');
                // $errorSpan.text('비밀번호가 일치하지 않습니다.'); // 제출 시점에만 에러 메시지 띄워도 됨
                return false; // 일치하지 않음
            }
        }
        
        // 폼 유효성 (필수 필드 + 아이디 중복 확인) 상태에 따라 '다음' 버튼 활성화/비활성화
        function toggleNextButton() {
            // 필수 필드와 아이디 중복 확인만으로 버튼 활성화/비활성화
             if (areRequiredFieldsFilled() && checkIdConfirmed && updatePasswordMatchIcon()) { // ✅ updatePasswordMatchIcon() 호출 결과 사용!
                    $nextBtn.prop('disabled', false); // 모두 충족하면 활성화
                } else {
                    $nextBtn.prop('disabled', true); // 아니면 비활성화
                }
        }

        // 🌟🌟🌟 필수 입력 필드 값이 변경될 때마다 버튼 상태 업데이트 🌟🌟🌟
        // 비밀번호 및 비밀번호 확인 필드도 포함!
        $requiredFields.on('input change', function() {
             // 비밀번호/확인 필드 변경 시 아이콘도 업데이트
             if ($(this).attr('id') === 'pass' || $(this).attr('id') === 'passConfirm') {
                 updatePasswordMatchIcon();
             }
             toggleNextButton(); // 버튼 상태 업데이트
        });

         // 🌟🌟🌟 비밀번호 확인 필드에서 벗어났을 때 (blur 이벤트) 비밀번호 일치 여부 실시간 체크 (선택 사항) 🌟🌟🌟
         // input/change 이벤트로 아이콘은 이미 업데이트 되지만, blur 시 에러 메시지 띄우는 용도로 사용 가능
         $('#passConfirm').on('blur', function() {
             const pass = $('#pass').val();
             const passConfirm = $(this).val();
             const $errorSpan = $('#error-passConfirm');

             if (passConfirm.trim() === "") {
                 $errorSpan.text('비밀번호 확인을 입력해주세요.');
             } else if (pass !== passConfirm) {
                 $errorSpan.text('비밀번호가 일치하지 않습니다.');
             } else {
                 $errorSpan.text(''); // 일치하면 에러 메시지 지움
             }
              // blur 이벤트 후에도 버튼 상태는 input/change 이벤트 핸들러에 의해 업데이트 됨.
         });


        // 아이디 중복 확인 로직 (기존 코드와 동일, 버튼 상태 업데이트 포함)
        $('#checkIdBtn').on('click', function() {
            const id = $('#id').val().trim();
            const $resultSpan = $('#id-check-result');
            const $errorSpan = $('#error-id');

            // 기존 에러/결과 메시지 초기화
            $('.error').text('');
            $resultSpan.text('');

            if (id === "") {
                $resultSpan.css('color', 'red').text('아이디를 입력해주세요.');
                checkIdConfirmed = false;
                toggleNextButton();
                return;
            }

            $.ajax({
                url: '${contextPath}/member/checkId.do', // 아이디 중복 확인 컨트롤러 URL 확인!
                type: 'GET',
                data: { id: id },
                dataType: 'json',
                success: function(data) {
                    if (data.exists) {
                        $resultSpan.css('color', 'red').text('이미 사용 중인 아이디입니다.');
                        checkIdConfirmed = false;
                    } else {
                        $resultSpan.css('color', 'green').text('사용 가능한 아이디입니다!');
                        checkIdConfirmed = true;
                    }
                    toggleNextButton(); // 상태 변경 후 버튼 상태 업데이트
                },
                error: function(xhr, status, error) {
                    console.error("오류:", error);
                    $resultSpan.css('color', 'red').text('아이디 중복 확인 중 오류 발생.');
                    checkIdConfirmed = false;
                    toggleNextButton(); // 상태 변경 후 버튼 상태 업데이트
                }
            });
        });

         // 폼 제출 (AJAX) 로직 
        $('#memberForm').on('submit', function(event) {
            event.preventDefault(); // 폼의 기본 제출 동작을 막음

            // 에러 메시지 초기화
            $('.error').text('');

            let formIsValid = true; // 최종 폼 유효성 상태

            // 1. 최종 필수 필드 체크 (버튼 활성화 상태와는 별개로 다시 체크)
             if (!areRequiredFieldsFilled()) {
                 // 이 경우는 버튼이 비활성화 되어있었겠지만, 혹시 모를 상황에 대비
                  alert("필수 정보를 모두 입력해주세요!");
                  formIsValid = false;
             }

          	//2. 아이디 중복 확인 체크
             const idInput = $('#id');
             const id = idInput.val().trim();
             const idErrorSpan = $('#error-id');
             const idResultSpan = $('#id-check-result'); // 중복확인 결과 span

              // 아이디 중복 확인 미완료 체크 (패턴 검사 통과 후에 하는게 좋겠지?)
              if (formIsValid && !checkIdConfirmed) { // 패턴도 맞고, 중복 확인도 안 했으면
                  idResultSpan.css('color', 'red').text('아이디 중복 확인이 필요합니다.');
                  alert("아이디 중복 확인을 완료해주세요.");
                  formIsValid = false;
              }
             // 🌟🌟🌟 3. 비밀번호 일치 최종 확인 및 형식 검사
             const passInput = $('#pass');
                const passConfirmInput = $('#passConfirm');
                const pass = passInput.val().trim();
                const passConfirm = passConfirmInput.val().trim();
                const passErrorSpan = $('#error-pass');
                const passConfirmErrorSpan = $('#error-passConfirm');

                // 비밀번호 길이 검사
                if (pass.length < 6) {
                    passErrorSpan.text('비밀번호는 6자 이상이어야 합니다.');
                    formIsValid = false;
                } else {
                    passErrorSpan.text(''); // 길이 맞으면 에러 메시지 지움
                }
                 // TODO: 비밀번호 복잡성 검사 (특수문자, 숫자 포함 등) 여기 추가

                // 비밀번호 확인 필드 비어있는지 체크
                 if (passConfirm === "") {
                     passConfirmErrorSpan.text('비밀번호 확인을 입력해주세요.');
                     formIsValid = false;
                 }
                 // 비밀번호와 비밀번호 확인 일치 여부 체크
                 else if (pass !== passConfirm) {
                     passConfirmErrorSpan.text('비밀번호가 일치하지 않습니다.');
                     formIsValid = false;
                 } else {
                     passConfirmErrorSpan.text(''); // 일치하면 에러 메시지 지움
                 }


            // 4. 다른 추가적인 유효성 검사 (형식 검사 등) - 필요하다면 여기에 추가!
            // TODO: 닉네임 중복 확인 로직 (AJAX 호출)
            // TODO: 전화번호 형식 검사 (정규식)
            // TODO: 생년월일 유효성 검사 등


            // 최종 유효성 검사 통과 시 AJAX 제출
            if (formIsValid) {
                // 폼 데이터 수집 (세션 memberVo에 이미 있는 contry, email 제외)
                // 비밀번호 확인 필드(passConfirm)는 name 속성이 없으므로 serialize()에 포함되지 않음.
                // 서버로는 'pass' 값 하나만 넘어가게 됨.
                const formData = $(this).serialize();

                // AJAX 요청을 서버의 addMemberProcess.do 컨트롤러로 보냄
                $.ajax({
                    url: $(this).attr('action'), // 폼의 action 속성 값 (addMemberProcess.do)
                    type: $(this).attr('method'), // 폼의 method 속성 값 (POST)
                    data: formData, // 수집한 폼 데이터 (id, pass, name, nick, year, gender, tel, address)
                    success: function(response) {
                        // 서버 응답 처리
                        // 서버 (addMemberProcess.do) 에서 성공 응답을 'success' 같은 문자열로 준다고 가정
                        // 실제 서버 응답 형식에 따라 수정 필요 (JSON 객체 등)
                        if (response === "success") {
                            // 서버에서 memberVo 업데이트 성공 시
                            //alert("정보 저장 완료! 다음 단계로 이동합니다."); 
                            // 다음 페이지 (profileImgAndMyLikeSetting.jsp)로 이동
                            window.location.href = "${contextPath}/member/profileImgAndMyLikeSetting.do"; // 다음 페이지 URL로 수정!
                        } else {
                            // 서버에서 실패 응답을 보냈을 경우 (예: 아이디 중복이 뒤늦게 발생 등)
                            alert("정보 저장 중 오류가 발생했습니다. 다시 시도해주세요.");
                            // TODO: 서버에서 특정 필드 오류 메시지를 JSON으로 보낸다면 여기에 반영하는 로직 추가
                        }
                    },
                    error: function(xhr, status, error) {
                        console.error("AJAX 제출 중 오류 발생:", error);
                         alert("오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
                    }
                });
            } else {
                // 최종 유효성 검사 실패 시 (각 필드 에러 메시지 확인하도록 안내)
                alert("입력 정보를 확인해주세요."); // 필수 누락, 중복 확인, 비밀번호 불일치, 형식 오류 등 포함
            }
        }); // -- 폼 제출 이벤트 끝

        // 🌟🌟🌟 페이지 로드 시 '다음' 버튼 상태 초기화 🌟🌟🌟
        toggleNextButton();


    }); // -- $(document).ready 끝

    </script>
</body>
</html>