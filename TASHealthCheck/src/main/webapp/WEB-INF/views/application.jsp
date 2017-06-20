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
<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">
<body>
	<jsp:include page="header.jsp" />
	<div class="container">
		<div class="row">
			<div class="col-sm-3 col-centered text-center">
				<h1>${app.appName}v${app.versionNum}</h1>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-3 col-centered text-center">
				<c:choose>
					<c:when test="${not empty healthPayload.errorMessage}">
						<img
							src="${pageContext.request.contextPath}/resources/pictures/red-x.png" />
						<div class="row top-buffer">
							<h4>An error occurred: ${healthPayload.errorMessage}</h4>
						</div>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${healthPayload.resultValue == 0}">
								<img
									src="${pageContext.request.contextPath}/resources/pictures/bluecircle.png" />
							</c:when>
							<c:when test="${healthPayload.resultValue == 1}">
								<img
									src="${pageContext.request.contextPath}/resources/pictures/greencircle.png" />
							</c:when>
							<c:when test="${healthPayload.resultValue == 2}">
								<img
									src="${pageContext.request.contextPath}/resources/pictures/yellowcircle.png" />
							</c:when>
							<c:when test="${healthPayload.resultValue == 3}">
								<img
									src="${pageContext.request.contextPath}/resources/pictures/redcircle.png" />
							</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</div>
		</div>

		<div class="row top-buffer">
			<div class="col-sm-4 col-centered">
				<c:choose>
					<c:when test="${empty healthPayload.errorMessage}">
						<table id="myTable"
							class="table-responsive table-striped tablesorter"
							style="font-size: 20px;">
							<thead class="thead-inverse">
								<tr>
									<th style="text-align: center; font-size: 20px;">Connection</th>
									<th style="text-align: center; font-size: 20px;">Status</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${healthPayload.resultValue == 0 }">
								Application health checks manually turned off or have scheduled down time
							</c:when>
									<c:when test="${not empty healthPayload.errorMessage}">
			${payload.errorMessage}
		</c:when>
									<c:when test="${empty healthPayload.connections}">
			Application has no connections
		</c:when>
									<c:otherwise>
										<c:forEach items="${healthPayload.connections}" var="conn">
											<tr class="spaceUnder">
												<c:choose>
													<c:when test="${conn.value}">
														<td align="center">${conn.key}</td>
														<td align="center"><img
															src="${pageContext.request.contextPath}/resources/pictures/greencircle.png" />
														<td>
													</c:when>
													<c:otherwise>
														<td align="center">${conn.key}</td>
														<td align="center"><img
															src="${pageContext.request.contextPath}/resources/pictures/redcircle.png" /></td>

													</c:otherwise>
												</c:choose>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</c:when>
				</c:choose>
			</div>
		</div>
	</div>
</body>
</html>