<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- Product catalog fragment - dynamically loaded via AJAX --%>
<c:forEach var="item" items="${catalogItems}">
    <c:set var="isSoldOut" value="${item.availability.status == 'SOLD_OUT'}"/>
    <c:set var="hasPromotion" value="${item.basePrice > item.resolvedPrice}"/>

    <article
            class="product-card ${isSoldOut ? 'is-sold-out' : ''}"
            data-product-card
            data-product-id="${item.id}"
            data-product-slug="${item.slug}"
            data-category-id="${item.categoryId}"
            data-category-slug="${item.categorySlug}"
            data-availability-status="${item.availability.status}"
            <c:if test="${isSoldOut}">data-sold-out="true"</c:if>
            data-product-name="${item.name}"
            data-product-desc="${item.description}"
            data-product-current-price="${item.resolvedPrice}"
            <c:if test="${hasPromotion}">
                data-product-original-price="${item.basePrice}"
            </c:if>
            data-product-image="${item.imageUrl}"
            data-has-options="${item.hasOptions}"
    >
        <div class="product-card__media"
             <c:if test="${isSoldOut}">style="position: relative"</c:if>>
            <img
                    src="${item.imageUrl}"
                    class="product-card__image"
                    alt="${item.name}"
            />
            <c:if test="${isSoldOut}">
                <div
                        class="product-card__overlay"
                        style="position: absolute; inset: 0; background: rgba(245, 245, 244, 0.7); display: flex; align-items: center; justify-content: center; font-weight: 600; color: #222;"
                >
                    Hết hàng
                </div>
            </c:if>
        </div>
        <div class="product-card__info">
            <div class="product-card__top">
                <p class="product-card__title mb-0">${item.name}</p>
            </div>
            <div class="product-card__price">
                <span class="product-card__price-current">
                    <fmt:formatNumber value="${item.resolvedPrice}" type="number"
                                      groupingUsed="true"/>đ
                </span>
                <c:if test="${hasPromotion}">
                    <span class="product-card__price-original">
                        <fmt:formatNumber value="${item.basePrice}" type="number"
                                          groupingUsed="true"/>đ
                    </span>
                </c:if>
            </div>
        </div>
        <c:if test="${hasPromotion && !isSoldOut}">
            <c:set var="discountAmount" value="${item.basePrice - item.resolvedPrice}"/>
            <c:set var="discountPercent" value="${(discountAmount * 100.0) / item.basePrice}"/>
            <div class="product-card__badge">
                <c:choose>
                    <%-- Prioritize amount display if >= 10K --%>
                    <c:when test="${discountAmount >= 10000}">
                        <p>Giảm <fmt:formatNumber value="${discountAmount / 1000}" maxFractionDigits="0"/>K</p>
                    </c:when>
                    <%-- Show percentage if >= 5% --%>
                    <c:when test="${discountPercent >= 5}">
                        <p>Giảm <fmt:formatNumber value="${discountPercent}" maxFractionDigits="0"/>%</p>
                    </c:when>
                    <%-- Fallback for small discounts --%>
                    <c:otherwise>
                        <p>Khuyến mãi</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
    </article>
</c:forEach>

<%-- Show empty state if no products --%>
<c:if test="${empty catalogItems}">
    <div class="col-12 text-center py-5">
        <p class="text-muted">Không có sản phẩm nào trong menu này</p>
    </div>
</c:if>
