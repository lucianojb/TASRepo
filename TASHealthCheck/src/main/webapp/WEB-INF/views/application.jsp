<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Viewing Application</title>
<script
	src="${pageContext.request.contextPath}/resources/jquery-3.2.1.min.js"></script>
<script type="text/javascript">
	$(function() {
		setInterval(function() {
			$.get( "${pageContext.request.contextPath}/appinner/${app.appID}/${app.appName}", function( data ) {
			$( "#inner-app" ).html( data );
		});}, 60000);
	});
</script>
</head>
<link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
	
<body>

	<jsp:include page="header.jsp" />
	<div id="inner-app">
		<jsp:include page="appinner.jsp" />
	</div>

</body>
</html>