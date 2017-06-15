<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
		<form method=POST>
	<c:choose>
		<c:when test="${application.activeState}">
		Would you like to turn off app: ${application.appName}?
	</c:when>
		<c:otherwise>
		Would you like to turn on app: ${application.appName}?
	</c:otherwise>
	</c:choose>
	<button type="submit" class="btn btn-danger btn-block" name="submit"
		value="continue">Continue</button>
	<button type="submit" class="btn btn-error btn-block" name="submit"
		value="cancel">Cancel</button>
		</form>
</body>
</html>