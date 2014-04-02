<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321.*"%>
<%
Journey jny;
if (session.getAttribute("JourneyFlag") == null){
jny= new Journey();
session.setAttribute("JourneyFlag",jny);
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<CENTER>
<H1>Choose a state</H1>
<FORM Action='control.jsp' METHOD='POST'>
<SELECT NAME='State' SIZE=7>
<OPTION>QLD</OPTION>
<OPTION>NSW</OPTION>
<OPTION>ACT</OPTION>
<OPTION>VIC</OPTION>
<OPTION>TAS</OPTION>
<OPTION>SA</OPTION>
<OPTION>WA</OPTION>
<OPTION>NT</OPTION>
</SELECT>
<BR><BR>
<INPUT type='submit' value='Send'>
<INPUT type='reset'>
</FORM></CENTER>
</body>
</html>