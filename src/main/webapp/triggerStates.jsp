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
<h3><a href="index.html">Home</a></h3>
<h2>Object</h2>
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
    <c:forEach items="${objectList}" var="obj">
        <%--<jsp:useBean id="obj" scope="page" type="org.rublin.model.ControlledObject">--%>
            <tr>
                <td>${obj.id}</td>
                <td>${obj.name}</td>
                <td>${obj.status}</td>
                <td>${obj.secure}</td>
                <td></td>
                <td>
                    <form method="post" >
                        <select name="secure">
                            <option>NOT_PROTECTED</option>
                            <option>PARTLY_PROTECTED</option>
                            <option>FULLY_PROTECTED</option>
                        </select>
                        <input type="submit" value="Send">
                    </form>
                </td>
            </tr>
        <%--</jsp:useBean>--%>

    </c:forEach>
</table>

<h2>Add trigger</h2>
<%--<jsp:useBean id="trigger" type="org.rublin.model.Trigger"request"/>--%>
<form method="post" action="states?action=addTrigger">
    <input type="hidden" name="id" value="${trigger.id}">
    Name:<input type="text" name="name" value="${trigger.name}">
    <c:choose>
        <c:when test="${not empty trigger.id}">
            <c:choose>
                <c:when test="${trigger.type=='DIGITAL'}">
                    Digital<input type="radio" name="type" value="digital" CHECKED disabled>
                    Analog<input type="radio" name="type" value="analog" disabled>
                </c:when>
                <c:when test="${trigger.type=='ANALOG'}">
                    Digital<input type="radio" name="type" value="digital" disabled>
                    Analog<input type="radio" name="type" value="analog" CHECKED disabled>
                </c:when>
            </c:choose>
        </c:when>
        <c:when test="${empty trigger.id}">
            Digital<input type="radio" name="type" value="digital" CHECKED >
            Analog<input type="radio" name="type" value="analog" >
        </c:when>
    </c:choose>


    <input type="submit" value="Submit">
</form>
<h2>Triggers in system</h2>


<table border="1" cellpadding="8" cellspacing="0">
    <thead>
    <tr>
        <th>id</th>
        <th>name</th>
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
<h2>Triggers states history</h2>
<form method="get" action="states?action=show">
<select name="triggerId">
    <c:forEach var="trigger" items="${triggerList}">
        <%--<option>${trigger.id}</option>--%>
        <option value="${trigger.id}">${trigger.name}</option>
    </c:forEach>
</select>
    <input type="submit" value="Select">
</form>
<c:choose>
    <c:when test="${not empty trigger}">
        <h3>Trigger is: ${trigger.name}</h3>
    </c:when>
    <c:when test="${empty trigger}">
        <h3>All events</h3>
    </c:when>
</c:choose>

<table border="1" cellpadding="8" cellspacing="0" >
    <thead>
    <tr>
        <th>trigger</th>
        <th>time</th>
        <th>state</th>
    </tr>
    </thead>
    <c:forEach items="${eventList}" var="event">
        <tr>
            <td>${event.trigger.name}</td>
            <td>${event.time}</td>
            <td>${event.state}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
