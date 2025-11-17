<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<base href="${pageContext.request.contextPath}/">

<!DOCTYPE html>
<html lang="vi">
<head>
    <jsp:include page="/WEB-INF/views/shared/head.jsp"/>

    <%-- Style --%>
    <link href="assets/client/styles/app.css" rel="stylesheet"/>
</head>
<body class="scroll-hidden">
    <%-- Including the header component --%>
    <jsp:include page="/WEB-INF/views/shared/header.jsp"/>

    <%--  Main content  --%>
    <jsp:include page="${requestScope.pageContent}"/>

    <%-- Including the footer component --%>
    <jsp:include page="/WEB-INF/views/shared/footer.jsp"/>

    <%-- Flash message --%>
    <c:if test="${not empty requestScope.messages}">
        <div class="toast-container" hidden>
            <c:forEach var="msg" items="${requestScope.messages}">
                <p data-type="${msg.type}"
                   data-message="${sessionScope.i18n.trans(msg.msgKey)}">
                </p>
            </c:forEach>
        </div>
    </c:if>

    <%-- Script --%>
    <script>
        window.__APP_MODE__ = "${initParam.APP_MODE}";
    </script>
    <script src="assets/shared/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script type="module" src="assets/shared/js/base.js"></script>
</body>
</html>