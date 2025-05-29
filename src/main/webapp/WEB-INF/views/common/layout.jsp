<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%
request.setCharacterEncoding("utf-8");
%>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width">
<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" /><!-- 셀렉트 라이브러리 -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"><!-- 폰트어썸(아이콘)라이브러리 -->
<link href='//spoqa.github.io/spoqa-han-sans/css/SpoqaHanSansNeo.css' rel='stylesheet' type='text/css'><!-- 폰트 -->
<link href="${contextPath}/resources/css/main.css" rel="stylesheet" type="text/css" media="screen">
<title><tiles:insertAttribute name="title" /></title>
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
		<footer>
			<tiles:insertAttribute name="footer" />
		</footer>
	</div>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script><!-- 제이쿼리 -->
	<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script><!-- 셀렉트 라이브러리 -->
	<script>
		//국가선택 로직
		$(document).ready(function() {
			$('#country-select').select2({
				minimumResultsForSearch: -1,
				templateResult: formatState,
				templateSelection: formatState 
			});

			
			function formatState (state) {
				if (!state.id) {
					return state.text;
				}
				var $state = $(
					'<span><img src="' + state.element.dataset.image + '" class="country-icon" style="width: 18px; height: auto; margin-right: 5px; vertical-align: middle;" /> ' + state.text + '</span>'
				);
				return $state;
			}
		});
	</script>
</body>
