<%-- This page displays a menu of the travel destinations to the user --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321.*"%>
<jsp:useBean id="journey" class="edu.unsw.comp9321.JourneyBean"
	scope="session" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%@ include file="Header.html"%>
<center>
<h1>Choose a state</h1>
<%-- 
	 Control is transferred to the ControlServlet along with
	 request parameter (in this case, the state selected by
	 the user) 
--%>
<form action='control' method='POST'>
<select name='state' size="7">
	<option>QLD</option>
	<option>NSW</option>
	<option>ACT</option>
	<option>VIC</option>
	<option>TAS</option>
	<option>SA</option>
	<option>WA</option>
	<option>NT</option>
</select> 
<br />
<br />
<input type="hidden" name="action" value="add"/>
<input type='submit' value='Send'/> 
<input type='reset'/>
</form>
</center>
</body>
</html>