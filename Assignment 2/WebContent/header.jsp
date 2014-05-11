<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="header">
	<h1>
		<a href="<%out.println(request.getContextPath());%>/">Hotels</a>
	</h1>
	<c:choose>
		<c:when test="${errorMessage != null}">
			<h4 align="center">${errorMessage}</h4>
		</c:when>
	</c:choose>
	<c:if test="${URLMessage != null}">
		<h4 align="center">${URLMessage}</h4>
	</c:if>
	<hr>
</div>