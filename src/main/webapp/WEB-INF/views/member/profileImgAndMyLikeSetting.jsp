<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>프로필 이미지 & 관심사 설정</title>
	<!-- 스타일 및 폰트 -->
	<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" /><!-- 셀렉트 라이브러리 -->
	<link href='//spoqa.github.io/spoqa-han-sans/css/SpoqaHanSansNeo.css' rel='stylesheet' type='text/css'><!-- 폰트 -->
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet" type="text/css" media="screen"><!-- 공통스타일 -->
</head>
<body>
	<h1>프로필 이미지 & 관심사 설정</h1>
		<form action="profileImgAndMyLikeSettingProcess.do" method="post" enctype="multipart/form-data">
         <!-- 프로필 이미지 설정 -->
        <div>
            <label>프로필 이미지:</label>
            	<c:if test="${empty profileImgPath}">
                	<img id="profileImage" src="${contextPath}/resources/icon/basic_profile.jpg" alt="기본 프로필 이미지" width="100" height="100">
            	</c:if>
            	<c:if test="${not empty profileImgPath}">
                	<img id="profileImage" src="${profileImgPath}" alt="선택한 프로필 이미지" width="100" height="100">
            	</c:if>
            	<input type="file" id="profileImg" name="profileImg" accept="image/*" onchange="previewImage(this)"> <!-- 이미지 파일만 첨부할수있게 설정 -->
            	
        </div>

        <!-- 관심사 설정 (체크박스) -->
        <div>
            <label>관심사:</label><br>
            <input type="checkbox" id="interest1" name="myLike" value="여행"> <label for="interest1">여행</label><br>
            <input type="checkbox" id="interest2" name="myLike" value="맛집"> <label for="interest2">맛집</label><br>
            <input type="checkbox" id="interest3" name="myLike" value="코딩"> <label for="interest3">코딩</label><br>
            <input type="checkbox" id="interest4" name="myLike" value="음악"> <label for="interest4">음악</label><br>
            <input type="checkbox" id="interest5" name="myLike" value="영화"> <label for="interest5">영화</label><br>
            <input type="checkbox" id="interest6" name="myLike" value="스포츠"> <label for="interest6">스포츠</label><br>
            <input type="checkbox" id="interest7" name="myLike" value="패션"> <label for="interest7">패션</label><br>
            <input type="checkbox" id="interest8" name="myLike" value="게임"> <label for="interest8">게임</label><br>
            <input type="checkbox" id="interest9" name="myLike" value="반려동물"> <label for="interest9">반려동물</label><br>
            <input type="checkbox" id="interest10" name="myLike" value="요리"> <label for="interest10">요리</label><br>
            <input type="checkbox" id="interest11" name="myLike" value="운동"> <label for="interest11">운동</label><br>
            <input type="checkbox" id="interest12" name="myLike" value="독서"> <label for="interest12">독서</label><br>
            <input type="checkbox" id="interest13" name="myLike" value="드라마"> <label for="interest13">드라마</label><br>
            <input type="checkbox" id="interest14" name="myLike" value="웹툰"> <label for="interest14">웹툰</label><br>
            <input type="checkbox" id="interest15" name="myLike" value="커피"> <label for="interest15">커피</label><br>
            <input type="checkbox" id="interest16" name="myLike" value="차"> <label for="interest16">차</label><br>
            <input type="checkbox" id="interest17" name="myLike" value="사진"> <label for="interest17">사진</label><br>
            <input type="checkbox" id="interest18" name="myLike" value="DIY"> <label for="interest18">DIY</label><br>
            <input type="checkbox" id="interest19" name="myLike" value="영화감상"> <label for="interest19">영화감상</label><br>
            <input type="checkbox" id="interest20" name="myLike" value="음악감상"> <label for="interest20">음악감상</label><br>
        </div>

        <button type="submit">가입 완료</button>

        <script>
            function previewImage(input) {
                const file = input.files[0];
                if (!file) return;

                const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
                if (!allowedTypes.includes(file.type)) {
                    alert("이미지 파일만 업로드 가능합니다 (JPG, PNG, GIF)");
                    input.value = "";
                    return;
                }

                const reader = new FileReader();
                reader.onload = function (e) {
                    document.getElementById('profileImage').src = e.target.result;
                }
                reader.readAsDataURL(file);
            }
        </script>
    </form>
</body>
</html>
