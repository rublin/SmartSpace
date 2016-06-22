<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Sheremet
  Date: 15.06.2016
  Time: 17:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>States of triggers | Smart Space</title>
</head>
<body>
<h1>Triggers states history</h1>
<h3>Trigger is: ${trigger.name}</h3>
<table border="1" cellpadding="8" cellspacing="0" >
    <thead>
    <tr>
        <th>time</th>
        <th>state</th>
    </tr>
    </thead>
    <c:forEach items="${eventList}" var="event">
        <%--<jsp:useBean id="state" scope="page" type="repository.StateRepository"/>--%>
        <tr>
            <td>${event.time}</td>
            <td>${event.state}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
