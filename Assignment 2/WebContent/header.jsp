<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="header">
	<h1><a href="customerMain.jsp">Hotels</a></h1>
	<%@ include file="loginForm.html"%>
	<c:choose>
		<c:when test="${errorMessage != null}">
			<h4 align="center">${errorMessage}</h4>
		</c:when>
	</c:choose>
	<hr>
</div>