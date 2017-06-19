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
<script>
function goBack() {
    window.history.back();
}
</script>

</head>
<body>

	<sf:form>
	
			Confirm deletion of scheduled down time from  
			<fmt:formatDate value="${schedule.startDate}" pattern="MM/dd/yyyy HH:mm" /> to 
			<fmt:formatDate value="${schedule.endDate}" pattern="MM/dd/yyyy HH:mm" /> for ${application.appName}
	
			<button type="submit" class="btn btn-error btn-block" name="submit"
			value="delete">Delete</button>
			<button onclick="goBack()">Cancel</button>
	
	</sf:form>

</body>
</html>