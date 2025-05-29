<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- jQuery CDN -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<div id="product-container" class="product-grid"></div>
<div style="text-align: center; margin-top: 20px;">
    <button id="loadMoreBtn">더보기</button>
</div>

<script>
let page = 0;

function loadProducts() {
    $.ajax({
        url: '${pageContext.request.contextPath}/product/ajaxList.do',
        method: 'GET',
        data: { page: page },
        success: function(products) {
            if (!products || products.length === 0) {
                $('#loadMoreBtn').hide(); // 더보기 버튼 숨김 처리 (더 이상 데이터 없을 때)
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
                                <img src="${p.productWriterImg}" class="writer-img" alt="작성자">
                                <span>${p.productWriterNick}</span>
                            </div>
                        </div>
                    </div>
                `);
            });

            page++;
        },
        error: function(err) {
            alert("상품 목록을 불러오는 중 오류가 발생했습니다.");
            console.error(err);
        }
    });
}

$(document).ready(() => {
    loadProducts();
    $('#loadMoreBtn').on('click', loadProducts);
});
</script>
