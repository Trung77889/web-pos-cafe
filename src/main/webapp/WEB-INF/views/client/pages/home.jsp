<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<base href="${pageContext.request.contextPath}/">

<%-- ========= MAIN ========= --%>
<main class="page-main">
    <div class="page-container">
        <div class="row g-5">
            <div class="col-lg-8 col-12">
                <section class="product-section">
                    <div
                            class="d-flex align-items-center justify-content-end gap-4 flex-wrap"
                    >
                        <div class="search-field search-field--wide">
                            <input
                                    class="search-field__input"
                                    type="text"
                                    placeholder="Uống gì nè bạn?"
                            />
                            <span class="search-field__icon icon-base"
                            ><i class="fi fi-rr-search"></i
                            ></span>
                        </div>
                    </div>

                    <div
                            id="heroCarousel"
                            class="carousel slide hero"
                            data-bs-ride="carousel"
                            data-bs-interval="5000"
                    >
                        <div class="carousel-inner">
                            <div class="carousel-item active">
                                <img
                                        src="assets/client/img/banner/banner-2.png"
                                        class="d-block w-100"
                                        alt="Banner"
                                />
                            </div>
                            <div class="carousel-item">
                                <img
                                        src="assets/client/img/banner/banner-3.png"
                                        class="d-block w-100"
                                        alt="Banner"
                                />
                            </div>
                        </div>
                        <div class="carousel-indicators">
                            <button
                                    type="button"
                                    data-bs-target="#heroCarousel"
                                    data-bs-slide-to="0"
                                    class="active"
                                    aria-current="true"
                                    aria-label="Slide 1"
                            ></button>
                            <button
                                    type="button"
                                    data-bs-target="#heroCarousel"
                                    data-bs-slide-to="1"
                                    aria-label="Slide 2"
                            ></button>
                        </div>
                    </div>

                    <div class="category-slider" data-category-slider>
                        <button
                                class="category-nav category-nav--prev"
                                type="button"
                                data-category-prev
                        >
                            <span class="icon-base"
                            ><i class="fi fi-rr-angle-small-left"></i
                            ></span>
                        </button>
                        <button
                                class="category-nav category-nav--next"
                                type="button"
                                data-category-next
                        >
                            <span class="icon-base"
                            ><i class="fi fi-rr-angle-small-right"></i
                            ></span>
                        </button>
                        <div class="category-track scroll-hidden" data-category-track>
                            <c:forEach var="category" items="${categories}" varStatus="status">
                                <button
                                        class="category-item ${status.first ? 'is-active' : ''}"
                                        type="button"
                                        data-category-item
                                        data-category-slug="${category.slug}"
                                        data-category-name="${category.name}"
                                >
                                    <img
                                            class="category-image"
                                            src="${category.iconUrl}"
                                            alt="${category.name}"
                                    />
                                    <span class="category-label">${category.name}</span>
                                </button>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="section-header">
                        <h5 class="section-title mb-0" data-category-title>
                            Menu cà phê
                        </h5>
                        <span class="section-meta" data-category-meta>
                            <c:choose>
                                <c:when test="${fn:length(catalogItems) == 1}">1 sản phẩm</c:when>
                                <c:otherwise>${fn:length(catalogItems)} sản phẩm</c:otherwise>
                            </c:choose>
                        </span>
                    </div>

                    <div class="product-grid" data-product-grid>
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
                                <p class="text-muted">Không có sản phẩm nào trong menu</p>
                            </div>
                        </c:if>
                    </div>
                </section>
            </div>

            <!-- Sidebar Cart -->
            <div class="col-lg-4 col-12 d-none d-lg-block">
                <aside class="cart-panel">
                    <h5 class="cart-title mb-3">
                        Giỏ hàng
                        <span class="cart-count" data-cart-count>0</span>
                    </h5>
                    <div class="cart-list" data-cart-list>
                        <!-- Cart items loaded from localStorage -->
                    </div>
                    <div class="cart-summary">
                        <div class="cart-summary__row total">
                            <span>Tổng cộng</span>
                            <span data-cart-total>0 Đ</span>
                        </div>
                    </div>
                    <button
                            class="btn btn-lg btn-primary--filled w-100"
                            data-view-cart
                    >
                        Xem giỏ hàng
                    </button>
                </aside>
            </div>
        </div>
    </div>
</main>

<!-- Mobile Bottom Navigation -->
<nav class="bottom-nav d-lg-none">
    <a href="#" class="bottom-nav__item active">
        <span class="bottom-nav__icon"><i class="fi fi-rr-home"></i></span>
        <span class="bottom-nav__label">Trang chủ</span>
    </a>
    <a href="#" class="bottom-nav__item">
        <span class="bottom-nav__icon"><i class="fi fi-rr-search"></i></span>
        <span class="bottom-nav__label">Tìm kiếm</span>
    </a>
    <a
            href="#"
            class="bottom-nav__item bottom-nav__item--cart"
            data-cart-toggle
    >
        <span class="bottom-nav__icon">
            <i class="fi fi-rr-shopping-cart"></i>
            <span class="bottom-nav__badge" data-cart-count>0</span>
        </span>
        <span class="bottom-nav__label">Giỏ hàng</span>
    </a>
    <a href="#" class="bottom-nav__item">
        <span class="bottom-nav__icon"><i class="fi fi-rr-user"></i></span>
        <span class="bottom-nav__label">Tài khoản</span>
    </a>
</nav>

<!-- Mobile Cart Drawer -->
<div class="mobile-cart-drawer" data-mobile-cart>
    <div class="mobile-cart-drawer__backdrop" data-cart-close></div>
    <div class="mobile-cart-drawer__panel">
        <div class="mobile-cart-drawer__header">
            <h5 class="mb-0">Giỏ hàng
                <span class="cart-count"></span>
            </h5>
            <button class="btn-close" data-cart-close></button>
        </div>
        <div class="mobile-cart-drawer__body">
            <div class="cart-list" data-cart-list>
                <!-- Cart items synced from localStorage -->
            </div>
        </div>
        <div class="mobile-cart-drawer__footer">
            <div class="cart-summary">
                <div class="cart-summary__row total">
                    <span>Tổng cộng</span>
                    <span data-cart-total>0 Đ</span>
                </div>
            </div>
            <button class="btn btn-lg btn-primary--filled w-100" data-view-cart>
                Xem giỏ hàng
            </button>
        </div>
    </div>
</div>