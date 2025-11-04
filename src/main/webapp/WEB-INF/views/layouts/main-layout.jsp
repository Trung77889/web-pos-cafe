<%@ page contentType='text/html; charset=UTF-8' pageEncoding='UTF-8' %>
<base href='${pageContext.request.contextPath}/'>

<!DOCTYPE html>
<html lang='vi'>
<head>
    <%@include file='head.jsp' %>
</head>
<body class='scroll-hidden'>
    <%-- Including the header component --%>
    <%@include file='header.jsp' %>

    <%--  Main content  --%>
    <main class='container'>
        <jsp:include page='${pageContent}'/>
    </main>

    <%-- Including the footer component --%>
    <%@include file='footer.jsp' %>

    <%-- Flash message --%>
    <c:if test='${not empty sessionScope.flash}'>
        <script>
            window.__FLASH__ = {
                type: '${sessionScope.flash.type}',
                msg: '${message[sessionScope.flash.msg]}'
            };
        </script>
        <c:remove var='flash' scope='session'/>
    </c:if>

    <%-- Re-open modal --%>
    <c:if test='${not empty sessionScope.openModal}'>
        <script>
            window.__OPEN_MODAL__ = '${sessionScope.openModal}';
        </script>
        <c:remove var='openModal' scope='session'/>
    </c:if>

    <%-- Script --%>
    <script>
        window.__APP_MODE__ = '${initParam.APP_MODE}';
    </script>
    <script src='assets/bootstrap/js/bootstrap.bundle.min.js'></script>
    <script type='module' src='assets/js/main.js'></script>
</body>
</html>