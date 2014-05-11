<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="header">
	<form action="login" method="POST">
		<div align="right"><input type="submit" name="action" value="Logout" /></div>
	</form>
	<h1><a href="<%out.println(request.getContextPath());%>/">Hotels</a></h1>
	<div>Logged in as: <c:out value="${loginName}" /></div>
	<c:choose>
		<c:when test="${errorMessage != null}">
			<h4 align="center">${errorMessage}</h4>
		</c:when>
	</c:choose>
	<hr>
</div>