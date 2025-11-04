<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- ========= HEADER ========= --%>
<header class="header">
    <div class="container header-main">
        <nav class="navbar navbar-expand-lg">
            <div class="container-fluid">
                <%-- Brand name --%>
                <a class="navbar-brand fw-semibold text-primary" href="./">
                    Zero Star Coffee
                </a>

                <%-- Menu toggle --%>
                <button
                        class="navbar-toggler"
                        type="button"
                        data-bs-toggle="collapse"
                        data-bs-target="#navbarNavDropdown"
                        aria-controls="navbarNavDropdown"
                        aria-expanded="false"
                        aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <%-- Navigation --%>
                <div class="collapse navbar-collapse" id="navbarNavDropdown">
                    <ul class="navbar-nav w-100 justify-content-md-center align-items-md-center">
                        <li class="nav-item ms-md-auto">
                            <a class="nav-link active" aria-current="page" href="#">
                                ${general["general.menu"]}
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#"> ${general["general.bookingSeat"]} </a>
                        </li>

                        <c:choose>
                            <c:when test="${empty sessionScope.user}">
                                <li class="nav-item ms-md-auto">
                                    <button
                                            type="button"
                                            class="btn btn-primary btn-open-modal"
                                            data-type="login">
                                            ${general["general.login"]}
                                    </button>
                                </li>
                            </c:when>

                            <c:otherwise>
                                <li class="nav-item ms-md-auto dropdown">
                                    <a class="nav-link dropdown-toggle" href="#" role="button"
                                            data-bs-toggle="dropdown" aria-expanded="false">
                                        <div class="user-tool d-inline-flex align-items-center">
                                            <div class="user-info d-inline-flex flex-column me-4">
                                                <span class="user-name text-black fw-semibold">${sessionScope.user.username}</span>
                                                <span class="user-membership">
                                                    1,200 điểm
                                                </span>
                                            </div>
                                            <img
                                                    src="https://images.unsplash.com/photo-1631947430066-48c30d57b943?auto=format&fit=crop&q=80&w=832"
                                                    alt="User Image"
                                                    class="user-avatar object-fit-cover rounded-circle"
                                            />
                                        </div>
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li><a class="dropdown-item" href="#">Lịch sử đơn hàng</a></li>
                                        <li><a class="dropdown-item" href="#">Đặt bàn</a></li>
                                        <li>
                                            <hr class="dropdown-divider"/>
                                        </li>
                                        <li>
                                            <a class="dropdown-item text-danger"
                                                    href="./logout">
                                                    ${general["general.logout"]}
                                            </a>
                                        </li>
                                    </ul>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>
            </div>
        </nav>
    </div>
</header>
