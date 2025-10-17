<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"
            name="viewport"/>
    <meta content="ie=edge" http-equiv="X-UA-Compatible"/>
    <title>Zero Star Coffee</title>

    <!-- Google Font -->
    <link href="https://fonts.googleapis.com" rel="preconnect"/>
    <link crossorigin href="https://fonts.gstatic.com" rel="preconnect"/>
    <link href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap"
            rel="stylesheet"/>

    <!-- Flat icon -->
    <link rel="stylesheet"
            href="https://cdn-uicons.flaticon.com/3.0.0/uicons-regular-rounded/css/uicons-regular-rounded.css"/>

    <!--  Style  -->
    <link href="assets/styles/app.css" rel="stylesheet"/>
</head>
<body>
    <%@ include file="./views/partials/header.jsp" %>

    <!-- ============ MAIN START ============ -->
    <main class="bg-body py-12">
        <div class="container">
            <div class="row g-5">
                <!-- Left: Menu & Order -->
                <div class="col-lg-8">
                    <!-- Bàn & Thời gian -->
                    <div class="d-flex align-items-center mb-3">
                        <!-- <div
                                  class="bg-primary d-flex flex-column align-items-center rounded-4 px-3 py-2 me-3 text-white fw-bold"
                               >
                                  <span>Còn lại</span>
                                  <span>04:39:01</span>
                               </div>
                               <div class="d-flex flex-column gap-1">
                                  <span class="fw-semibold">Bàn Số 8</span>
                                  <span class="text-muted">
                                     Cố lên bạn ơi, sắp xong rồi!
                                  </span>
                               </div> -->

                        <!-- Search box -->
                        <div class="ms-auto w-50 d-flex align-items-center gap-3">
                            <input
                                    type="text"
                                    class="form-control"
                                    placeholder="Uống gì nè bạn ơi?"
                                    name="search-box"/>
                            <button class="btn btn-primary">
                                <i class="icon-base fi fi-rr-search"></i>
                            </button>
                        </div>
                    </div>

                    <!-- Banner -->
                    <div
                            id="bannerCarousel"
                            class="carousel slide mb-3"
                            data-bs-ride="carousel"
                            data-bs-touch="true">
                        <div class="carousel-inner">
                            <div class="carousel-item active">
                                <img src="assets/img/category/banner.png" class="d-block w-100" alt="..."/>
                            </div>
                            <div class="carousel-item">
                                <img src="assets/img/category/banner-2.png" class="d-block w-100" alt="..."/>
                            </div>
                            <div class="carousel-item">
                                <img src="assets/img/category/banner-3.png" class="d-block w-100" alt="..."/>
                            </div>
                        </div>

                        <button
                                class="carousel-control-prev"
                                type="button"
                                data-bs-target="#bannerCarousel"
                                data-bs-slide="prev">
                            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Previous</span>
                        </button>
                        <button
                                class="carousel-control-next"
                                type="button"
                                data-bs-target="#bannerCarousel"
                                data-bs-slide="next">
                            <span class="carousel-control-next-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Next</span>
                        </button>
                    </div>

                    <!-- Categories -->
                    <div class="category-wrapper position-relative">
                        <button class="category-nav-btn prev" id="prevCategory">
                            <i class="icon-sm fi fi-rr-angle-left"></i>
                        </button>

                        <div class="category-container">
                            <ul class="category-list">
                                <li class="category-chip active">
                                    <img
                                            src="assets/img/category/coffee.png"
                                            alt="Cà phê"
                                            class="category-image"/>
                                    <span class="category-name">Cà phê</span>
                                </li>
                                <li class="category-chip">
                                    <img
                                            src="assets/img/category/coffee.png"
                                            alt="Cà phê"
                                            class="category-image"/>
                                    <span class="category-name">Cà phê</span>
                                </li>
                                <li class="category-chip">
                                    <img
                                            src="assets/img/category/coffee.png"
                                            alt="Cà phê"
                                            class="category-image"/>
                                    <span class="category-name">Cà phê</span>
                                </li>
                                <li class="category-chip">
                                    <img
                                            src="assets/img/category/coffee.png"
                                            alt="Cà phê"
                                            class="category-image"/>
                                    <span class="category-name">Cà phê</span>
                                </li>
                                <li class="category-chip">
                                    <img
                                            src="assets/img/category/coffee.png"
                                            alt="Cà phê"
                                            class="category-image"/>
                                    <span class="category-name">Cà phê</span>
                                </li>
                                <li class="category-chip">
                                    <img
                                            src="assets/img/category/coffee.png"
                                            alt="Cà phê"
                                            class="category-image"/>
                                    <span class="category-name">Cà phê</span>
                                </li>
                                <li class="category-chip">
                                    <img
                                            src="assets/img/category/coffee.png"
                                            alt="Cà phê"
                                            class="category-image"/>
                                    <span class="category-name">Cà phê</span>
                                </li>
                                <li class="category-chip">
                                    <img
                                            src="assets/img/category/coffee.png"
                                            alt="Cà phê"
                                            class="category-image"/>
                                    <span class="category-name">Cà phê</span>
                                </li>
                                <li class="category-chip">
                                    <img
                                            src="assets/img/category/coffee.png"
                                            alt="Cà phê"
                                            class="category-image"/>
                                    <span class="category-name">Cà phê</span>
                                </li>
                                <li class="category-chip">
                                    <img
                                            src="assets/img/category/coffee.png"
                                            alt="Cà phê"
                                            class="category-image"/>
                                    <span class="category-name">Cà phê</span>
                                </li>
                            </ul>
                        </div>

                        <button class="category-nav-btn next" id="nextCategory">
                            <i class="icon-sm fi fi-rr-angle-right"></i>
                        </button>
                    </div>

                    <!-- Coffee Menu -->
                    <h4 class="fw-semibold mt-12 mb-5">Danh sách món</h4>
                    <div class="row row-cols-3 g-4">
                        <div class="col">
                            <!-- Product card -->
                            <div class="card" data-bs-toggle="modal" data-bs-target="#productDetailModal">
                                <img
                                        src="assets/img/product/soda-dao.png"
                                        class="card-img-top"
                                        alt="Soda Đào"/>
                                <div class="card-body">
                                    <h5 class="card-title mb-1">Soda Đào</h5>
                                    <p class="card-text small text-muted mb-2">
                                        Aquafina soda kèm với mứt đào ngon ngọt, cảm giác mát lạnh
                                    </p>
                                    <div>
                                        <span class="fw-bold fs-3 text-primary">18,000đ</span>
                                        <span class="text-decoration-line-through fs-5 fw-semibold text-muted ms-2">
                                            23,000đ
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Right: Cart -->
                <div class="col-lg-4">
                    <div class="bg-white rounded-5 shadow-sm p-4">
                        <h4 class="fw-bold mb-4">Giỏ Hàng</h4>
                    </div>
                </div>
            </div>
        </div>
    </main>
    <!-- ============ MAIN END ============ -->

    <%@ include file="./views/partials/footer.jsp" %>

    <!-- Script -->
    <script src="./assets/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="./assets/js/main.js"></script>
</body>
</html>