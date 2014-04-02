<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>The Music Store</title>
</head>
<body>
<h2>Welcome to The Music Store</h2>
<center>
<form action="control" method="POST">
<select name="searchType">
	<option>Album</option>
	<option>Song</option>
</select>
<input type="text" name="searchString" />
<input type="hidden" name="action" value="search" />
<input type="submit" value="Search" />
</form>
</center>
</body>
</html>