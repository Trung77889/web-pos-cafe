<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<base href="${pageContext.request.contextPath}/">

<!-- ============ MODAL START ============ -->
<!-- 1. Login modal -->
<div
        class="modal fade"
        id="loginModal"
        tabindex="-1"
        aria-labelledby="loginModalLabel"
        aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-body">
                <div class="d-flex flex-column align-items-center">
                    <h3 class="modal-title fw-bold text-center text-primary mb-3" id="exampleModalLabel">
                        Chào mừng trở lại
                    </h3>
                    <p class="text-center text-muted mb-4">Tận hưởng dịch vụ với nhiều ưu đãi</p>

                    <!-- Social login buttons -->
                    <div class="d-flex gap-4 w-100">
                        <a href="#" class="btn btn-lg btn-outline-primary w-100">
                            <img src="./assets/img/icons/google.svg" alt="Google Icon" class="me-2"/>
                            Google
                        </a>
                        <a href="#" class="btn btn-lg btn-outline-primary w-100">
                            <img src="./assets/img/icons/zalo.svg" alt="Google Icon" class="me-2"/>
                            Zalo
                        </a>
                    </div>

                    <div class="text-center divider text-muted">
                        <p>Hoặc</p>
                    </div>

                    <!-- Login form -->
                    <form action="login" method="POST" class="w-100">
                        <div class="form-floating mb-4">
                            <input
                                    type="text"
                                    class="form-control"
                                    id="floatingUsername"
                                    name="username"
                                    placeholder="Haianh101"/>
                            <label for="floatingInput">Tên đăng nhập</label>
                        </div>
                        <div class="form-floating mb-4">
                            <input
                                    type="password"
                                    class="form-control"
                                    id="floatingPassword"
                                    name="password"
                                    placeholder="Mật khẩu"/>
                            <label for="floatingPassword">Mật khẩu</label>
                        </div>
                        <div class="mb-4 form-check">
                            <input type="checkbox" class="form-check-input" id="remember"/>
                            <label class="form-check-label" for="remember">Lưu đăng nhập</label>
                        </div>
                        <button type="submit" class="btn btn-lg btn-primary w-100 mt-2">Đăng nhập</button>
                        <button
                                type="button"
                                class="btn btn-lg btn-link w-100 mt-2"
                                data-bs-toggle="modal"
                                data-bs-target="#forgotPasswordModal">
                            Quên mật khẩu
                        </button>
                    </form>

                    <!-- Register link -->
                    <p class="text-muted text-center m-0 mt-3">
                        Chưa có tài khoản ?
                        <button
                                type="button"
                                class="btn btn-link"
                                data-bs-toggle="modal"
                                data-bs-target="#registerModal">
                            Đăng ký
                        </button>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 2. Register modal -->
<div
        class="modal fade"
        id="registerModal"
        tabindex="-1"
        aria-labelledby="registerModalLabel"
        aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-body">
                <div class="d-flex flex-column align-items-center">
                    <h3 class="modal-title fw-bold text-center text-primary mb-3" id="exampleModalLabel">
                        Tạo tài khoản
                    </h3>
                    <p class="text-center text-muted mb-4">Đăng ký bằng tài khoản mạng xã hội</p>

                    <!-- Social register buttons -->
                    <div class="d-flex gap-4 w-100">
                        <a href="#" class="btn btn-lg btn-outline-primary w-100">
                            <img src="./assets/img/icons/google.svg" alt="Google Icon" class="me-2"/>
                            Google
                        </a>
                        <a href="#" class="btn btn-lg btn-outline-primary w-100">
                            <img src="./assets/img/icons/zalo.svg" alt="Google Icon" class="me-2"/>
                            Zalo
                        </a>
                    </div>

                    <div class="text-center divider text-muted">
                        <p>Hoặc</p>
                    </div>

                    <!-- Register form -->
                    <form action="register" method="POST" class="w-100">
                        <div class="form-floating mb-4">
                            <input
                                    type="text"
                                    class="form-control"
                                    id="floatingFullname"
                                    name="fullname"
                                    placeholder="Nguyễn Hải Anh"/>
                            <label for="floatingInput">Họ và tên</label>
                        </div>

                        <div class="form-floating mb-4">
                            <input
                                    type="email"
                                    class="form-control"
                                    id="floatingEmail"
                                    name="email"
                                    placeholder="Địa chỉ email"/>
                            <label for="floatingInput">Email</label>
                        </div>

                        <div class="form-floating mb-4">
                            <input
                                    type="text"
                                    name="username"
                                    class="form-control"
                                    id="floatingUsername"
                                    placeholder="Haianh101"/>
                            <label for="floatingInput">Tên đăng nhập</label>
                        </div>
                        <!-- <div id="passwordHelpBlock" class="form-text mb-4 ml-2 text-muted">
                          Tên đăng nhập từ 6-20 ký tự, không bao gồm ký tự đặc biệt
                        </div> -->

                        <div class="form-floating mb-2">
                            <input
                                    type="password"
                                    class="form-control"
                                    id="floatingPassword"
                                    name="password"
                                    placeholder="Mật khẩu"
                                    aria-describedby="passwordHelpBlock"/>
                            <label for="floatingPassword">Mật khẩu</label>
                        </div>
                        <div id="passwordHelpBlock" class="form-text mb-4 ml-2 text-muted">
                            Mật khẩu tổi thiểu 6 ký tự, gồm chữ cái số và ký tự đặc biệt !@#$...
                        </div>

                        <div class="mb-3 form-check">
                            <input type="checkbox" class="form-check-input" required id="agree-to-policy"/>
                            <label class="form-check-label" for="agree-to-policy">
                                Đồng ý với điều khoản sử dụng
                            </label>
                        </div>
                        <button type="submit" class="btn btn-lg btn-primary w-100 mt-4">Đăng ký</button>
                    </form>

                    <!-- Login link -->
                    <p class="text-muted text-center m-0 mt-3">
                        Đã có tài khoản ?
                        <button
                                type="button"
                                class="btn btn-link"
                                data-bs-toggle="modal"
                                data-bs-target="#loginModal">
                            Đăng nhập
                        </button>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 3. Forgot password modal -->
<div
        class="modal fade"
        id="forgotPasswordModal"
        tabindex="-1"
        aria-labelledby="forgotPasswordModalLabel"
        aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-body">
                <div class="d-flex flex-column align-items-center">
                    <h3 class="modal-title fw-bold text-center text-primary mb-3" id="exampleModalLabel">
                        Quên mật khẩu ?
                    </h3>
                    <p class="text-center text-muted mb-4">
                        Một email khôi phục sẽ được gửi về địa chỉ email đăng ký tài khoản
                    </p>

                    <!-- Forgot password form -->
                    <form action="forgot-password" method="POST" class="w-100">
                        <div class="form-floating mb-4">
                            <input
                                    type="email"
                                    class="form-control"
                                    id="floatingEmail"
                                    placeholder="Địa chỉ email"/>
                            <label for="floatingInput">Địa chỉ email</label>
                        </div>

                        <button
                                type="button"
                                class="btn btn-lg btn-primary w-100 mt-2"
                                data-bs-toggle="modal"
                                data-bs-target="#resetPasswordModal">
                            Gửi lại mật khẩu
                        </button>
                    </form>

                    <!-- Register link -->
                    <p class="text-muted text-center m-0 mt-3">
                        Quay lại
                        <button
                                type="button"
                                class="btn btn-link"
                                data-bs-toggle="modal"
                                data-bs-target="#loginModal">
                            Đăng nhập
                        </button>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 4. Reset password modal -->
<div
        class="modal fade"
        id="resetPasswordModal"
        tabindex="-1"
        aria-labelledby="resetPasswordModalLabel"
        aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-body">
                <div class="d-flex flex-column align-items-center">
                    <h3 class="modal-title fw-bold text-center text-primary mb-3" id="exampleModalLabel">
                        Đặt lại mật khẩu
                    </h3>

                    <!-- Forgot password form -->
                    <form action="forgot-password" method="POST" class="w-100">
                        <div class="form-floating mb-4">
                            <input
                                    type="password"
                                    class="form-control"
                                    id="floatingPassword"
                                    placeholder="Mật khẩu mới"/>
                            <label for="floatingInput">Mật khẩu mới</label>
                        </div>
                        <div class="form-floating mb-4">
                            <input
                                    type="password"
                                    class="form-control"
                                    id="floatingPasswordConfirm"
                                    placeholder="Nhập lại mật khẩu"/>
                            <label for="floatingInput">Nhập lại mật khẩu</label>
                        </div>

                        <button type="submit" class="btn btn-lg btn-primary w-100 mt-2">
                            Đặt lại mật khẩu
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 5. Product details modal -->
<div
        class="modal fade"
        id="productDetailModal"
        tabindex="-1"
        aria-labelledby="productDetailModalLabel"
        aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-body">
                <!-- Product Info -->
                <div class="product-info d-flex gap-4 mb-4">
                    <div class="product-info__image">
                        <img src="assets/img/product/soda-dao.png" alt="Soda Đào"/>
                    </div>
                    <div class="product-info__details">
                        <h4 class="mb-2">Soda Đào</h4>
                        <div class="mb-2">
                            <span class="fs-4 fw-bold text-primary">18,000đ</span>
                            <span class="text-decoration-line-through text-muted ms-2">23,000đ</span>
                        </div>
                        <p class="text-muted small text-truncate-4">
                            Aquafina soda kèm với mứt đào ngon ngọt, cảm giác mát lạnh Aquafina soda kèm với
                            mứt đào ngon ngọt, cảm giác mát lạnh Aquafina soda kèm với mứt đào ngon ngọt, cảm
                            giác mát lạnh Aquafina soda kèm với mứt đào ngon ngọt, cảm giác mát lạnh Aquafina
                            soda kèm với mứt đào ngon ngọt, cảm giác mát lạnh Aquafina soda kèm với mứt đào
                            ngon ngọt, cảm giác mát lạnh
                        </p>
                        <div class="quantity-control d-inline-flex align-items-center">
                            <button type="button" class="product-qty-btn minus">
                                <i class="icon-base fi fi-rr-minus"></i>
                            </button>
                            <input
                                    type="number"
                                    class="product-qty fs-4"
                                    value="1"
                                    min="1"
                                    name="product-qty"
                                    readonly/>
                            <button type="button" class="product-qty-btn plus">
                                <i class="icon-base fi fi-rr-plus"></i>
                            </button>
                        </div>
                    </div>
                </div>

                <!-- Options Section - Scrollable -->
                <div class="options-wrapper">
                    <!-- Size Options -->
                    <div class="mb-4">
                        <h6 class="fw-bold mb-3">Chọn Size</h6>
                        <div class="product-options">
                            <div class="form-check checked">
                                <input class="form-check-input" type="radio" name="size" id="sizeM" checked/>
                                <label class="form-check-label" for="sizeM">Size M</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="size" id="sizeL"/>
                                <label class="form-check-label" for="sizeL">
                                    Size L
                                    <span class="product-extra-price">(+5,000đ)</span>
                                </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="size" id="sizeXL"/>
                                <label class="form-check-label" for="sizeXL">
                                    Size XL
                                    <span class="product-extra-price">(+10,000đ)</span>
                                </label>
                            </div>
                        </div>
                    </div>

                    <!-- Ice Options -->
                    <div class="mb-4">
                        <h6 class="fw-bold mb-3">Mức Đá</h6>
                        <div class="product-options">
                            <div class="form-check checked">
                                <input
                                        class="form-check-input"
                                        type="radio"
                                        name="ice"
                                        id="iceNormal"
                                        checked/>
                                <label class="form-check-label" for="iceNormal">Bình thường</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="ice" id="iceLess"/>
                                <label class="form-check-label" for="iceLess">Ít đá</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="ice" id="iceNo"/>
                                <label class="form-check-label" for="iceNo">Không đá</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="ice" id="iceSeparate"/>
                                <label class="form-check-label" for="iceSeparate">Đá riêng</label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-lg flex-fill btn-primary add-to-cart">
                    Thêm vào giỏ -
                    <span class="product-price">36,000 đ</span>
                </button>
            </div>
        </div>
    </div>
</div>
<!-- ============ MODAL END ============ -->