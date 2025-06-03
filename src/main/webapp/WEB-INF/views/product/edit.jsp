<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>상품 수정</h2>

<form action="${pageContext.request.contextPath}/product/edit.do"
      method="post" enctype="multipart/form-data">
    <input type="hidden" name="productId" value="${product.productId}">
    <table class="product-form">
        <tr>
            <td>상품명</td>
            <td><input type="text" name="productTitle"
                       value="${product.productTitle}" required></td>
        </tr>
        <tr>
            <td>가격</td>
            <td><input type="number" name="productPrice"
                       value="${product.productPrice}" required></td>
        </tr>
        <tr>
            <td>거래방식</td>
            <td>
                <select name="productStatus">
                    <option value="판매" ${product.productStatus == '판매' ? 'selected' : ''}>판매</option>
                    <option value="나눔" ${product.productStatus == '나눔' ? 'selected' : ''}>나눔</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>카테고리</td>
            <td><input type="text" name="productCategory"
                       value="${product.productCategory}" required></td>
        </tr>
        <tr>
            <td>설명</td>
            <td><textarea name="productContent">${product.productContent}</textarea></td>
        </tr>

        <tr>
            <td>기존 이미지</td>
            <td>
                <c:if test="${not empty product.productImg}">
                    <img id="currentPreview" src="${product.productImg}" width="100" style="border:1px solid #ccc; border-radius:4px;"><br>
                    <label><input type="checkbox" name="deleteImg" value="true"> 기존 이미지 삭제</label>
                </c:if>
            </td>
        </tr>

        <tr>
            <td>새 이미지</td>
            <td>
                <input type="file" name="uploadFile" accept="image/*" onchange="previewImage(event)">
                <div style="margin-top: 10px;">
                    <img id="newPreview" src="#" alt="미리보기" style="display:none; max-width: 100px; border: 1px solid #ccc; border-radius: 4px;" />
                </div>
            </td>
        </tr>

    </table>

    <input type="submit" value="수정">
</form>

<script>
function previewImage(event) {
    const input = event.target;
    const preview = document.getElementById('newPreview');
    const current = document.getElementById('currentPreview');

    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            preview.src = e.target.result;
            preview.style.display = 'block';
            if (current) current.style.display = 'none';
        }
        reader.readAsDataURL(input.files[0]);
    } else {
        preview.src = '#';
        preview.style.display = 'none';
        if (current) current.style.display = 'block';
    }
}
</script>
