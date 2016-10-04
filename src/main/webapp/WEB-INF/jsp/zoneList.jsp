<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Sheremet
  Date: 15.09.2016
  Time: 13:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<h2>Zone</h2>
<form method="post" action="${pageContext.request.contextPath}/zones/edit">
    <input type="hidden" name="id" value="${zone.id}">
    <dl>
        <dt>Name:</dt>
        <dd><input type="text" name="name" value="${zone.name}"></dd>
    </dl>
    <dl>
        <dt>Short name:</dt>
        <dd><input type="text" name="shortName" value="${zone.shortName}"></dd>
    </dl>
    <input type="submit" value="Submit">
</form>
<table border="1" cellspacing="0" cellpadding="8">
    <thead>
    <tr>
        <th>id</th>
        <th>name</th>
        <th>short name</th>
        <th>status</th>
        <th>secure</th>
        <th></th>
        <th>Arming | Disarming</th>
    </tr>
    </thead>
    <c:forEach items="${zoneList}" var="zone">
        <td>${zone.id}</td>
        <td>${zone.name}</td>
        <td>${zone.shortName}</td>
        <td style="color: ${zone.status}">
            ${zone.status}
        </td>

            <c:choose>
                <c:when test="${zone.secure}">
                    <td style="color: green">ARMING</td>
                </c:when>
                <c:otherwise>
                    <td style="color: darkgray">DISARMIN</td>
                </c:otherwise>
            </c:choose>

        <td>
            <a href="zones/select?id=${zone.id}">edit</a>
            <br>
            <a href="zones/delete?id=${zone.id}">delete</a>
        </td>
        <td>
            <form method="post" action="zones/secure">
                <input type="hidden" name="id" value="${zone.id}">
                <select name="secure">
                    <option>true</option>
                    <option>false</option>
                </select>
                <input type="submit" value="Send">
            </form>
        </td></tr>
    </c:forEach>
</table>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
