<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin application view</title>
</head>
<body>

	<jsp:include page="header.jsp" />

	<div class="container">
		<div class="row">
			<div class="col-sm-3 col-centered  row-bordered">
				<h1 style="text-align: center; font-weight: bold;">Admin
					Application List</h1>
			</div>
		</div>

		<div class="row top-buffer">
			<div class="col-sm-8 col-centered">
				<table id="myTable" class="table table-striped tablesorter">
					<thead class="thead-inverse">
						<tr>
							<th style="text-align: center">App Name</th>
							<th style="text-align: center">URL</th>
							<th></th>
							<th></th>
						</tr>
					</thead>

					<tbody>
						<c:forEach items="${appData}" var="app">
							<tr>
								<td align="center"><c:out value="${app.appName}" /></td>
								<td align="center"><c:out value="${app.url}" /></td>
								<td align="center"><a
									href="<c:url value='/editapplication/${app.appID}'/>">Edit</a></td>
								<td align="center"><a
									href="<c:url value='/deleteapplication/${app.appID}'/>">Delete</a></td>
								<td align="center"><a
									href="<c:url value='/disableapplication/${app.appID}'/>">Schedule Down Time</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>