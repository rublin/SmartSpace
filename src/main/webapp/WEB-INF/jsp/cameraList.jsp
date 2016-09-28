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
<h2>Add camera</h2>
<form method="post" action="/camera/admin">
    <input type="hidden" name="id" value="${camera.id}">
    <%--Zone:<select name="zoneId">
    <c:forEach var="zone" items="${zoneList}"--%>
    <dl>
        <dt>Name:</dt>
        <dd><input type="text" name="name" value="${camera.name}"><br></dd>
    </dl>
    <dl>
        <dt>Login:</dt>
        <dd><input type="text" name="login" value="${camera.login}"> </dd>
    </dl>
    <dl>
        <dt>Password:</dt>
        <dd><input type="password" name="password" value="${camera.password}"></dd>
    </dl>
    <dl>
        <dt>IP:</dt>
        <dd><input type="text" name="ip" value="${camera.ip}"></dd>
    </dl>
    <dl>
        <dt>URL:</dt>
        <dd><input type="text" name="url" value="${camera.URL}"></dd>
    </dl>
    <dl>
        <dt>Zone:</dt>
        <dd><select name="zoneId">
            <c:forEach var="zone" items="${zoneList}">
            <option value="${zone.id}">${zone.name}</option>
            </c:forEach>
        </dd>
    </dl><br>
    <input type="submit" value="Submit">
</form>
<h2>Cameras in system</h2>
<table border="1" cellpadding="8" cellspacing="0">
    <thead>
    <tr>
        <th>id</th>
        <th>name</th>
        <th>zone</th>
        <th>login</th>
        <th>password</th>
        <th>IP</th>
        <th>URL</th>
        <th>photo</th>
        <th></th>
    </tr>
    </thead>
    <c:forEach items="${cameraList}" var="camera">
        <jsp:useBean id="camera" scope="page" type="org.rublin.model.Camera"/>
        <tr>
            <td>${camera.id}</td>
            <td>${camera.name}</td>
            <td>${camera.zone.name}</td>
            <td>${camera.login}</td>
            <td>${camera.password}</td>
            <td>${camera.ip}</td>
            <td><a href="${camera.URL}">${camera.URL}</a> </td>
            <td>
                <img src="${camera.URL}" height="80" width="80">
            </td>
            <td>
                <a href="/camera/select?id=${camera.id}">edit</a>
                <br>
                <a href="/camera/admin/delete?id=${camera.id}">delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
