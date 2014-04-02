<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<H2>Using EL implicit objects</H2> 
<P/> 
<UL> 
<LI><b>Request parameter called <code>test</code>:</b> ${param.test}</LI> 
<LI><b>User-agent info in request Header:</b> ${header["User-Agent"]}</LI> 
<LI><b>Cookie (JSESSIONID) value:</b> ${cookie.JSESSIONID.value}</LI> 
<LI><b>Server Info:</b> ${pageContext.servletContext.serverInfo}</LI> 
<LI><b>Request Method:</b> ${pageContext.request.method}</LI> 
</UL>

</body>
</html>