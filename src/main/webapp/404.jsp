<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"
            name="viewport"/>
    <meta content="ie=edge" http-equiv="X-UA-Compatible"/>
    <title>Zero Star Coffee - 404 NOT FOUND</title>

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
            <h1>404 - NOT FOUND PAGE</h1>
            <h4 class="text-danger">Details: ${error} </h4>
        </div>
    </main>
    <!-- ============ MAIN END ============ -->

    <%@ include file="./views/partials/footer.jsp" %>

    <!-- Script -->
    <script src="./assets/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="./assets/js/main.js"></script>
</body>
</html>