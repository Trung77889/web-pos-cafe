# ☕ Zero Star Coffee – Java Web App (Servlet + JSP + MySQL)

Zero Star Coffee is a modular Java web application built with **Jakarta Servlet/JSP**, **Tomcat 11**, and **MySQL**.
It powers the internal system for Zero Star Coffee — a learning project focusing on full-stack web development,
scalability, and reusable UI layout.

---

## 🏗️ Project Overview

| Component              | Description                                        |
|------------------------|----------------------------------------------------|
| **Language**           | Java 21+                                           |
| **Web Container**      | Apache Tomcat 11 (Jakarta EE 10)                   |
| **Database**           | MySQL 8.x                                          |
| **Connection Pooling** | Apache Commons DBCP 2 (via Tomcat JNDI DataSource) |
| **Logging**            | Logback with daily rolling file logs               |
| **Frontend Tools**     | Bootstrap 5, SCSS, and JSP layout includes         |

---

## ⚙️ Local Development Setup

### 1️⃣ Install Requirements

| Component             | Version      | Notes                                 |
|-----------------------|--------------|---------------------------------------|
| **JDK**               | 21 or higher | Ensure `JAVA_HOME` is set             |
| **Apache Tomcat**     | 11.x         | `CATALINA_HOME` must be configured    |
| **MySQL**             | 8.x          | Create database: `zerostar_cf`        |
| **MySQL Connector/J** | 8.x          | Copy `.jar` into `$CATALINA_HOME/lib` |
| **XAMPP (optional)**  | 8.x          | For using MySQL with PhpMyAdmin GUI   |

---

### 2️⃣ Compiling SCSS → CSS

From the project root:

```bash
cd src/main/webapp/assets/styles
sass ./base.scss ./base.css -w -q
```

This automatically compiles your main SCSS file into `/assets/styles/app.css`.

---

## 🧱 Project Structure

```
src/main/java/com.laptrinhweb.zerostarcafe/
├── core/          → DB, security, utilities, validation
├── domain/        → Business logic (auth, user, etc.)
└── web/
    ├── controllers/ → Servlets (auth, home, error)
    └── filters/     → Locale, Flash, Logging, Error filters
```

---

## 🧩 JSP Layout System

Zero Star Coffee uses a **universal layout** pattern that centralizes your HTML head, header, footer, and scripts in one
file.
****

### 🧠 `main-layout.jsp`

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<base href="${pageContext.request.contextPath}/">

<!DOCTYPE html>
<html lang="vi">
<head>
    <jsp:include page="head.jsp"/>
</head>
<body class="scroll-hidden">
    <%-- Including the header component --%>
    <jsp:include page="header.jsp"/>

    <%--  Main content  --%>
    <jsp:include page="${pageContent}"/>

    <%-- Including the footer component --%>
    <jsp:include page="footer.jsp"/>

    <%-- Flash message --%>
    <c:if test="${not empty requestScope.messages}">
        <div class="toast-container" hidden>
            <c:forEach var="msg" items="${messages}">
                <p data-type="${msg.type}" data-message="${i18n.trans(msg.msgKey)}"></p>
            </c:forEach>
        </div>
    </c:if>

    <%-- Script --%>
    <script>
        window.__APP_MODE__ = "${initParam.APP_MODE}";
    </script>
    <script src="assets/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script type="module" src="assets/js/base.js"></script>
</body>
</html>
```

Each page (like `home.jsp` or `error.jsp`) provides only the **page content**.
The servlet dynamically decides which page to include.

---

### 🧭 Example: HomeServlet.java

```java

@WebServlet(name = "HomeRedirectServlet", urlPatterns = "/home")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("pageTitle", "general.page.home");
        req.setAttribute("pageContent", PathUtil.Pages.HOME);
        req.getRequestDispatcher(PathUtil.Layout.MAIN).forward(req, resp);
    }
}
```

---

## Working with Static Resources

Because the `<base>` tag is declared **once** in `main-layout.jsp`:

```jsp
<base href="${pageContext.request.contextPath}/">
```

You can safely use **relative paths** everywhere in JSP:

```html
<!-- Correct usage -->
<link rel="stylesheet" href="assets/styles/app.css">
<script src="assets/js/base.js"></script>
<img src="assets/img/banner.png" alt="Banner">

<!-- Internal page link -->
<a href="./">Home</a>
<a href="logout">Logout</a>
```

💡 No need for `${pageContext.request.contextPath}` on every link.
Avoid `href="/assets/...` because it skips the context path when deployed under `/zero_star_cafe`.

---

**App URL:**
`http://localhost:8080/zero_star_cafe/`
