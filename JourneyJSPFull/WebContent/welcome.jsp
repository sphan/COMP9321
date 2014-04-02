<%-- This is the welcome page and does absolutely nothing other than welcome the user--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome!</title>
</head>
<body>
<%@ include file="Header.html"%>
<center>
<h1>Let's visit Australia!</h1>
Would you like to begin your journey?
<!--
	Send the user on to the Menu page 
 -->
<form action='menu.jsp'>
    <input type='submit' value='Onwards!'></form>
</center>
<%@ include file="footer.jsp"%>
</body>
</html>