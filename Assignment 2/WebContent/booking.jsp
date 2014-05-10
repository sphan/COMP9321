<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="booking" class="edu.unsw.comp9321.bean.BookingListBean"
	scope="session" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hotels</title>
<link href="hotel.css" rel="stylesheet">
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<form action="payment" method="post">
		<c:choose>
			<c:when test="${booking.size != 0}">
				<table id="result-table" align="center">
					<thead>
						<tr id="result-table-header">
							<td>Room</td>
							<td>Room Type</td>
							<td>Price per night</td>
							<td>Extra bed?</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${booking.list}" var="bookingSelection">
							<tr>
								<td>${bookingSelection.index}</td>
								<td>${bookingSelection.roomType}</td>
								<td>${bookingSelection.price}</td>
								<td>
								<c:set
										value="${bookingSelection.extraBed ?'checked':''}"
										var="check" />
								<c:set
										value="${bookingSelection.roomType=='SINGLE'?'disabled':''}"
										var="disable" /> <input type="checkbox" name="extrabed"
									${disable} ${check} value="${bookingSelection.index}">
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div>Total: ${booking.totalPrice}</div>

				<div align="center">
					Please enter your information:
					<div>
						<div>
							First Name:<input type="text" name="fname"><br>
						</div>
						<div>
							Last Name:<input type="text" name="lname"><br>
						</div>
						<div>
							Email:<input type="text" name="email"><br>
						</div>
						<br>
						<div>
							Credit Card Number:<input type="text" name="creditcard"
								maxlength="16">
						</div>
						<div>
							Card Expiration <select name='expireMM'>
								<option value='01'>January</option>
								<option value='02'>February</option>
								<option value='03'>March</option>
								<option value='04'>April</option>
								<option value='05'>May</option>
								<option value='06'>June</option>
								<option value='07'>July</option>
								<option value='08'>August</option>
								<option value='09'>September</option>
								<option value='10'>October</option>
								<option value='11'>November</option>
								<option value='12'>December</option>
							</select> <select name='expireYY'>
								<option value='13'>2013</option>
								<option value='14'>2014</option>
								<option value='15'>2015</option>
							</select>
						</div>
					</div>
				</div>
				<input type="submit" name="action" value="confirm">
				<input type="submit" name="action" value="update total">
			</c:when>
			<c:otherwise>
				nothing selected
			</c:otherwise>
		</c:choose>
	</form>
</body>
</html>