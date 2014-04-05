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
			<td>Song Title</td>
			<td>Song Artist</td>
			<td>Album Title</td>
			<td>Genre</td>
			<td>Publisher</td>
			<td>Year</td>
			<td>Price</td>
			<td>Select ?</td>
		</tr>
		<c:forEach var="map" items="${songsFound}">
			<c:forEach var="song" items="${map.value}">
			<tr>
				<td><c:out value="${song.title}" /></td>
				<td><c:out value="${song.artist}" /></td>
				<td><c:out value="${map.key.title}" /></td>
				<td><c:out value="${map.key.genre}" /></td>
				<td><c:out value="${map.key.publisher}" /></td>
				<td><c:out value="${map.key.year}" /></td>
				<td><fmt:formatNumber value="${song.price}" type="currency" /></td>
				<td><input type="checkbox" name="addToCart" value="${song.title}"></td>
			</tr>
			</c:forEach>
		</c:forEach>
	</table>
	</div>
</center>
</form>
<%@ include file="searchFooter.html"%>
</body>
</html>