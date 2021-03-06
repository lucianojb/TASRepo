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
<script src="${pageContext.servletContext.contextPath}/resources/jquery-3.2.1.min.js"></script>
</head>
<body>
	<jsp:include page="header.jsp" />

	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-3 col-centered row-bordered">
				<h1 style="text-align: center; font-weight: bold;">Edit ${application.appName}</h1>
			</div>
		</div>

		<sf:form method="POST" commandName="application">
			<sf:input type="hidden" path="appID" value="${application.appID}" />
			<sf:input type="hidden" path="activeState" value="${application.activeState}"/>
			<sf:input type="hidden" path="versionNum" value="${application.versionNum}"/>
						
			<div class="row top-buffer">
				<div class="col-sm-2 col-centered">
					<div class="form-group">
						<label class="control-label" for="appName">Application Name</label>
						<sf:input class="form-control" id="appName" name="appName" style = "text-align:center"
							path="appName" type="text" value="${application.appName}" />
						<sf:errors path="appName" cssClass="urlError" element="h2"/>
					</div>
				</div>
			</div>
			<div class="row">

				<div class="col-sm-2 col-centered">
					<div class="form-group">
						<label class="control-label" for="url">URL</label>
						<sf:input class="form-control" id="url" name="url" path="url"
							type="text" value="${application.url}" style = "text-align:center"/>
						<sf:errors path="url" cssClass="urlError" element="h2"/>
					</div>
				</div>
			</div>

			<div class="row">
					<div class="form-group" id="connectionsGroup">
						<c:if test="${not empty connections}">
						<div class="col-sm-2 col-sm-offset-5 first-col-label">Connections</div>
						<div class="first-col-label" style="text-align:left">Priority</div>
							<c:forEach var="con" items="${connections}" varStatus="myIndex">
								<div class = "col-sm-2 col-sm-offset-5 cn">
									<input type="text" name="connection" value="${con.connName}"
										class="form-control margin-bottom" style = "text-align:center">
								</div>
								<div class="col-sm-1 cb">
								<c:choose>
								<c:when test="${con.priority}">
									<input type="checkbox" name="core" value="${myIndex.index}" checked>
								</c:when>
								<c:otherwise>
									<input type="checkbox" name="core" value="${myIndex.index}">
								</c:otherwise>
								</c:choose>
								</div>
							</c:forEach>
						</c:if>
					</div>
				</div>
				<div>
					<c:if test="${not empty connectionInvalid}">
					<div class = "row" style="padding-bottom:20px">
						<div class="col-sm-3 col-centered">
							<h2 style="text-align:center">${connectionInvalid}</h2>
						</div>
					</div>
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