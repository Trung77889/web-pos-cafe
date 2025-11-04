<%--
  Description: login form with (username, password)
  Author: Dang Van Trung
  Date: 22/10/2025
--%>
<%@ page contentType='text/html;charset=UTF-8' language='java' %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>

<div class='modal fade' id='loginModal' tabindex='-1' aria-labelledby='loginModal' aria-hidden='true'>
    <div class='modal-dialog modal-sm modal-dialog-centered'>
        <div class='modal-content'>
            <div class='modal-body'>
                <div class='d-flex flex-column align-items-center'>
                    <h3 class='modal-title fw-bold text-center text-primary mb-3'>
                        ${form['form.title.login']}
                    </h3>
                    <p class='text-center text-muted mb-4 w-75'>
                        ${form['form.login.desc']}
                    </p>

                    <%-- Social buttons --%>
                    <div class='d-flex gap-4 w-100'>
                        <a href='#' class='btn btn-lg btn-outline-primary w-50'>
                            <img src='assets/img/icons/google.svg' alt='Google' class='me-2'/>
                            Google
                        </a>
                        <a href='#' class='btn btn-lg btn-outline-primary w-50'>
                            <img src='assets/img/icons/zalo.svg' alt='Zalo' class='me-2'/>
                            Zalo
                        </a>
                    </div>

                    <div class='text-center divider text-muted'>
                        <p>${form['form.or']}</p>
                    </div>

                    <%-- Login Form --%>
                    <form
                            id='loginForm'
                            class='needs-validation w-100 mb-0'
                            novalidate
                            method='post'
                            action='login'>

                        <div class='form-floating mb-4'>
                            <input type='text'
                                   class='form-control '
                                   id='loginUsername'
                                   name='username'
                                   placeholder='Tên đăng nhập'
                                   required/>
                            <label for='loginUsername'>
                                ${form['form.username']}
                            </label>
                        </div>

                        <div class='form-floating mb-4'>
                            <input type='password'
                                   class='form-control '
                                   id='loginPassword'
                                   name='password'
                                   placeholder='Mật khẩu'
                                   required/>
                            <label for='loginPassword'>
                                ${form['form.password']}
                            </label>
                        </div>

                        <div class='form-check mb-6'>
                            <input type='checkbox'
                                   class='form-check-input'
                                   id='rememberMe'
                                   required/>
                            <label class='form-check-label' for='rememberMe'>
                                ${form['form.rememberMe']}
                            </label>
                        </div>

                        <button type='submit' class='btn btn-lg btn-primary w-100'>
                            ${form['form.button.login']}
                        </button>
                    </form>

                    <p class='text-center text-muted mt-6 d-flex gap-2 justify-content-center align-items-center'>
                        ${form['form.dontHaveAnAccount']}
                        <button
                                type='button'
                                class='btn btn-link btn-open-modal p-0'
                                data-type='register'>
                            ${form['form.button.register']}
                        </button>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>
