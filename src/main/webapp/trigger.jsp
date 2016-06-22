<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Sheremet
  Date: 15.06.2016
  Time: 14:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Trigger | SmartSpace</title>
</head>
<body>
<h2><a href="index.html">Home</a></h2>
<h1>Add trigger</h1>
<%--<jsp:useBean id="trigger" type="model.AbstractTrigger" scope="request"/>--%>
<form method="post" action="triggers?action=add">
    <input type="hidden" name="id" value="${trigger.id}">
    Name:<input type="text" name="name" value="${trigger.name}">
    <br>
    Digital<input type="radio" name="type" value="digital" CHECKED>
    <br>
    Analog<input type="radio" name="type" value="analog">
    <input type="submit" value="Submit">
</form>
<h1>Triggers in system</h1>


<table border="1" cellpadding="8" cellspacing="0">
    <thead>
    <tr>
        <th>id</th>
        <th>name</th>
        <th>type</th>
    </tr>
    </thead>
    <c:forEach items="${triggerList}" var="trigger">
        <%--<jsp:useBean id="trigger" scope="page" type="model.DigitTrigger"/>--%>
        <tr>
            <td>${trigger.id}</td>
            <td>${trigger.name}</td>
            <td>${trigger.event.toString()}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
