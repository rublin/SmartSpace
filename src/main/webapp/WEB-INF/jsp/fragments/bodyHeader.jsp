<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<header>
    <a href="${pageContext.request.contextPath}/">Home</a>
    <a href="${pageContext.request.contextPath}/zones">Zones</a>
    <a href="${pageContext.request.contextPath}/triggers">Triggers</a>
    <a href="${pageContext.request.contextPath}/events">Events</a>
    <a href="${pageContext.request.contextPath}/camera">Camera</a>
    <a href="${pageContext.request.contextPath}/users">Users</a>
    <a href="${pageContext.request.contextPath}/logout">logout</a>
    <%--<a href="/">Home</a>--%>
</header>
<%--<h3><a href="${pageContext.request.contextPath}">???</a> </h3>--%>