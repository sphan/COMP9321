<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321.*, java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Album Results</title>
</head>
<body>
<%@ include file="Header.html"%>
<%@ include file="SearchBar.html"%>
<form action="control" method="POST">
<center>
	<div>
	<table border="1" cellpadding="5">
		<tr>
			<td>Album Title</td>
			<td>Album Artist</td>
			<td>Genre</td>
			<td>Publisher</td>
			<td>Year</td>
			<td>Price</td>
			<td>Select ?</td>
		</tr>
		<c:forEach var="album" items="${albumsFound}">
			<tr>
				<td><c:out value="${album.title}" /></td>
				<td><c:out value="${album.artist}" /></td>
				<td><c:out value="${album.genre}" /></td>
				<td><c:out value="${album.publisher}" /></td>
				<td><c:out value="${album.year}" /></td>
				<td><fmt:formatNumber value="${album.price}" type="currency" /></td>
				<td><input type="checkbox" name="addToCart" value="<c:out value="${album.title}" />"></td>
			</tr>
		</c:forEach>
	</table>
	<input type="submit" value="Add To Cart" />
	<input type="hidden" name="action" value="add" />
	</div>
</center>
</form>
<form action="control" method="POST">
	<input type="submit" value="Back to Search" />
	<input type="hidden" name="action" value="welcome" />
</form>
</body>
</html>