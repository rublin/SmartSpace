<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Sheremet
  Date: 15.09.2016
  Time: 13:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Zone | SmartSpace</title>
</head>
<body>
<h3><a href="/">Home</a></h3>
<h2>Zone</h2>
<form method="post" action="states?action=addZone">
    <input type="hidden" name="id" value="${zone.id}">
    Name:<input type="text" name="name" value="${zone.name}">
    <input type="submit" value="Submit">
</form>
<table border="1" cellspacing="0" cellpadding="8">
    <thead>
    <tr>
        <th>id</th>
        <th>name</th>
        <th>status</th>
        <th>secure</th>
        <th></th>
        <th></th>
    </tr>
    </thead>
    <c:forEach items="${zoneList}" var="zone">
        <td>${zone.id}</td>
        <td>${zone.name}</td>
        <td>${zone.status}</td>
        <td>${zone.secure}</td>
        <td>
            <a href="states?action=edit&zoneId=${zone.id}">edit</a>
            <br>
            <a href="states?action=delete&zoneId=${zone.id}">delete</a>
        </td>
        <td>
            <form method="post" >
                <select name="secure">
                    <option>true</option>
                    <option>false</option>
                </select>
                <input type="submit" value="Send">
            </form>
        </td></tr>
    </c:forEach>
</table>
</body>
</html>
