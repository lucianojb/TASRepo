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
<script src="resources/jquery-3.2.1.min.js"></script>
</head>
<body>
	<jsp:include page="header.jsp" />

	<div class="container">
		<div class="row">
			<div class="col-sm-3 col-centered row-bordered">
				<h1 style="text-align: center;">Edit ${application.appName}</h1>
			</div>
		</div>

		<sf:form method="POST" commandName="application">
			<sf:input type="hidden" path="appID" value="${application.appID}" />
			<div class="row top-buffer">
				<div class="col-sm-3 col-centered">
					<div class="form-group">
						<label class="control-label" for="appName">App Name</label>
						<sf:input class="form-control" id="appName" name="appName"
							path="appName" type="text" value="${application.appName}" />
						<sf:errors path="appName" />
					</div>
				</div>
			</div>
			<div class="row">

				<div class="col-sm-3 col-centered">
					<div class="form-group">
						<label class="control-label" for="url">URL</label>
						<sf:input class="form-control" id="url" name="url" path="url"
							type="text" value="${application.url}" />
						<sf:errors path="url" />
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-sm-3 col-centered">
					<div class="form-group" id="connectionsGroup">
						<label class="control-label" for="connectionsGroup">Connections</label>
						<c:if test="${not empty connections}">
							<c:forEach var="con" items="${connections}">
								<input type="text" name="connection" value="${con}"
									class="form-control margin-bottom">
							</c:forEach>
						</c:if>
					</div>
				</div>
				<div>
					<c:if test="${not empty connectionInvalid}">
						<c:out value="${connectionInvalid}"></c:out>
					</c:if>
				</div>
			</div>
			<jsp:include page="component.jsp" />

			<div class="row top-buffer margin-bottom">
				<div class="col-sm-2 col-centered">
					<button type="submit" class="btn btn-success btn-block"
						name="submit" value="save">Save</button>
					<button type="submit" class="btn btn-block" name="submit"
						value="cancel">Cancel</button>
				</div>

			</div>
		</sf:form>
	</div>
</body>
</html>