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
    <jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<h2>Events history</h2>
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
        <c:choose>
            <c:when test="${event.alarm}">
                <tr style="color: red">
                    <td>${event.trigger.name}</td>
                    <td>${event.time}</td>
                    <td>${event.state}</td>
                </tr>
            </c:when>
            <c:otherwise>
                <tr style="color: green">
                    <td>${event.trigger.name}</td>
                    <td>${event.time}</td>
                    <td>${event.state}</td>
                </tr>
            </c:otherwise>
        </c:choose>

    </c:forEach>
</table>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
