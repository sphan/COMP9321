<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321.*, java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<CENTER>
<%   String state = request.getParameter("State"); %>
<H1>Thanks for choosing <%= state %></H1>
<%
  Journey jny = (Journey)session.getAttribute("JourneyFlag");
  jny.addPlace(state);
  Iterator iter = jny.getPlaces();
%>
Your journey so far: 
<%
  while (iter.hasNext()) out.println("- "+ iter.next());
%>
<BR>
Would you like to continue your travels?
<FORM ACTION='menu.jsp' METHOD='POST'>
<INPUT TYPE='submit' VALUE='More!'>
</FORM>
<FORM ACTION='enough.jsp' METHOD='POST'>
<INPUT TYPE='submit' VALUE='Enough!'>
</FORM></CENTER>
</body>
</html>