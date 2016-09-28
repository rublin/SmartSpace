<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Sheremet
  Date: 18.09.2016
  Time: 11:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<form action="spring_security_check" method="post">
    <dl>
        <dt>Login / email:</dt>
        <dd><input type="text" placeholder="Email" class="form-control" name='username'></dd>
    </dl>
    <dl>
        <dt>Password:</dt>
        <dd><input type="password" placeholder="Password" class="form-control" name='password'></dd>
    </dl>
    <input type="submit" value="Login">
</form>

<div class="jumbotron">
    <div class="container">
        <c:if test="${error}">
            <div class="error">
                    ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
            </div>
        </c:if>
        <c:if test="${not empty message}">
            <div class="message">
                <spring:message code="${message}"/>
            </div>
        </c:if>
        <p>User login: <b>user@gmail.com / P@ssw0rd</b></p>

        <p>Admin login: <b>admin@gmail.com / P@ssw0rd</b></p>

        <p><a class="btn btn-primary btn-lg" role="button" href="register"><spring:message code="app.register"/> &raquo;</a></p>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>