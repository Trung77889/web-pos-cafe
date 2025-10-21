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

### 2️⃣ Database Connection Check

Visit:
👉 **[http://localhost:8080/zero_star_cafe/health](http://localhost:8080/zero_star_cafe/health)**

You should see a “✅ Database connection established” message confirming that:

* JDBC driver is available
* JNDI DataSource is configured
* MySQL is reachable

---

### 3️⃣ Compiling SCSS → CSS

From the project root:

```bash
cd src/main/webapp/assets/styles
sass ./app.scss ./app.css -w -q
```

This automatically compiles your main SCSS file into `/assets/styles/app.css`.

---

## 🧱 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com.laptrinhweb.zerostarcafe/
│   │       ├── controller/
│   │       │   ├── HomeServlet.java
│   │       │   ├── LoginServlet.java
│   │       │   ├── LogoutServlet.java
│   │       │   └── RegisterServlet.java
│   │       ├── dao/
│   │       ├── model/
│   │       └── utils/
│   │           ├── AppLogger.java
│   │           ├── DBConnection.java
│   │           └── HealthCheckServlet.java
│   │
│   ├── resources/
│   │
│   └── webapp/
│       ├── assets/
│       │   ├── styles/
│       │   ├── js/
│       │   └── img/
│       ├── WEB-INF/
│       │   ├── views/
│       │   │   ├── layout/
│       │   │   │   ├── head.jsp
│       │   │   │   ├── header.jsp
│       │   │   │   ├── footer.jsp
│       │   │   │   └── main-layout.jsp
│       │   │   ├── pages/
│       │   │   │   ├── home.jsp
│       │   │   │   └── error.jsp
│       │   │   ├── admin/
│       │   │   └── user/
│       │   └── web.xml
│       └── META-INF/
```

---

## 🧩 JSP Layout System

Zero Star Coffee uses a **universal layout** pattern that centralizes your HTML head, header, footer, and scripts in one
file.

### 🧠 `main-layout.jsp`

```jsp
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
```

Each page (like `home.jsp` or `error.jsp`) provides only the **page content**.
The servlet dynamically decides which page to include.

---

### 🧭 Example: HomeServlet.java

```java

@WebServlet(name = "HomeServlet", urlPatterns = {"", "/"})
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("pageTitle", "Trang chủ Zero Star Cafe");
        req.setAttribute("pageContent", "/WEB-INF/views/pages/home.jsp");
        req.getRequestDispatcher("/WEB-INF/views/layout/main-layout.jsp")
                .forward(req, resp);
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
<script src="assets/js/main.js"></script>
<img src="assets/img/banner.png" alt="Banner">

<!-- Internal page link -->
<a href="./">Home</a>
<a href="logout">Logout</a>
```

💡 No need for `${pageContext.request.contextPath}` on every link.
Avoid `href="/assets/...` because it skips the context path when deployed under `/zero_star_cafe`.

---

## 🚀 Run & Access

| Path          | Description                                 |
|---------------|---------------------------------------------|
| `/`           | Home Page (via HomeServlet)                 |
| `/login`      | Login Page                                  |
| `/logout`     | Logout (invalidate session + redirect home) |
| `/health`     | Health check servlet                        |
| `/assets/...` | Static resources (CSS, JS, images)          |

**App URL:**
`http://localhost:8080/zero_star_cafe/`
