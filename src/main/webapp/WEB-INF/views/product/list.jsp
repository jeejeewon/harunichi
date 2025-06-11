<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/product.css">

<!-- 최신 jQuery -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<!-- 검색/필터 영역 + 글쓰기 버튼 -->
<div class="search-bar">
    <div class="filter-group">
        <input type="text" id="keyword" placeholder="검색어 입력 (제목/내용)" />
        <select id="category">
            <option value="">상품카테고리</option>
            <option value="book">📚도서</option>
            <option value="electronics">💻전자제품</option>
            <option value="toy">🧸장난감</option>
            <option value="fashion">👕패션</option>
            <option value="music">💿음반</option>
            <option value="etc">🎁기타</option>
        </select>
        <button class="btn-sky" onclick="searchProducts()">검색</button>
    </div>

    <button class="btn-sky" onclick="location.href='${pageContext.request.contextPath}/product/write'">글쓰기</button>
</div>


<!-- 상품 리스트 -->
<div id="product-container" class="product-grid"></div>

<!-- 더보기 버튼 -->
<div style="text-align: center; margin-top: 20px;">
    <button id="loadMoreBtn" class="btn btn-more">더보기</button>
</div>

<!-- JavaScript -->
<script>
const ctx = '${pageContext.request.contextPath}';
let page = 1;
let isSearchMode = false;

// 카테고리 코드 → 한글명 매핑
const categoryMap = {
  book: '📚도서',
  electronics: '💻전자제품',
  toy: '🧸장난감',
  fashion: '👕패션',
  music: '💿음반',
  etc: '🎁기타'
};

// 상품 렌더링 함수
function renderProducts(products) {
    if (!products || products.length === 0) {
        $('#loadMoreBtn').hide();
        if (page === 1) {
            $('#product-container').html('<p style="text-align:center;">등록된 상품이 없습니다.</p>');
        }
        return;
    }

    products.forEach(p => {
        // 디버깅 로그
        console.log("카테고리:", p.productCategory, "상태:", p.productStatus);

        // 카테고리 매핑
        const rawCategory = (p.productCategory || '').toLowerCase();
        const displayCategory = categoryMap[rawCategory] || '카테고리 없음';

        // 이미지 처리
		const productImg = p.productImg ? ctx + p.productImg : ctx + '/resources/images/product/no_image.png';
		const profileImg = p.writerProfileImg ? ctx + p.writerProfileImg : ctx + '/resources/images/member/default_profile.png';
        const writerNick = p.writerNick;

        // 상태 처리 (문자열 "1" 또는 숫자 1 모두 처리)
        const statusText = p.productStatus == 1 || p.productStatus === "1" ? '나눔' : '판매';

        const productHtml =
            '<div class="product-card" onclick="location.href=\'' + ctx + '/product/view?productId=' + p.productId + '\'">' +
                '<img src="' + productImg + '" alt="상품 이미지" class="product-thumb">' +
                '<div class="product-info">' +
                    '<div class="product-title">' + (p.productTitle || '제목 없음') + '</div>' +
                    '<div class="product-price">' + (p.productPrice ? p.productPrice.toLocaleString() : '0') + '원</div>' +
                    '<div class="product-category">' + displayCategory + '</div>' +
                    '<div class="product-status">' + statusText + '</div>' +
                    '<div class="writer-info">' +
                        '<img src="' + profileImg + '" class="writer-img" alt="작성자">' +
                        '<span class="writer-nick">🖊️ '+ writerNick + '</span>' +
                    '</div>' +
                '</div>' +
            '</div>';
        $('#product-container').append(productHtml);
    });
}


// 공통 데이터 요청 함수
function fetchProducts(url, params) {
    $.ajax({
        url: url,
        method: 'GET',
        data: params,
        success: function(products) {
            console.log('불러온 데이터:', products);
            renderProducts(products);
            page++;
        },
        error: function(err) {
            console.error('상품 로딩 실패:', err);
            alert('상품 정보를 불러오는 데 실패했습니다.');
        }
    });
}

// 초기 목록 로딩
function loadProducts() {
    fetchProducts(ctx + '/product/moreList', { page: page });
}

// 검색 필터 적용
function searchProducts(reset = true) {
    const keyword = $('#keyword').val();
    const category = $('#category').val();

    if (reset) {
        page = 1;
        $('#product-container').empty();
    }

    isSearchMode = true;

    fetchProducts(ctx + '/product/search', {
        keyword: keyword,
        category: category,
        page: page
    });
}

// 더보기 버튼 이벤트
$('#loadMoreBtn').on('click', () => {
    if (isSearchMode) {
        searchProducts(false);  // reset = false → 페이지 누적 유지
    } else {
        loadProducts();
    }
});

// 엔터키 이벤트
$(document).on('keydown', function(e) {
    if (e.key === 'Enter') {
        searchProducts();
    }
});

// 페이지 로딩 시 초기 데이터 로딩
$(document).ready(() => {
    loadProducts();
});
</script>
