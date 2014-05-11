<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hotels</title>
</head>
<body>
<div id="header">
	<h1>
		<a href="<%out.println(request.getContextPath());%>/">Hotels</a>
	</h1>
	<div id="loginForm">
	<form action="login" method="POST" align="center">
		<div>
			Username: 
			<input type="text" name="username" />
		</div>
		<div>
			Password: 
			<input type="password" name="password" />
		</div>
		<div>
			<input type="submit" name="action" value="Login"/>
		</div>
	</form>
</div>
	<c:choose>
		<c:when test="${errorMessage != null}">
			<h4 align="center">${errorMessage}</h4>
		</c:when>
	</c:choose>
	<c:if test="${URLMessage != null}">
		<h4 align="center">${URLMessage}</h4>
	</c:if>
</div>
</body>
</html>