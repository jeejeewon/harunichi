<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<div class="product-detail-container">
    <h2 class="product-title">${product.productTitle}</h2>

    <div class="writer-info">
        <img src="${product.writerProfileImg}" alt="프로필 이미지" class="writer-img">
        <span class="writer-nick">${product.writerNick}</span>
    </div>

    <div class="product-detail">
        <img src="${product.productImg}" alt="상품 이미지" class="product-image">

        <ul class="product-meta">
            <li><strong>가격:</strong> ${product.productPrice}원</li>
            <li><strong>상태:</strong> ${product.productStatus}</li>
            <li><strong>카테고리:</strong> ${product.productCategory}</li>
            <li><strong>설명:</strong><br> ${product.productContent}</li>
            <li><strong>조회수:</strong> ${product.productCount}</li>
            <li><strong>등록일:</strong> ${product.productDate}</li>
        </ul>
    </div>

    <div class="detail-buttons">
        <a href="${pageContext.request.contextPath}/product/edit.do?productId=${product.productId}" class="btn">수정</a>

        <form action="${pageContext.request.contextPath}/product/delete.do" method="post" style="display:inline;">
            <input type="hidden" name="productId" value="${product.productId}">
            <input type="submit" value="삭제" class="btn">
        </form>

        <a href="${pageContext.request.contextPath}/product/list.do" class="btn">목록</a>
        
        <button onclick="purchaseProduct(${product.productId})" class="btn btn-buy">구입하기</button>
        <button onclick="startChat('${product.productWriterId}')" class="btn btn-chat">채팅하기</button>
    </div>
</div>


<script>

function startChat(writerId) {
    alert("채팅방을 생성합니다. (예: /chat?to=" + writerId + ")");
    // location.href = '/chat.do?to=' + writerId;
}
</script>
