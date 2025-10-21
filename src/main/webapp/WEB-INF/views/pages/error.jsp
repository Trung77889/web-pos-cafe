<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<base href="${pageContext.request.contextPath}/">

<%--
  Using Expression Language (EL) to fetch standard error attributes.
  These attributes are automatically set into the request scope by the Servlet Container (Tomcat)
  when an error occurs, and are only accessible if isErrorPage="true" is set.

  Standard Servlet Error Attributes:
  - jakarta.servlet.error.exception: The Throwable object (if any).
  - jakarta.servlet.error.status_code: The HTTP status code (404, 500, etc.).
  - jakarta.servlet.error.message: The error message.
  - jakarta.servlet.error.request_uri: The URI that caused the error.
--%>

<c:set var="statusCode" value="${requestScope['jakarta.servlet.error.status_code']}"/>
<c:set var="exception" value="${requestScope['jakarta.servlet.error.exception']}"/>
<c:set var="message" value="${requestScope['jakarta.servlet.error.message']}"/>
<c:set var="requestUri" value="${requestScope['jakarta.servlet.error.request_uri']}"/>

<%--
  Based on the statusCode, we set the presentation variables (errorTitle, errorSubtitle, errorIcon).
--%>
<c:choose>
    <c:when test="${statusCode == 404}">
        <c:set var="errorTitle" value="404 - Trang không tồn tại"/>
        <c:set var="errorSubtitle" value="Xin lỗi, trang bạn tìm không tồn tại hoặc đã bị di chuyển ☕"/>
        <c:set var="errorIcon" value="fi fi-rr-search"/>
    </c:when>
    <c:when test="${statusCode == 500}">
        <c:set var="errorTitle" value="500 - Lỗi hệ thống"/>
        <c:set var="errorSubtitle" value="Máy chủ đang gặp sự cố, nhóm kỹ thuật đang xử lý! ⚙️"/>
        <c:set var="errorIcon" value="fi fi-rr-bug"/>
    </c:when>
    <c:when test="${statusCode == 403}">
        <c:set var="errorTitle" value="403 - Truy cập bị từ chối"/>
        <c:set var="errorSubtitle" value="Bạn không có quyền truy cập vào trang này 🚫"/>
        <c:set var="errorIcon" value="fi fi-rr-lock"/>
    </c:when>
    <c:otherwise>
        <c:set var="errorTitle" value="${statusCode} - Lỗi không xác định"/>
        <c:set var="errorSubtitle" value="Đã xảy ra lỗi khi xử lý yêu cầu của bạn 🙈"/>
        <c:set var="errorIcon" value="fi fi-rr-warning"/>
    </c:otherwise>
</c:choose>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../layout/head.jsp"/>
</head>

<body>
    <%-- Including the header component --%>
    <jsp:include page="../layout/header.jsp"/>

    <main class="py-16">
        <div class="container">
            <div class="d-flex flex-column align-items-center justify-content-center text-center my-12">

                <%-- Displaying the generated error content --%>
                <i class="${errorIcon} text-primary fs-1 mb-4"></i>
                <h1 class="fw-bold mb-4 fs-2">${errorTitle}</h1>
                <p class="text-muted fs-5 mb-8">${errorSubtitle}</p>

                <div class="d-flex gap-3 justify-content-center mb-8">
                    <a href="../layout" class="btn btn-primary px-4">Về trang chủ</a>
                    <a href="javascript:history.back()" class="btn btn-outline-secondary px-4">Quay lại</a>
                </div>

                <!-- Debug mode -->
                <c:if test="${pageContext.servletContext.getInitParameter('debugMode') eq 'true'}">
                    <div class="alert alert-danger text-start">
                        <p><strong> Status Code: </strong> ${statusCode}</p>
                        <p><strong> Request URI: </strong> ${requestUri != null ? requestUri : 'Not Defined'}</p>
                        <p><strong> Message: </strong> ${message}</p>

                            <%-- Displaying the stack trace if an exception exists --%>
                        <c:if test="${not empty exception}">
                            <pre class="fs-6"> ${exception} </pre>
                        </c:if>
                    </div>
                </c:if>
            </div>
        </div>
    </main>

    <%-- Including the footer component --%>
    <jsp:include page="../layout/footer.jsp"/>

    <!-- Script -->
    <script src="./assets/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="./assets/js/main.js"></script>
</body>
</html>