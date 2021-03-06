<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="booking" class="edu.unsw.comp9321.bean.BookingListBean"
	scope="session" />
<jsp:useBean id="searchDetails"
	class="edu.unsw.comp9321.bean.SearchDetailsBean" scope="session" />
<jsp:useBean id="URL" class="edu.unsw.comp9321.bean.URLBookingBean"
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

	<div id="content">
		<%@ include file="searchForm.jsp"%>
		<div id="main-content">
			<c:if test="${searchDetails.location != 'Select'}">
				<p>Searching for hotel rooms in "${searchDetails.location}":</p>
			</c:if>
			<c:choose>
				<c:when test="${roomTypeList == null}">
				Search results are empty please search with different parameters
			</c:when>
				<c:otherwise>
					<form action="booking" method="POST">
						<c:if test="${URLhidden != null}">
							<input type="hidden" name="URLhidden" value="${URLhidden}">
						</c:if>
						<table id="result-table">
							<thead>
								<tr id="result-table-header">
									<td>Room Type</td>
									<td>Price per night</td>
									<td>Peaked or Discounted</td>
									<td>Count</td>
									<td>Select</td>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${roomTypeList}" var="roomType">
									<tr>
										<td>${roomType.roomType}</td>
										<td>${roomType.price}</td>
										<c:if
											test="${roomType.discounted eq true and roomType.peaked eq false}">
											<td>Discounted</td>
										</c:if>
										<c:if
											test="${roomType.peaked eq true and roomType.discounted eq false}">
											<td>Peaked</td>
										</c:if>
										<c:if
											test="${roomType.peaked eq true and roomType.discounted eq true}">
											<td>Peaked and Discounted</td>
										</c:if>
										<c:if
											test="${roomType.peaked eq false and roomType.discounted eq false}">
											<td>N/A</td>
										</c:if>
										<td>${roomType.count}</td>
										<td><input type="hidden" name="roomTypeName[]"
											value="${roomType.roomType}"><input type="hidden"
											name="roomTypePrice[]" value="${roomType.price}"><input
											type="number" name="roomTypeCount[]"
											value="${roomType.selectValue}" min="0" max=${roomType.count}></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<input type="submit" name="action" value="submit"> <input
							type="submit" name="action" value="calculate total">
					</form>
					<div>
						<c:if test="${totalPrice != null}">
						Total Price of booking selection is: ${totalPrice}<br>
						</c:if>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<div id="clearing-div"></div>
</body>
</html>