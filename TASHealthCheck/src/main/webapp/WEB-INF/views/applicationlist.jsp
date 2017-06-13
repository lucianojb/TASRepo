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

	<div class="row top-buffer">
			<div class = "col-sm-8 col-centered">
				<table id="myTable" class="table table-striped tablesorter">
				<thead class="thead-inverse">
					<tr>
						<th style="text-align: center">App Name</th>
						<th style="text-align: center">URL</th>
					</tr>
				</thead>
				
				<tbody>
					<c:forEach items="${appData}" var="app">
						<tr>
							<td align="center"><c:out value="${app.appName}"/></td>
							<td align="center"><c:out value="${app.url}" /></td>
							<td align="center"><a href="<c:url value='/editapplication/${app.appID}' />" >Edit</a></td>
						</tr>
					</c:forEach>
				</tbody>
				</table>
			</div>		
		</div>


</body>
</html>