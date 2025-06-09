<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<style>
.error {
	color: red;
	font-size: 0.9em;
}
</style>
</head>
<body>
	<h1>회원가입 폼</h1>

	<form action="addMemberProcess.do" method="post">
		<div class="form-group">
			<label for="id">아이디:</label>
			<div style="display: flex; gap: 10px;">
				<input type="text" name="id" id="id">
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
			<label for="name">이름:</label>
			<input type="text" name="name" id="name">
			<span class="error" id="error-name"></span>
		</div>

		<div class="form-group">
			<label for="nick">닉네임:</label>
			<input type="text" name="nick" id="nick">
			<span class="error" id="error-nick"></span>
		</div>

		<div class="form-group">
			<label for="year">생년월일:</label>
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
			<label for="email">이메일:</label>
			<input type="email" name="email" id="email">
			<span class="error" id="error-email"></span>
		</div>
		
		<div class="form-group">
			<label for="tel">전화번호</label>
			<input type="text" name="tel" id="tel">
		</div>

		<div class="form-group">
			<label for="address">주소:</label>
			<input type="text" id="address" name="address" placeholder="예: 서울특별시 강남구 역삼동 123-45">
		</div>







		<div class="form-group">
			<button type="submit">가입 완료</button>
		</div>
	</form>

	<script>
    	//필수값 입력 확인
    	let checkIdConfirmed = false;
        document.addEventListener("DOMContentLoaded", function () {
            const form = document.querySelector("form");

            form.addEventListener("submit", function (e) {
                let isValid = true;
                document.querySelectorAll(".error").forEach(span => span.textContent = "");
                
                const fields = [
                    { id: "id", msg: "아이디를 입력해주세요." },
                    { id: "pass", msg: "비밀번호를 입력해주세요." },
                    { id: "name", msg: "이름을 입력해주세요." },
                    { id: "nick", msg: "닉네임을 입력해주세요." },
                    { id: "year", msg: "생년월일을 선택해주세요." },
                    { id: "email", msg: "이메일을 입력해주세요." }
                ];

                fields.forEach(field => {
                    const input = document.getElementById(field.id);
                    const errorSpan = document.getElementById("error-" + field.id);
                    if (!input.value.trim()) {
                        errorSpan.textContent = field.msg;
                        isValid = false;
                    }
                });
                
                if (!checkIdConfirmed) {
                    alert("아이디 중복 확인을 해주세요.");
                    isValid = false;
                }

                if (!isValid) {
                    e.preventDefault(); // 제출 막음
                }
            });
        });
    	
    	//아이디 중복 확인
    	document.getElementById("checkIdBtn").addEventListener("click", function() {
    		const id = document.getElementById("id").value.trim();
    		const resultSpan = document.getElementById("id-check-result");
    		
    		if (id === "") {
        		resultSpan.style.color = "red";
        		resultSpan.textContent = "아이디를 입력해주세요.";
        		return;
    		}

    		fetch("checkId.do?id=" + encodeURIComponent(id))
        		.then(response => response.json())
        		.then(data => {
            		if (data.exists) {
                		resultSpan.style.color = "red";
                		resultSpan.textContent = "이미 사용 중인 아이디입니다.";
                		checkIdConfirmed = false;
            	} else {
                		resultSpan.style.color = "green";
                		resultSpan.textContent = "사용 가능한 아이디입니다!";
                		checkIdConfirmed = true;
            		}
        		})
        	.catch(error => {
            	console.error("에러 발생:", error);
        	});
		});
    </script>
</body>
</html>