<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<% request.setCharacterEncoding("utf-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>harunichi admin</title>
	<link href='//spoqa.github.io/spoqa-han-sans/css/SpoqaHanSansNeo.css' rel='stylesheet' type='text/css'><!-- 폰트 -->
	<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" /><!-- 구글아이콘 연결 -->
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet" type="text/css" media="screen"><!-- 공통스타일 -->
    <link href="${contextPath}/resources/css/admin.css" rel="stylesheet" type="text/css" media="screen">
</head>
<body>
	<div id="wrap">
		<header>
			<tiles:insertAttribute name="header" />
		</header>
		<div id="inner-wrap">
			<aside>
				<tiles:insertAttribute name="side" />
			</aside>
			<article>
				<tiles:insertAttribute name="body" />
			</article>
		</div>
	</div>
</body>
</html>