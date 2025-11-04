<%--
  Description: Registration modal for customers (email, username, password)
  Author: Dang Van Trung
  LastModified: 31/10/2025
--%>

<%@ page contentType='text/html;charset=UTF-8' language='java' %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>

<div class='modal fade' id='registerModal' tabindex='-1' aria-labelledby='registerModalLabel' aria-hidden='true'>
    <div class='modal-dialog modal-sm modal-dialog-centered'>
        <div class='modal-content'>
            <div class='modal-body'>
                <div class='d-flex flex-column align-items-center'>
                    <h3 class='modal-title fw-bold text-center text-primary mb-3'>
                        ${form['form.title.register']}
                    </h3>
                    <p class='text-center text-muted mb-4 w-75'>
                        ${form['form.social.desc']}
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

                    <%-- Register form --%>
                    <form id='registerForm'
                          class='needs-validation w-100 mb-0'
                          novalidate
                          method='post'
                          action='auth/register'
                    >
                        <div class='form-floating mb-4'>
                            <input type='email'
                                   class='form-control <c:if test='${not empty sessionScope.formErrors.email}'>is-invalid</c:if>'
                                   id='registerEmail'
                                   name='email'
                                   value='${sessionScope.formData.email}'
                                   placeholder='Email'
                                   required
                                   autocomplete='email'
                            />
                            <label for='registerEmail'>${form['form.email']}</label>
                            <c:if test='${not empty sessionScope.formErrors.email}'>
                                <div class='invalid-feedback'>
                                        ${form[sessionScope.formErrors.email]}
                                </div>
                            </c:if>
                        </div>

                        <div class='form-floating mb-4'>
                            <input type='text'
                                   class='form-control <c:if test='${not empty sessionScope.formErrors.username}'>is-invalid</c:if>'
                                   id='registerUsername'
                                   name='username'
                                   value='${sessionScope.formData.username}'
                                   placeholder='${form['form.username']}'
                                   required/>
                            <label for='registerUsername'>${form['form.username']}</label>
                            <c:if test='${not empty sessionScope.formErrors.username}'>
                                <div class='invalid-feedback'>
                                        ${form[sessionScope.formErrors.username]}
                                </div>
                            </c:if>
                        </div>

                        <div class='form-floating mb-4'>
                            <input type='password'
                                   class='form-control <c:if test='${not empty sessionScope.formErrors.password}'>is-invalid</c:if>'
                                   id='registerPassword'
                                   name='password'
                                   placeholder='${form['form.password']}'
                                   required minlength='6'/>
                            <label for='registerPassword'>${form['form.password']}</label>
                            <c:if test='${not empty sessionScope.formErrors.password}'>
                                <div class='invalid-feedback'>
                                        ${form[sessionScope.formErrors.password]}
                                </div>
                            </c:if>
                            <div class="form-text">${form['form.password.error.invalidFormat']}</div>
                        </div>

                        <div class='form-check mb-6 text-start'>
                            <input type='checkbox'
                                   class='form-check-input <c:if test='${not empty sessionScope.formErrors.agreeTerms}'>is-invalid</c:if>'
                                   id='agreeTerms'
                                   name='agreeTerms'
                                   required/>
                            <label class='form-check-label' for='agreeTerms'>
                                ${form['form.agree']}
                                <a href='#' class='btn-link'>${form['form.term']}</a>
                                ${form['form.usage']}
                            </label>
                            <c:if test="${not empty sessionScope.formErrors.agreeTerms}">
                                <div class="invalid-feedback">${form[formErrors.agreeTerms]}</div>
                            </c:if>
                        </div>

                        <button type='submit' class='btn btn-lg btn-primary w-100'>
                            ${form['form.button.register']}
                        </button>
                    </form>

                    <p class='text-center text-muted mt-4'>
                        ${form['form.alreadyHasAnAccount']}
                        <button class='btn btn-link btn-open-modal p-0 align-baseline' data-type='login'>
                            ${form['form.button.login']}
                        </button>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>