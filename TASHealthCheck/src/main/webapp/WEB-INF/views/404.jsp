<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    
<html>
<head>
<title>404 Page</title>

<link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet">
</head>
	
<body>

	<jsp:include page="header.jsp" />
	<h3>You have stumbled upon a broken or non-existent link....</h3>
	
</body>
</html>