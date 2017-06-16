<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Logout</title>
<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
	<jsp:include page="header.jsp" />
	<div class="container">
		<div class="row">
			<div class="col-sm-3 col-centered">
				<h1 style="text-align: center">You are now logged out.</h1>
			</div>
		</div>
		<div class = "row top-buffer">
			<div class = "col-sm-3 col-centered">
				<a href="/login" class="btn btn-warning btn-block" role="button">Return to Login</a>	
			</div>
		</div>
	</div>
</html>
