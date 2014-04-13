<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321.*, java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Thank you for Shopping</title>
</head>
<body>
<%@ include file="Header.html"%>
<center>
<c:choose>
<c:when test="${action eq 'buy'}">
	<div><h3>Thank you for purchasing!!</h3></div>
</c:when>
<c:when test="${action eq 'leave'}">
	<div><h3>Thank you for shopping!!</h3></div>
</c:when>
</c:choose>
</center>
</body>
</html>