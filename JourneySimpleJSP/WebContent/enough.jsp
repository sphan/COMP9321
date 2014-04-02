<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<% session.invalidate(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<center>
<H1>No longer in session!</H1>
<h2>Go back to beginning ?</h2>
<FORM Action='welcome.jsp'>
<INPUT type='submit' value='Try it'>
</FORM>
</center>
</body>
</html>