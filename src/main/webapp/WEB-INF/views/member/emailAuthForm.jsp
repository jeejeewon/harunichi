<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<%-- hidden 필드에 선택한 국가 정보를 저장 --%>
<input type="hidden" id="userNationality" value="">

<section class="email-auth-form">
    <h2>이메일 인증</h2>
    <p>회원가입을 위해 이메일 주소를 입력하고 인증을 진행해주세요.</p>

    <form id="emailAuthForm" action="#" method="post">
        <div class="form-group">
            <label for="email">이메일 주소:</label>
            <input type="email" id="email" name="email" placeholder="이메일 주소를 입력해주세요." required>
        </div>

        <button type="button" id="sendAuthCodeBtn">인증번호 발송</button>

        <div class="form-group" id="authCodeGroup" style="display: none;">
            <label for="authCode">인증번호:</label>
            <input type="text" id="authCode" name="authCode" placeholder="인증번호를 입력해주세요." required>
        </div>

        <button type="button" id="verifyAuthCodeBtn" style="display: none;">인증번호 확인</button>
        <button type="button" id="resendAuthCodeBtn" style="display: none;">인증번호 재발송</button>
    </form>
</section>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function() {
    	// URL 파라미터에서 국가 정보 읽어오기
        var urlParams = new URLSearchParams(window.location.search);
        var nationality = urlParams.get('nationality');
        
        // hidden 필드에 저장
        $('#userNationality').val(nationality);
        
        // [인증번호 발송] 버튼 클릭 이벤트
        $('#sendAuthCodeBtn').on('click', function() {
            var email = $('#email').val(); // 입력한 이메일 주소 가져오기
            var userNationality = $('#userNationality').val(); // 저장해 둔 국가 정보 가져오기

            if (email === "") {
                alert("이메일 주소를 입력해주세요!");
                return;
            }
            
         	// 인증번호발송 버튼 숨기기(중복 클릭 방지)
            var $authButton = $(this); // 클릭된 버튼 요소 저장
            $authButton.hide(); // 버튼 숨김

            // AJAX로 서버에 이메일 전송 요청
            $.ajax({
                url: '${contextPath}/mail/sendAuthEmail.do', // 실제 컨트롤러 URL로 변경
                type: 'POST',
                data: { email: email, nationality: userNationality },
                success: function(response) {
                    if (response === "success") {
                        alert("인증 메일이 발송되었습니다. 메일함을 확인해주세요.");
                        $('#authCodeGroup').show(); // 인증번호 입력 필드 보여주기
                        $('#verifyAuthCodeBtn').show(); // 인증번호 확인 버튼 보여주기
                        $('#resendAuthCodeBtn').show(); // 재발송 버튼 보여주기
                    } else {
                        alert("메일 발송에 실패했습니다. 다시 시도해주세요.");
                        $authButton.show();
                    }
                },
                error: function(xhr, status, error) {
                    console.error("메일 발송 중 오류 발생:", error);
                    alert("메일 발송 중 오류가 발생했습니다.");
                    $authButton.show();
                }
            });
        });
        
     	// [인증번호 재발송] 버튼 클릭 이벤트
        $('#resendAuthCodeBtn').on('click', function() {
            var email = $('#email').val();

            if (email === "") {
                 alert("이메일 주소를 입력해주세요!");
                 return;
            }
            
            var $resendButton = $(this);
            $resendButton.hide();
            
         	// AJAX로 서버에 인증번호 재발송 요청
            $.ajax({
                url: '${contextPath}/mail/sendAuthEmail.do',
                type: 'POST',
                data: { email: email, nationality: $('#userNationality').val() },
                success: function(response) {
                    if (response === "success") {
                         alert("인증 메일이 재발송되었습니다. 메일함을 확인해주세요.");
                         $resendButton.show();
                    } else {
                         alert("메일 재발송에 실패했습니다. 다시 시도해주세요.");
                         $resendButton.show();
                    }
                },
                error: function(xhr, status, error) {
                    console.error("메일 재발송 중 오류 발생:", error);
                    alert("메일 재발송 중 오류가 발생했습니다.");
                    $resendButton.show();
                }
            });
        });
     	
     	
     	

        // [인증번호 확인] 버튼 클릭 이벤트
        $('#verifyAuthCodeBtn').on('click', function() {
            var email = $('#email').val(); // 입력한 이메일 주소
            var authCode = $('#authCode').val(); // 입력한 인증번호
            
            if(authCode === "") {
            	alert("인증번호를 입력해주세요!");
                return;
            }
            
            // AJAX로 서버에 인증번호 확인 요청
            $.ajax({
                url: '${contextPath}/mail/verifyAuthCode.do',
                type: 'POST',

                data: { email: email, authCode: authCode},
                success: function(response) {
                    if (response === "success") {
                        alert("이메일 인증이 완료되었습니다!");
                        $('#email').prop('disabled', true); // 이메일 입력창 비활성화
                        $('#sendAuthCodeBtn').hide(); // 발송 버튼 숨김
                        $('#authCodeGroup').hide(); // 인증번호 입력 필드 숨김
                        $('#verifyAuthCodeBtn').hide(); // 확인 버튼 숨김
                        
                        // 인증 완료 후 다음 페이지로 이동
                        window.location.href = "${contextPath}/여기에다음페이지주소를입력.do";

                    } else {
                        alert("인증번호가 일치하지 않습니다. 다시 확인해주세요.");
                        $('#authCode').val(''); // 인증번호 입력창 비우기
                        $('#authCode').focus(); // 인증번호 입력창에 커서 두기
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
