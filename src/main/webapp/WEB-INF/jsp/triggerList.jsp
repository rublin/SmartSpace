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
<h3><a href="/">Home</a></h3>
<h2>Add trigger</h2>
<%--<jsp:useBean id="trigger" type="org.rublin.model.Trigger"request"/>--%>

<h2>Triggers in system</h2>

<form method="post" >
    <input type="hidden" name="id" value="${trigger.id}">
    <%--Zone:<select name="zoneId">
    <c:forEach var="zone" items="${zoneList}"--%>
    Name:<input type="text" name="name" value="${trigger.name}"><br>
    <c:choose>
        <c:when test="${not empty trigger.id}">
            <c:choose>
                <c:when test="${trigger.type=='DIGITAL'}">
                    Digital<input type="radio" name="type" value="digital" CHECKED disabled>
                    Analog<input type="radio" name="type" value="analog" disabled>
                </c:when>
                <c:when test="${trigger.type=='ANALOG'}">
                    Digital<input type="radio" name="type" value="digital" disabled>
                    Analog<input type="radio" name="type" value="analog" CHECKED disabled><br>
                    min value:<input type="number" name="minThreshold" value="${trigger.minThreshold}">
                    max value:<input type="number" name="maxThreshold" value="${trigger.maxThreshold}">
                </c:when>
            </c:choose>
        </c:when>
        <c:when test="${empty trigger.id}">
            Digital<input type="radio" name="type" value="digital" CHECKED >
            Analog<input type="radio" name="type" value="analog" ><br>
            Secure<input type="checkbox" name="secureTrigger"><br>
            min value:<input type="number" name="minThreshold" value="${trigger.minThreshold}">
            max value:<input type="number" name="maxThreshold" value="${trigger.maxThreshold}">
        </c:when>
    </c:choose>
    <br>
    <input type="submit" value="Submit">
</form>

<table border="1" cellpadding="8" cellspacing="0">
    <thead>
    <tr>
        <th>id</th>
        <th>name</th>
        <th>zone</th>
        <th>type</th>
        <%--<th>current state</th>--%>
        <th>update state</th>
        <th></th>
    </tr>
    </thead>
    <c:forEach items="${triggerList}" var="trigger">
        <%--<jsp:useBean id="trigger" scope="page" type="org.rublin.model.DigitTrigger"/>--%>
        <tr>
            <td>${trigger.id}</td>
            <td>${trigger.name}</td>
            <td>${trigger.zone.name}</td>
            <td>${trigger.type}</td>
                <%--<td>${trigger.event.getState()}</td>--%>
            <td>
                <c:choose>
                    <c:when test="${trigger.type=='DIGITAL'}">
                        <a href="states?action=addEvent&triggerId=${trigger.id}&state=true">state</a>
                    </c:when>
                    <c:when test="${trigger.type=='ANALOG'}">
                        <a href="states?action=addEvent&triggerId=${trigger.id}&state=20.0">state</a>
                    </c:when>
                </c:choose>
            </td>
            <td>
                <a href="states?action=edit&triggerId=${trigger.id}">edit</a>
                <br>
                <a href="states?action=delete&triggerId=${trigger.id}">delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
