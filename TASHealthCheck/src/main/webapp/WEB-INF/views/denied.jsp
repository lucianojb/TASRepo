<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Access Denied</title>
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
			<div class="col-sm-4 col-centered">
				<h1 style="text-align:center">Error: Wrong username or password</h1>
			</div>
		</div>

		<div class="row top-buffer">
			<div class="col-sm-4 col-centered">
				<a href="./login" type="button" class="btn btn-primary btn-center btn-block">Return
					to login</a>
			</div>
		</div>

	</div>
</body>
</html>