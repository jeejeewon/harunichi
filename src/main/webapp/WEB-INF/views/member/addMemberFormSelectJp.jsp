<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<%-- 일본인 회원가입 폼 --%>

<section class="register-options">
    <%-- 이메일 인증 버튼 --%>
    <button type="button" id="email-auth-btn">
		<img src="${contextPath}/resources/image/email_icon.svg" alt="이메일 아이콘">
        <span>メールでの認証</span>
    </button>
</section>

