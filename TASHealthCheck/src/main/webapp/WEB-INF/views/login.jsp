<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>TAS Login</title>
<link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
    <body>
    	<jsp:include page="header.jsp" />
		<div class="container">
			<div class="row">
				<div class="col-sm-3 col-centered row-bordered">
					<h1 style="text-align: center">Admin Login</h1>
				</div>
			</div>

		<sf:form action="${loginUrl}" method="post">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		<c:url var="loginUrl" value="/login" />

			<div class="row top-buffer">
				<div class="col-sm-3 col-centered top-buffer">
					<div class="form-group">
						<label class="control-label"for="username">Username:</label> <input type="text" class="form-control" id="username" name="username"/>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-sm-3 col-centered">
					<div class="form-group">
						<label class="control-label" for="password">Password:</label> <input type="password" class="form-control" id="password" name="password">
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-sm-3 col-centered">
					<button type="submit" class="btn btn-primary btn-block">Login</button>
				</div>
			</div>
	</sf:form>
	</div>
</body>
</html>