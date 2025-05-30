<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- jQuery CDN -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<!-- 검색/필터 영역 -->
<div style="margin: 20px 0; text-align: center;">
    <input type="text" id="keyword" placeholder="검색어 입력 (제목/내용)" />
    <select id="category">
        <option value="">전체 카테고리</option>
        <option value="book">도서</option>
        <option value="electronics">전자제품</option>
        <option value="toy">장난감</option>
        <option value="fashion">패션</option>
        <option value="music">음반</option>
        <option value="etc">기타</option>
    </select>
    <input type="number" id="minPrice" placeholder="최소가격" style="width:100px;" />
    <input type="number" id="maxPrice" placeholder="최대가격" style="width:100px;" />
    
    <button onclick="searchProducts()">검색</button>
</div>

<!-- 상품 리스트 -->
<div id="product-container" class="product-grid"></div>

<div style="text-align: center; margin-top: 20px;">
    <button id="loadMoreBtn">더보기</button>
</div>

<script>
let page = 1;
let isSearchMode = false;

function renderProducts(products) {
    if (!products || products.length === 0) {
        $('#loadMoreBtn').hide();
        return;
    }

    products.forEach(p => {
        $('#product-container').append(`
            <div class="product-card">
                <img src="${p.productImg}" alt="상품 이미지" class="product-thumb">
                <div class="product-info">
                    <div class="product-title">${p.productTitle}</div>
                    <div class="product-price">${p.productPrice}원</div>
                    <div class="product-category">[${p.productCategory}]</div>
                    <div class="writer-info">
                        <img src="${p.profileImg}" class="writer-img" alt="작성자">
                        <span>${p.nick}</span>
                    </div>
                </div>
            </div>
        `);
    });
}

// 초기 상품 목록 (일반 목록 로드)
function loadProducts() {
    $.ajax({
        url: '${pageContext.request.contextPath}/product/moreList.do',
        method: 'GET',
        data: { page: page },
        success: function(products) {
            renderProducts(products);
            page++;
        },
        error: function(err) {
            alert("상품 목록 로드 실패");
            console.error(err);
        }
    });
}

// 검색/필터 기반 상품 목록
function searchProducts() {
    const keyword = $('#keyword').val();
    const category = $('#category').val();
    const minPrice = $('#minPrice').val();
    const maxPrice = $('#maxPrice').val();

    isSearchMode = true;
    page = 1;
    $('#product-container').empty(); // 이전 목록 초기화

    $.ajax({
        url: '${pageContext.request.contextPath}/product/search.do',
        method: 'GET',
        data: {
            keyword: keyword,
            category: category,
            minPrice: minPrice,
            maxPrice: maxPrice,
            page: page
        },
        success: function(products) {
            renderProducts(products);
            page++;
        },
        error: function(err) {
            alert("검색 실패");
            console.error(err);
        }
    });
}

// 더보기 버튼 이벤트
$('#loadMoreBtn').on('click', () => {
    if (isSearchMode) {
        searchProducts(); // 검색모드에서는 조건 유지한 채 다음 페이지
    } else {
        loadProducts();
    }
});

// Enter 키로 검색
$(document).on('keydown', function(e) {
    if (e.key === 'Enter') {
        searchProducts();
    }
});

// 초기 로딩
$(document).ready(() => {
    loadProducts();
});
</script>
