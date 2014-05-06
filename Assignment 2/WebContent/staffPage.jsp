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
<div id="header">
<center><h1>Hotels</h1></center>
<hr>
</div>

<div id="content">
	<%@ include file="staffSearchForm.html"%>
	<div id="main-content">
		<p>All Customer Bookings:</p>
		<form action="search" method="POST">
			<table id="result-table">
				<thead>
					<tr id="result-table-header">
						<td>Hotel Name</td>
						<td>Customer</td>
						<td>Booking Number</td>
						<td>Select</td>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>Iris</td>
						<td>Somebody</td>
						<td>1</td>
						<td><input type="radio" name="selectBooking" /></td>
					</tr>
				</tbody>
			</table>
			<div>
				<input type="submit" name="action" value="Select Booking">
			</div>
		</form>
	</div>
</div>
</body>
</html>