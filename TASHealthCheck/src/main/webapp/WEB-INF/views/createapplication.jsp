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
				<div class="col-sm-2 col-centered  row-bordered">
					<h1 style="text-align: center; font-weight: bold;">Add
						Application</h1>
				</div>
			</div>

			<div class="col-sm-2 col-centered top-buffer">
				<div class="form-group">
					<label class="control-label" for="appName">Application Name</label>
					<sf:input class="form-control" id="appName" name="appName"
						path="appName" type="text" style = "text-align:center" />
					<sf:errors path="appName" />
				</div>
			</div>

			<div class="col-sm-2 col-centered">
				<div class="form-group">
					<label class="control-label" for="url">URL</label>
					<sf:input class="form-control" id="url" name="url" path="url"
						type="text" style = "text-align:center" />
					<sf:errors path="url" />
				</div>
			</div>

			<div class="row">
					<div class="form-group" id="connectionsGroup">
						<c:choose>
							<c:when test="${not empty connectionsAdded}">
							<c:set var="count" value="0" scope="page" />
							<div class="col-sm-2 col-sm-offset-5 first-col-label">Connections</div>
							<div class="first-col-label" style="text-align:left">Priority</div>
								<c:forEach var="con" items="${connectionsAdded}">
								<c:set var="priority" value="false"/>
									<div class = "col-sm-2 col-sm-offset-5 cn">
										<input type="text" class="form-control margin-bottom "
											name="connection" value="${con}" style = "text-align:center">
									</div>
									
									<c:forEach var="box" items="${priorityValues}">
										<c:if test="${count == box}">
											<c:set var="priority" value="true"/>
										</c:if>	
									</c:forEach>
									<div class="col-sm-1 cb">
									<c:choose>
										<c:when test="${priority}">
											<input type="checkbox" name="core" value=${count} checked>
										</c:when>
										<c:otherwise>
											<input type="checkbox" name="core" value=${count}">
										</c:otherwise>
									</c:choose>
									</div>
								<c:set var="count" value="${count + 1}" scope="page"/>
								</c:forEach>
							</c:when>
							<c:otherwise>
							<div class = "col-sm-2 col-sm-offset-5">
							<div class="first-col-label">Connections</div>
								<input type="text" class="form-control margin-bottom"
									name="connection" value="${con}" style = "text-align:center">
							</div>
							<div class="first-col-label" style="text-align:left">Priority</div>
							<div class = "col-sm-1">	
								<input type="checkbox" name="core" value=0>
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