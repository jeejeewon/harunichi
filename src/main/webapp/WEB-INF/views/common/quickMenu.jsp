<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<body>
	<%--
			최근 상세페이지를 열어 본 도서상품 이미지를 표시하는 퀵메뉴 디자인을 나타내는 quickMenu.jsp파일입니다.
			최근 본 상품은 상품목록(new ArrayList<GoodsVo>();)에서 상품목록을 가져온 다음 첫번째 상품 이미지만 표시하고
			다른 상품 이미지는 <hidden>태그에 저장합니다.(동일한 <hidden>태그에 여러개의 데이터 저장시 자동으로 배열로 저장됩니다.)
			사용자가 다음을 클릭하면 <hidden>태그의 상품정보를 자바스크립트 함수로 전달하여 이미지를 표시합니다.
	 --%>


	<div id="sticky">
		<ul>
			<li>
				<a href="#"> 
					<img width="24" height="24" src="${contextPath}/resources/image/facebook_icon.png"> 페이스북
				</a>
			</li>
			<li>
				<a href="#"> 
					<img width="24" height="24" src="${contextPath}/resources/image/twitter_icon.png"> 트위터
				</a>
			</li>
			<li>
				<a href="#"> 
					<img width="24" height="24" src="${contextPath}/resources/image/rss_icon.png"> RSS 피드
				</a>
			</li>
		</ul>
		<div class="recent">
			<h3>최근 본 상품</h3>
			<ul>
				<c:choose>
					<%-- 최근 본 도서상품 은 상품목록(new ArrayList<GoodsVO>();)에서 상품정보(GoodsVO객체들이)들이 없을 경우  --%>
					<c:when test="${ empty sessionScope.quickGoodsList }">
						<strong>최근본 상품이 없습니다.</strong>
					</c:when>
					
					<%-- 최근 본 도서상품 은 상품목록(new ArrayList<GoodsVO>();)에서 상품정보(GoodsVO객체들이)들이 하나라도 있을 경우 --%>
					<c:otherwise>
						<form name="frm_sticky">
							
							<%-- 세션영역에 저장된 빠른 퀵 메뉴 디자인에 보여줄 이미지정보를  <hidden>태그에 차례대로 저장합니다. --%>
							<c:forEach var="item" items="${sessionScope.quickGoodsList }" varStatus="itemNum">
								<c:choose>
									<%-- 한번 반복 할때의 첫번쨰 이미지만 퀵메뉴영역에 표시하고  --%>
									<c:when test="${itemNum.count==1 }">
										<a href="javascript:goodsDetail();"> 
											<img width="75"
												 height="95" id="img_sticky"
											     src="${contextPath}/thumbnails.do?goods_id=${item.goods_id}&fileName=${item.goods_fileName}">
										</a>
										<input type="hidden" name="h_goods_id" value="${item.goods_id}" />
										<input type="hidden" name="h_goods_fileName" value="${item.goods_fileName}" />
										<br>
									</c:when>
									<c:otherwise>
										<input type="hidden" name="h_goods_id" value="${item.goods_id}" />
										<input type="hidden" name="h_goods_fileName" value="${item.goods_fileName}" />
									</c:otherwise>
								</c:choose>
							</c:forEach>
					</c:otherwise>
				</c:choose>
			</ul>
			</form>
		</div>
		<div>
			<c:choose>
				<%--  최근본 도서상품이 하나라도 없다면? --%>
				<c:when test="${ empty sessionScope.quickGoodsList }">
					<h5>&nbsp; &nbsp; &nbsp; &nbsp; 0/0 &nbsp;</h5>
				</c:when>
				<%-- 최근본 도서상품(상세페이지에 보여주는 클릭하여 조회된 GoodsVO객체)이 ArrayList배열에 하나라도 있으면? --%>
				<c:otherwise>
					<h5>
						<a href='javascript:fn_show_previous_goods();'> 이전 </a> &nbsp; 
						
						<span id="cur_goods_num">1</span>/${sessionScope.quickGoodsListNum} &nbsp; 
						
						<a href='javascript:fn_show_next_goods();'> 다음 </a>
					</h5>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</body>
</html>

