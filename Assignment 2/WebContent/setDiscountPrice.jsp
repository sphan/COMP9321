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
	<div id="set-price-form" align="center">
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
					<c:set var="monthVal" value="1" />
					<c:forTokens items="January,February,March,April,May,June,July,August,September,October,November,December" delims="," var="month">
						<option value=<c:out value="${monthVal}" /> ${startMonth eq monthVal ? 'selected' : ''}>
							<c:out value="${month}" />
						</option>
						<c:set var="monthVal" value="${monthVal + 1}" />
					</c:forTokens>
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
					<c:set var="monthVal" value="1" />
					<c:forTokens items="January,February,March,April,May,June,July,August,September,October,November,December" delims="," var="month">
						<option value=<c:out value="${monthVal}" /> ${startMonth eq monthVal ? 'selected' : ''}>
							<c:out value="${month}" />
						</option>
						<c:set var="monthVal" value="${monthVal + 1}" />
					</c:forTokens>
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
					<input type="hidden" name="discountPrice" value="<c:out value="${discountPrice}" />" />
				</div>
				<div>
					Start Date:
					<input type="text" name="startDate" value="<c:out value="${startDate}" />" disabled />
					<input type="hidden" name="startDate" value="<c:out value="${startDate}" />" />
				</div>
				<div>
					End Date:
					<input type="text" name="endDate" value="<c:out value="${endDate}" />" disabled />
					<input type="hidden" name="endDate" value="<c:out value="${endDate}" />" />
				</div>
				<input type="submit" name="action" value="Back to Form" />
				<input type="submit" name="action" value="Confirm" />
			</form>
		</c:if>
	</div>
</div>
</body>
</html>