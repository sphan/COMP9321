<%-- This page displays the itinerary of the user so far --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321.*, java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="journey" class="edu.unsw.comp9321.JourneyBean"
	scope="session" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Third Attempt at Good JSP</title>
</head>
<body>
<%@ include file="Header.html" %>
<center>

<c:choose>
	<c:when test="${isAlreadySelected eq 'true'}">
		<h1> You have already added this state </h1>
	</c:when>
	<c:otherwise>
		<h1>Thanks for choosing ${state}</h1>
	</c:otherwise>
</c:choose>

<p> Your journey so far: 

<c:forEach var="place" items="${journey.places}">
<c:out value="${place} -" /> 
</c:forEach>
  
</p>
<br>
Would you like to continue your travels?
<form action='control'>
	<input type='hidden' name='action' value='menu'/>
	<input type='submit' value='Moar!'/>
</form>
<form action='control' method='POST'>
	<input type='hidden' name='action' value='enough'/>
	<input type='submit' value='Enough!'></form>
</center>
</body>
</html>