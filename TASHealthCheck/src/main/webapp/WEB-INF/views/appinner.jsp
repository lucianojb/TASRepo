<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container">
	<div class="row">
		<div class="col-sm-3 col-centered text-center">
			<h1>${app.appName}
				<c:if test="${not empty app.versionNum}">v${app.versionNum}</c:if>
			</h1>
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
							<img style="max-width: 20%"
								src="${pageContext.request.contextPath}/resources/pictures/bluecircle.png" />
						</c:when>
						<c:when test="${healthPayload.resultValue == 1}">
							<img style="max-width: 15%"
								src="${pageContext.request.contextPath}/resources/pictures/greencircle.png" />
						</c:when>
						<c:when test="${healthPayload.resultValue == 2}">
							<img style="max-width: 20%"
								src="${pageContext.request.contextPath}/resources/pictures/yellowcircle.png" />
						</c:when>
						<c:when test="${healthPayload.resultValue == 3}">
							<img style="max-width: 20%"
								src="${pageContext.request.contextPath}/resources/pictures/redcircle.png" />
						</c:when>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<div class="row top-buffer">
		<div class="col-sm-6 col-centered">
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
					<table id="myTable"
						class="table-responsive table-striped tablesorter"
						style="font-size: 20px;">
						<thead class="thead-inverse">
							<tr>
								<th style="text-align: center; font-size: 20px;">Connection</th>
								<th width="100%" style="text-align: center; font-size: 20px;">Details</th>
								<th style="text-align: center; font-size: 20px;">Status</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${healthPayload.connections}" var="conn">
								<tr class="spaceUnder">
									<c:choose>
										<c:when test="${empty conn.value.functional}">
											<td align="center">${conn.key}</td>
											<td align="center">${conn.value.details}</td>
											<td align="center"><img
												src="${pageContext.request.contextPath}/resources/pictures/greycircle.png" />
											</td>
										</c:when>
										<c:when test="${conn.value.functional}">
											<td align="center">${conn.key}</td>
											<td align="center">${conn.value.details}</td>
											<td align="center"><img width="50%"
												src="${pageContext.request.contextPath}/resources/pictures/greencircle.png" />
											</td>
										</c:when>
										<c:otherwise>
											<td align="center">${conn.key}</td>
											<td align="center">${conn.value.details}</td>
											<td align="center"><img style="max-width: 40%"
												src="${pageContext.request.contextPath}/resources/pictures/redcircle.png" /></td>
										</c:otherwise>
									</c:choose>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:otherwise>

			</c:choose>
		</div>
		<div class="row top-buffer">
			<div class="col-sm-6 col-centered">
				<c:if test="${not empty scheduledTimes}">
					<h2 class="row-bordered" style="text-align: center">Scheduled Down Times</h2>
					<table class="table-striped" style="width: 100%">
						<thead class="thead-inverse">
							<tr>
								<td align="center">Start time</td>
								<td align="center">End time</td>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${scheduledTimes}" var="sched">
								<tr>
									<td align="center"><fmt:formatDate
											value="${sched.startDate}" pattern="MM/dd/yyyy HH:mm" /></td>
									<td align="center"><fmt:formatDate
											value="${sched.endDate}" pattern="MM/dd/yyyy HH:mm" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:if>
			</div>
		</div>
	</div>
</div>