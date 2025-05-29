<%@ page contentType="text/html; charset=UTF-8"%>

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
			<td>상태</td>
			<td><select name="productStatus">
					<option value="판매"
						${product.productStatus == '판매' ? 'selected' : ''}>판매</option>
					<option value="나눔"
						${product.productStatus == '나눔' ? 'selected' : ''}>나눔</option>
			</select></td>
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
			<td><img src="${product.productImg}" width="100"></td>
		</tr>
		<tr>
			<td>새 이미지</td>
			<td><input type="file" name="uploadFile"></td>
		</tr>
	</table>
	<input type="submit" value="수정">
</form>
