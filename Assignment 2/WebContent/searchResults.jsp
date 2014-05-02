<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hotels</title>
<link href="hotel.css" rel="stylesheet">
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>

<div id="content">
	<%@ include file="searchForm.html"%>
	<div id="main-content">
		<p>Hotels and rooms matching your results:</p>
		<form name="action" method="POST">
			<table id="result-table">
				<thead>
					<tr id="result-table-header">
						<td>Hotel Name</td>
						<td>Room Package</td>
						<td>Price/night</td>
						<td>Book</td>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>Iris</td>
						<td>2 Single Bed</td>
						<td>70</td>
						<td><input type="radio" name="book" /></td>
					</tr>
					<tr>
						<td>Iris</td>
						<td>2 Single Bed</td>
						<td>70</td>
						<td><input type="radio" name="book" /></td>
					</tr>
					<tr>
						<td>Iris</td>
						<td>2 Single Bed</td>
						<td>70</td>
						<td><input type="radio" name="book" /></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</div>
<div id="clearing-div"></div>
</body>
</html>