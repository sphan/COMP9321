<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321.*, java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hotels</title>
<link href="hotel.css" rel="stylesheet">
</head>
<body>
<div id="header">
<form action="login" method="POST">
<div align="right"><input type="submit" name="action" value="Logout" /></div>
</form>
<center><h1>Hotels</h1></center>
<div>Logged in as: <c:out value="${loginName}" />
</div>
<hr>
</div>

<div id="content">
	<form action="owner" method="POST">
		<input type="submit" name="action" value="Back to Main">
	</form>
	<div id="set-price-form">
		<c:if test="${setDiscountStatus eq 'displayForm'}">
			<form action="owner" method="POST">
				<div>
					Hotel Location:
					<input type="text" name="location" value="<c:out value="${location}" />" disabled />
					<input type="hidden" name="location" value="<c:out value="${location}" />" />
				</div>
				<div>
					Room Type:
					<input type="text" name="roomType" value="<c:out value="${roomType}" />" disabled />
					<input type="hidden" name="roomType" value="<c:out value="${roomType}" />" />
				</div>
				<div>
					Current Price:
					<input type="text" name="curPrice" value="<c:out value="${curPrice}" />" disabled />
					<input type="hidden" name="curPrice" value="<c:out value="${curPrice}" />" />
				</div>
				<div>
					Discounted Price:
					<input type="text" name="discountPrice" value="${discountPrice ne '' ? discountPrice : ''}" />
				</div>
				<div id="checkin">
				Discount Start Date: <br> Date: <select name="startday">
					<c:forEach var="date" begin="1" end="31">
						<option ${startDate eq date ? 'selected' : ''}><c:out value="${date}" /></option>
					</c:forEach>
				</select> Month: <select name="startmonth">
					<option value=01>January</option>
					<option value=02>February</option>
					<option value=03>March</option>
					<option value=04>April</option>
					<option value=05>May</option>
					<option value=06>June</option>
					<option value=07>July</option>
					<option value=08>August</option>
					<option value=09>September</option>
					<option value=10>October</option>
					<option value=11>November</option>
					<option value=12>December</option>
				</select> Year: <select name="startyear">
					<c:forEach var="year" begin="2014" end="2018">
						<option ${startYear eq year ? 'selected' : ''}><c:out value="${year}" /></option>
					</c:forEach>
				</select>
			</div>
			<div id="checkout">
				Discount End Date: <br> Date: <select name="endday">
					<c:forEach var="date" begin="1" end="31">
						<option ${endDate eq date ? 'selected' : ''}><c:out value="${date}" /></option>
					</c:forEach>
				</select>
				Month: <select name="endmonth">
					<option value=1>January</option>
					<option value=2>February</option>
					<option value=3>March</option>
					<option value=4>April</option>
					<option value=5>May</option>
					<option value=6>June</option>
					<option value=7>July</option>
					<option value=8>August</option>
					<option value=9>September</option>
					<option value=10>October</option>
					<option value=11>November</option>
					<option value=12>December</option>
				</select> Year: <select name="endyear">
					<c:forEach var="year" begin="2014" end="2018">
						<option ${endYear eq year ? 'selected' : ''}><c:out value="${year}" /></option>
					</c:forEach>
				</select>
			</div>
				<input type="submit" value="Submit" />
				<input type="hidden" name="action" value="submitDiscountPrice" />
			</form>
		</c:if>
		<c:if test="${setDiscountStatus eq 'confirm'}">
			<form action="owner" method="POST">
				<div>
					Hotel Location:
					<input type="text" name="location" value="<c:out value="${location}" />" disabled />
					<input type="hidden" name="location" value="<c:out value="${location}" />" />
				</div>
				<div>
					Room Type:
					<input type="text" name="roomType" value="<c:out value="${roomType}" />" disabled />
					<input type="hidden" name="roomType" value="<c:out value="${roomType}" />" />
				</div>
				<div>
					Current Price:
					<input type="text" name="curPrice" value="<c:out value="${curPrice}" />" disabled />
					<input type="hidden" name="curPrice" value="<c:out value="${curPrice}" />" />
				</div>
				<div>
					Discounted Price:
					<input type="text" name="discountPrice" value="<c:out value="${discountPrice}" />" disabled/>
				</div>
				<div>
					Start Date:
					<input type="text" name="startDate" value="<c:out value="${startDate}" />" disabled />
				</div>
				<div>
					End Date:
					<input type="text" name="endDate" value="<c:out value="${endDate}" />" disabled />
				</div>
				<input type="submit" name="action" value="Back" />
				<input type="hidden" name="action" value="backToDiscountForm" />
				<input type="submit" name="action" value="Confirm" />
			</form>
		</c:if>
	</div>
</div>
</body>
</html>