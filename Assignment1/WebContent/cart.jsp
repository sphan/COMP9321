<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321.*, java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Shopping Cart</title>
</head>
<body>
<%@ include file="Header.html"%>
<center>
<c:choose>

<c:when test="${cartSize eq 0}">
	<h4>Cart is empty!</h4>
	<form action="control" method="POST">
	<input type="submit" value="Back to Search" />
	<input type="hidden" name="action" value="welcome" />
</form>
</c:when>
<c:when test="${cartSize ne 0}">
<form action="control" method="POST">
	<c:if test="${alreadyInCartSize > 0}">
		<div>
		<div>The following items are duplicated in your cart.</div>
		<c:forEach var="item" items="${alreadyInCart}">
			<div><c:out value="${item}" /></div>
		</c:forEach>
		</div>
	</c:if>
	<div>
	<table border="1" cellpadding="5">
		<tr>
			<td>Title</td>
			<td>Artist</td>
			<td>Type</td>
			<td>Publisher</td>
			<td>Price</td>
			<td>Select</td>
		</tr>
		<c:forEach var="item" items="${cart.items}">
			<tr>
				<td><c:out value="${item.title}" /></td>
				<td><c:out value="${item.artist}" /></td>
				<td><c:out value="${item.type}" /></td>
				<td><c:out value="${item.publisher}" /></td>
				<td><fmt:formatNumber value="${item.price}" type="currency" /></td>
				<td><input type="checkbox" name="removeFromCart" value="<c:out value="${item.title}" />"></td>
			</tr>
		</c:forEach>
		<tr>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td>Total</td>
			<td><fmt:formatNumber value="${totalCost}" type="currency" /></td>
		</tr>
	</table>
	<input type="submit" value="Remove From Cart" />
	<input type="hidden" name="action" value="remove" />
	</div>
</form>
<form action="control" method="POST">
<input type="submit" value="Back to Search" />
<input type="hidden" name="action" value="welcome" />
</form>
<form action="control" method="POST">
<input type="submit" value="Go to Checkout" />
<input type="hidden" name="action" value="checkout" />
</form>
</c:when>
</c:choose>
</center>
</body>
</html>