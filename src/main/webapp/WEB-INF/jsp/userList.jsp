<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Sheremet
  Date: 18.09.2016
  Time: 12:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<h2>Add user</h2>
<form method="post" action="/users">
    <input type="hidden" name="id" value="${user.id}">
    <dl>
        <dt>First name:</dt>
        <dd><input type="text" name="fname" value="${user.firstName}"><br></dd>
    </dl>
    <dl>
        <dt>Last name:</dt>
        <dd><input type="text" name="lname" value="${user.lastName}"></dd>
    </dl>
    <dl>
        <dt>Role admin:</dt>
        <dd><input type="checkbox" name="admin" value="ROLE_ADMIN"></dd>
    </dl>
    <dl>
        <dt>Email:</dt>
        <dd><input type="email" name="email" value="${user.email}"> </dd>
    </dl>
    <dl>
        <dt>Password:</dt>
        <dd><input type="password" name="password" value="${user.password}"></dd>
    </dl>
    <dl>
        <dt>Mobile:</dt>
        <dd><input type="text" name="mobile" value="${user.mobile}"></dd>
    </dl>
    <dl>
        <dt>Telegram name:</dt>
        <dd><input type="text" name="telegram_name" value="${user.telegramName}"></dd>
    </dl>
    <input type="submit" value="Submit">
</form>
<h2>Users in system</h2>
<table border="1" cellpadding="8" cellspacing="0">
    <thead>
    <tr>
        <th>id</th>
        <th>first name</th>
        <th>last name</th>
        <th>role</th>
        <th>email</th>
        <th>mobile</th>
        <th>telegram name</th>
        <th>active</th>
        <th></th>
    </tr>
    </thead>
    <c:forEach items="${userList}" var="user">
        <jsp:useBean id="user" scope="page" type="org.rublin.model.user.User"/>
        <tr>
            <td>${user.id}</td>
            <td>${user.firstName}</td>
            <td>${user.lastName}</td>
            <td>${user.roles}</td>
            <td>${user.email}</td>
            <td>${user.mobile}</td>
            <td>${user.telegramName}</td>
            <td>
                <c:choose>
                    <c:when test="${user.enabled}">
                    enabled<br>
                    <a href="/users/enable?id=${user.id}">disable</a></td>
            </c:when>
            <c:otherwise>
                disabled<br>
                <a href="/users/enable?id=${user.id}">enable</a></td>
            </c:otherwise>
                </c:choose>
            <td>

                <a href="/users/select?id=${user.id}">edit</a>
                <br>
                <a href="/users/delete?id=${user.id}">delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
