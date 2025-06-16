<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" href="${ctx}/resources/css/payment.css" />
<div class="payment-result success">
    <h2>🎉 결제가 성공적으로 완료되었습니다!</h2>
    <p>감사합니다.</p>
    <div class="buttons">
        <a href="${ctx}/product/list" class="btn">상품 목록 보기</a>
        <a href="${ctx}/payment/orders" class="btn">내 주문 보기</a>
    </div>
</div>
