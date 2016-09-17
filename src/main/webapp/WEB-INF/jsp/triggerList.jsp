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
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<h2>Add trigger</h2>
<%--<jsp:useBean id="trigger" type="org.rublin.model.Trigger"request"/>--%>



<form method="post" action="/triggers">
    <input type="hidden" name="id" value="${trigger.id}">
    <%--Zone:<select name="zoneId">
    <c:forEach var="zone" items="${zoneList}"--%>
    <dl>
        <dt>Name:</dt>
        <dd><input type="text" name="name" value="${trigger.name}"><br></dd>
    </dl>
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

            <dl>
                <dt>Digital</dt>
                <dd><input type="radio" name="type" value="digital" CHECKED ></dd>
            </dl>
            <dl>
                <dt>Analog</dt>
                <dd><input type="radio" name="type" value="analog" ></dd>
            </dl>
            <dl>
                <dt>min value:</dt>
                <dd><input type="number" name="minThreshold" value="${trigger.minThreshold}"></dd>
            </dl>
            <dl>
                <dt>max value:</dt>
                <dd><input type="number" name="maxThreshold" value="${trigger.maxThreshold}"></dd>
            </dl>
        </c:when>
    </c:choose>
    <dl>
        <dt>Secure:</dt>
        <dd><input type="checkbox" name="secureTrigger" value="${trigger.secure}"></dd>
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
<h2>Triggers in system</h2>
<table border="1" cellpadding="8" cellspacing="0">
    <thead>
    <tr>
        <th>id</th>
        <th>name</th>
        <th>zone</th>
        <th>type</th>
        <th>state</th>
        <th>secure</th>
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
            <td>${trigger.state}</td>
            <td>${trigger.secure}</td>
            <td>
                <c:choose>
                    <c:when test="${trigger.type=='DIGITAL'}">
                        <a href="events/add?triggerId=${trigger.id}&state=true">state</a>
                    </c:when>
                    <c:when test="${trigger.type=='ANALOG'}">
                        <a href="events/add?triggerId=${trigger.id}&state=20.0">state</a>
                    </c:when>
                </c:choose>
            </td>
            <td>
                <a href="/triggers/select?id=${trigger.id}">edit</a>
                <br>
                <a href="/triggers/delete?id=${trigger.id}">delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
