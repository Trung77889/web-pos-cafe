<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<base href="${pageContext.request.contextPath}/">

<!DOCTYPE html>
<html lang="vi">
<head>
    <jsp:include page="head.jsp"/>
</head>
<body>
    <%-- Including the header component --%>
    <jsp:include page="header.jsp"/>

    <main class="container">
        <jsp:include page="${pageContent}"/>
    </main>

    <%-- Including the footer component --%>
    <jsp:include page="footer.jsp"/>

    <!-- Script -->
    <script src="assets/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="assets/js/main.js"></script>
</body>
</html>