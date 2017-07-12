<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Application</title>
<script src="resources/jquery-3.2.1.min.js"></script>
</head>
<body>
	<jsp:include page="header.jsp" />
	<div class="container-fluid">
		<sf:form method="POST" commandName="application">
			<div class="row">
				<div class="col-sm-3 col-centered  row-bordered">
					<h1 style="text-align: center; font-weight: bold;">Add
						Application</h1>
				</div>
			</div>

			<div class="col-sm-3 col-centered top-buffer">
				<div class="form-group">
					<label class="control-label" for="appName">Application Name</label>
					<sf:input class="form-control" id="appName" name="appName"
						path="appName" type="text" />
					<sf:errors path="appName" />
				</div>
			</div>

			<div class="col-sm-3 col-centered">
				<div class="form-group">
					<label class="control-label" for="url">URL</label>
					<sf:input class="form-control" id="url" name="url" path="url"
						type="text" />
					<sf:errors path="url" />
				</div>
			</div>

			<div class="row">
				<div class="col-sm-3 col-centered">
					<div class="form-group" id="connectionsGroup">
						<label class="control-label" for="connectionsGroup">Connections</label>
						<c:choose>
							<c:when test="${not empty connectionsAdded}">
								<c:forEach var="con" items="${connectionsAdded}">
									<input type="text" class="form-control margin-bottom"
										name="connection" value="${con}">
								</c:forEach>
							</c:when>
							<c:otherwise>
							<div class = "col-sm-8">
								<input type="text" class="form-control margin-bottom"
									name="connection" value="${con}">
									</div>
							<div class = "col-sm-2">
								<input type="checkbox" value="">
							</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div>
					<c:if test="${not empty connectionInvalid}">
						<c:out value="${connectionInvalid}"></c:out>
					</c:if>
				</div>
			</div>

			<jsp:include page="component.jsp" />

			<div class="row top-buffer">
				<div class="col-sm-2 col-centered">
					<button type="submit" class="btn btn-success btn-block"
						name="submit" value="create">Create</button>
					<button type="submit" class="btn btn-error btn-block" name="submit"
						value="cancel">Cancel</button>
				</div>
			</div>
		</sf:form>
	</div>
</body>
</html>