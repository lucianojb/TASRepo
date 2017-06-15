<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="header.jsp" />

	<div class="container">
		<div class="row">
			<div class="col-sm-3 col-centered row-bordered">
				<h1 style="text-align: center;">Delete ${application.appName}</h1>
			</div>
		</div>
		<form method=POST>
			<div class="row top-buffer">
				<div class="col-sm-2 col-centered">
					<button type="submit" class="btn btn-danger btn-block"
						name="submit" value="delete">Delete</button>
					<button type="submit" class="btn btn-error btn-block" name="submit"
						value="cancel">Cancel</button>
				</div>
			</div>
		</form>
	</div>
</body>
</html>