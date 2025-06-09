<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" href="${ctx}/resources/css/product.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<div class="product-detail-container">
    <h2 class="product-title">${product.productTitle}</h2>

    <div class="writer-info">
        <img src="${ctx}${product.writerProfileImg}" alt="프로필 이미지" class="writer-img">
        <span class="writer-nick">${product.writerNick}</span>
    </div>

    <div class="product-detail">
        <img src="${ctx}${product.productImg != null ? product.productImg : '/resources/images/no-image.png'}"
             alt="상품 이미지" class="product-image">

        <div class="product-info-box">
            <ul class="product-meta">
                <li><strong>가격:</strong>
                    <span><fmt:formatNumber value="${product.productPrice}" type="number" groupingUsed="true" />원</span>
                </li>
				<li><strong>거래방식:</strong>
				    <span>
				        <c:choose>
				            <c:when test="${product.productStatus == 1}">나눔</c:when>
				            <c:otherwise>판매</c:otherwise>
				        </c:choose>
				    </span>
				</li>
                <li><strong>카테고리:</strong>
                    <span>
                        <c:choose>
                            <c:when test="${product.productCategory == 'book'}">도서</c:when>  
                            <c:when test="${product.productCategory == 'electronics'}">전자제품</c:when>                                               
                            <c:when test="${product.productCategory == 'toy'}">장난감</c:when>                                               
                            <c:when test="${product.productCategory == 'fashion'}">패션</c:when>
                            <c:when test="${product.productCategory == 'music'}">음반</c:when>
                            <c:when test="${product.productCategory == 'etc'}">기타</c:when>
                            <c:otherwise>${product.productCategory}</c:otherwise>
                        </c:choose>
                    </span>
                </li>
                <li><strong>설명:</strong> <p>${product.productContent}</p></li>
                <li><strong>조회수:</strong> <span>${product.productCount}</span></li>
                <li><strong>등록일:</strong>
                    <span><fmt:formatDate value="${product.productDate}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
                </li>
            </ul>

            <div class="action-buttons">
                <button onclick="startChat('${product.productWriterId}')" class="btn btn-chat">채팅하기</button>
                <button id="likeButton" class="btn-like">
                    <i class="fa-regular fa-heart">❤️</i> <span id="likeCount">0</span>
                </button>
            </div>
        </div>
    </div>

    <div class="detail-buttons">
		<form id="editForm" action="${ctx}/product/edit" method="get" style="display:inline;">
		    <input type="hidden" name="productId" value="${product.productId}">
		    <button type="submit" class="btn btn-edit" onclick="return checkLogin()">수정</button>
		</form>
		<form id="deleteForm" action="${ctx}/product/delete" method="post" style="display:inline-block;">
		    <input type="hidden" name="productId" value="${product.productId}">
		    <button type="submit" class="btn btn-delete" onclick="return checkLogin()">삭제</button>
		</form>
        <a href="${ctx}/product/list" class="btn btn-list">목록</a>
    </div>
</div>

<div class="other-products">
  <h3>👍 ${product.writerNick}님의 다른 상품</h3>
  <div class="other-products-row">
    <button id="prevBtn" class="btn-nav">◀️ 이전</button>
    <div id="other-product-list" class="other-product-list"></div>
    <button id="nextBtn" class="btn-nav">다음 ▶️</button>
  </div>
</div>

<script>
  const ctx = '${ctx}';
  const currentProductId = '${product.productId}';
  const writerId = '${product.productWriterId}';
  const currentUserId = '${sessionScope.loginId}';
  let page = 1;
  const size = 5;

  function loadOtherProducts() {
    $.ajax({
      url: ctx + '/product/other',
      method: 'GET',
      data: {
        writerId: writerId,
        productId: currentProductId,
        page: page,
        size: size
      },
      success: function (data) {
        const listEl = $('#other-product-list');
        listEl.empty();

        if (data.length === 0) {
          listEl.html('<p>작성자의 다른 상품이 없습니다.</p>');
          return;
        }

        console.log("불러온 데이터 배열:", data);
        
        data.forEach(p => {
        	  console.log("상품 :", p);

        	  const title = p.productTitle || '제목 없음';
        	  const price = (p.productPrice != null) ? Number(p.productPrice).toLocaleString() + '원' : '가격 미정';
        	  const imgSrc = p.productImg ? (ctx + p.productImg) : (ctx + '/resources/images/no-image.png');

        	  const html =
        		  '<div class="other-product-card" onclick="location.href=\'' + ctx + '/product/view?productId=' + p.productId + '\'">' +
        		    '<img src="' + imgSrc + '" alt="상품 이미지">' +
        		    '<p class="other-product-title">' + title + '</p>' +
        		    '<p class="other-product-price">' + price + '</p>' +
        		  '</div>';

        	  console.log("렌더링 HTML:", html);
        	  $('#other-product-list').append(html);
        	});
      	},
      error: function () {
        $('#other-product-list').html("<p>작성자의 다른 상품을 불러오지 못했습니다.</p>");
      }
    });
  }

  $(document).ready(function () {
    loadOtherProducts();

    // 좋아요 상태 불러오기
    $.get(ctx + '/like/status', { productId: currentProductId }, function (res) {
      if (res.liked) {
        $('#likeButton').addClass('liked');
        $('#likeButton i').removeClass('fa-regular').addClass('fa-solid');
      }
      $('#likeCount').text(res.likeCount);
    }).fail(function (jqXHR) {
      console.error("좋아요 상태 실패:", jqXHR.responseText);
    });

    // 좋아요 토글
    $('#likeButton').on('click', function () {
      if (!currentUserId || currentUserId === 'null' || currentUserId === '') {
        alert('로그인이 필요합니다.');
        location.href = ctx + '/member/login';
        return;
      }

      $.ajax({
        url: ctx + '/like/toggle',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ productId: currentProductId }),
        success: function (res) {
          if (res.liked) {
            $('#likeButton').addClass('liked');
            $('#likeButton i').removeClass('fa-regular').addClass('fa-solid');
          } else {
            $('#likeButton').removeClass('liked');
            $('#likeButton i').removeClass('fa-solid').addClass('fa-regular');
          }
          $('#likeCount').text(res.likeCount);
        },
        error: function (jqXHR) {
          console.error("좋아요 토글 실패:", jqXHR.responseText);
          alert("좋아요 처리에 실패했습니다.");
        }
      });
    });

    // 이전/다음 버튼
    $('#prevBtn').on('click', function () {
      if (page > 1) {
        page--;
        loadOtherProducts();
      }
    });

    $('#nextBtn').on('click', function () {
      page++;
      loadOtherProducts();
    });
  });

  // 전역 함수로 채팅 시작
  function startChat(writerId) {
    if (!currentUserId || currentUserId === 'null' || currentUserId === '') {
      alert("채팅 기능은 로그인 후 이용 가능합니다.");
      location.href = ctx + '/member/login';
      return;
    }
    if (writerId === currentUserId) {
      alert("자신과는 채팅할 수 없습니다.");
      return;
    }
    location.href = ctx + '/chat?to=' + writerId;
  }
  
  // 수정, 삭제 버튼 클릭
//   function checkLogin() {
// 	  const currentUserId = '${sessionScope.loginId}';
// 	  if (!currentUserId || currentUserId === 'null' || currentUserId === '') {
// 	    alert('로그인 후 이용 가능합니다.');
// 	    location.href = ctx + '/member/login';
// 	    return false;
// 	  }
// 	  return true;
// 	}
</script>
