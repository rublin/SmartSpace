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
<h2><a href="index.html">Home</a></h2>
<h1>Add trigger</h1>
<%--<jsp:useBean id="trigger" type="org.rublin.model.Trigger"request"/>--%>
<form method="post" action="states?action=addTrigger">
    <%--<input type="hidden" name="id" value="${trigger.id}">--%>
    Name:<input type="text" name="name" >
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
        <th>current state</th>
        <th>update state</th>
        <th></th>
    </tr>
    </thead>
    <c:forEach items="${triggerList}" var="trigger">
        <%--<jsp:useBean id="trigger" scope="page" type="org.rublin.model.DigitTrigger"/>--%>
        <tr>
            <td>${trigger.id}</td>
            <td>${trigger.name}</td>
            <td>${trigger.type}</td>
            <td>${trigger.event.getState()}</td>
            <td><a href="states?action=addEvent&triggerId=${trigger.id}&state=${trigger.event.getState()}">state</a></td>
            <td></td>
        </tr>
    </c:forEach>
</table>
<h1>Triggers states history</h1>
<form method="get" action="states?action=show">
<select name="triggerId">
    <c:forEach var="trigger" items="${triggerList}">
        <option>${trigger.id}</option>
    </c:forEach>
</select>
    <input type="submit" value="Select">
</form>
<h3>Trigger is: ${trigger.name}</h3>
<table border="1" cellpadding="8" cellspacing="0" >
    <thead>
    <tr>
        <th>time</th>
        <th>state</th>
    </tr>
    </thead>
    <c:forEach items="${eventList}" var="event">
        <tr>
            <td>${event.time}</td>
            <td>${event.state}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
