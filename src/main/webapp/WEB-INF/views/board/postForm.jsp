<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<script src="http://code.jquery.com/jquery-latest.min.js"></script>

<h1>게시글 등록</h1>
<form action="${contextPath}/board/post" method="post"
	enctype="multipart/form-data">

	<table>
		<tr>
			<td>작성자:</td>
			<td><input type="text" name="boardWriter"></td>
		</tr>
		<tr>
			<td>내용:</td>
			<td><textarea name="boardCont"></textarea></td>
		</tr>
		<tr>
			<td>카테고리:</td>
			<td><select name="boardCate">
					<option value="" disabled selected>카테고리를 선택해주세요</option>
					<option value="생활정보">생활정보</option>
					<option value="맛집, 카페">맛집, 카페</option>
					<option value="일상">일상</option>
					<option value="찾습니다">찾습니다</option>
					<option value="건강, 운동">건강, 운동</option>
					<option value="육아, 교육">육아, 교육</option>
			</select>

			<!-- 
			디비에서 가져올 때
				<select name="boardCate">
					<c:forEach var="category" items="${categoryList}">
						<option value="${category.categoryId}">${category.categoryName}</option>
					</c:forEach>
				</select>
			 -->
		</tr>
		<tr>
			<td>첨부 이미지:</td>
			<%-- 설명을 변경했습니다 --%>
			<td><input type="file" name="imageFiles" accept="image/*"
				multiple></td>
			<%-- multiple 속성 추가 --%>
		</tr>
	</table>
	<input type="submit" value="등록">
</form>

<script>
    $(document).ready(function(){
        // 파일 입력 필드의 change 이벤트에 validateFileType 함수 연결
        // input[name="imageFiles"]는 name 속성이 "imageFiles"인 input 태그를 선택합니다.
        $('input[name="imageFiles"]').change(function(){
            validateFileType(this); // this는 이벤트가 발생한 input 요소를 가리킵니다.
        });
    });

    function validateFileType(input){
        const files = input.files; // 선택된 파일 목록 가져오기
        const maxFiles = 4; // 허용되는 최대 파일 개수

        if(files.length === 0){
            console.log("선택된 파일 없음");
            return; // 파일 선택이 취소되었거나 파일이 없으면 함수 종료
        }

        console.log("파일 유효성 검사 시작");

        // 1. 파일 개수 확인
        if (files.length > maxFiles) {
            alert('이미지는 최대 ' + maxFiles + '개까지 업로드할 수 있습니다.');
            input.value = ''; // 파일 선택 필드 초기화 (선택된 모든 파일 제거)
            console.log("파일 개수 초과. 파일 선택 초기화.");
            return; // 유효하지 않은 파일 개수 발견 시 즉시 함수 종료
        }

        // 2. 파일 타입 검사 (파일 개수 검사 통과 후 실행)
        console.log("파일 개수 유효성 검사 통과.");
        for(let i = 0; i < files.length; i++){
            const file = files[i];
            console.log("검사 중 파일:", file.name, "타입:", file.type);

            // 파일 타입이 'image/'로 시작하는지 확인
            if(!file.type.startsWith('image/')){
                alert('이미지 파일만 업로드할 수 있습니다.\n잘못된 파일: ' + file.name);
                input.value = ''; // 파일 선택 필드 초기화 (선택된 모든 파일 제거)
                console.log("유효하지 않은 파일 타입 감지. 파일 선택 초기화.");
                return; // 유효하지 않은 파일 발견 시 즉시 함수 종료
            }
        }

        console.log("모든 파일 유효성 검사 통과.");
    }
</script>