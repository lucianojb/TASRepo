<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<script type="text/javascript">
	$(function() {
		$(".expand").on("click", function() {
			$(this).next().slideToggle(200);
			$expand = $(this).find(">:first-child");
			if ($expand.text() == "+") {
				$expand.text("-");
			} else {
				$expand.text("+");
			}
		});
	});
</script>

<c:if test="${empty payloads}">
		<div class="row">
			<div class="col-sm-3 col-centered row-bordered">
				<h1 style="text-align: center;">No Applications Added</h1>
			</div>
		</div>
</c:if>
<c:set var="count" value="1" scope="page" />
	<c:forEach items="${payloads}" var="payload">

		<c:choose>
			<c:when test="${count % 2 == 1}">
				<div class="container-fluid">
					<div class="row">
						<div class="col-sm-4 col-sm-offset-2">
			</c:when>
			<c:otherwise>
				<div class="col-sm-4">
			</c:otherwise>
		</c:choose>

		<div id="integration-list" style="display:block;">
			<ul>
				<li style="text-align: center"><a class="expand">
						<div class="right-arrow">+</div>
						<div>
							<div>
							<c:choose>
								<c:when test="${payload.resultValue == -1}">
									<img class="left" style="max-width: 10%"
										src="${pageContext.request.contextPath}/resources/pictures/bluecircle.png" alt="Off" />
								</c:when>
								<c:when test="${payload.resultValue == 0}">
									<img class="left" style="max-width: 10%"
										src="${pageContext.request.contextPath}/resources/pictures/greencircle.png" alt="Up"/>
								</c:when>
								<c:when test="${payload.resultValue == 2}">
									<img class="left" style="max-width: 10%"
										src="${pageContext.request.contextPath}/resources/pictures/yellowcircle.png" alt="Warning"/>
								</c:when>
								<c:when test="${payload.resultValue == 3}">
									<img class="left" style="max-width: 10%"
										src="${pageContext.request.contextPath}/resources/pictures/redcircle.png" alt="Down"/>
								</c:when>
								<c:otherwise>
									<img class="left" style="max-width: 10%"
										src="${pageContext.request.contextPath}/resources/pictures/red-x.png" alt="Error"/>
								</c:otherwise>
							</c:choose>
						</div>
							<h1 style="text-align: center; font-size: 30px;">${payload.app.appName} <c:if test="${not empty payload.app.versionNum}">v${payload.app.versionNum}</c:if></h1>


						</div>
				</a>

					<div class="detail">
						<div id="sup" style="display:block;">
							<c:choose>
								<c:when test="${not empty payload.errorMessage}">
											${payload.errorMessage}
											<a href="<c:url value='/application/${payload.app.appID}/${payload.app.appName}'/>" class="btn btn-success btn-block" role="button">Details</a>
										</c:when>
								<c:when test="${payload.resultValue == -1}">
									Healthcheck parsing for application is manually turned off or has scheduled downtime
									<a href="<c:url value='/application/${payload.app.appID}/${payload.app.appName}'/>" class="btn btn-success btn-block" role="button">Details</a>
								</c:when>
								<c:when test="${empty payload.connections}">
											Application has no connections
											<a href="<c:url value='/application/${payload.app.appID}/${payload.app.appName}'/>" class="btn btn-success btn-block" role="button">Details</a>
										</c:when>
								<c:otherwise>
									<table id="myTable"
										class="table-responsive table-striped tablesorter" style="font-size: 20px; table-layout:fixed;
width: 100%;">
										<thead class="thead-inverse">
											<tr>
												<th class ="col-sm-4" style="text-align: center; font-size: 20px;">Status</th>
												<th class ="col-sm-4" style="text-align: center;font-size: 20px;">Connection</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${payload.connections}" var="conn">
												<tr>
													<c:choose>
														<c:when test="${empty conn.value.functional}">
															<td class="center"><img style="max-width: 20%"
																src="${pageContext.request.contextPath}/resources/pictures/greycircle.png" alt="warning"/></td>
															<td class="center">${conn.key}</td>
														</c:when>
														<c:when test="${conn.value.functional}">
															<td class="center"><img style="max-width: 20%"
																src="${pageContext.request.contextPath}/resources/pictures/greencircle.png" alt="up" /></td>
															<td class="center">${conn.key}</td>
														</c:when>
														<c:otherwise>
															<td class="center"><img style="max-width: 20%"
																src="${pageContext.request.contextPath}/resources/pictures/redcircle.png" alt="down" /></td>
															<td class="center">${conn.key}</td>
														</c:otherwise>
													</c:choose>
												</tr>
											</c:forEach>
									</table>
									<a href="<c:url value='/application/${payload.app.appID}/${payload.app.appName}'/>" class="btn btn-success btn-block" role="button">Details</a>
								</c:otherwise>
							</c:choose></div>
						</div></li>
			</ul>
		</div>

		<c:choose>
			<c:when test="${count % 2 == 1}">
				</div>
			</c:when>
			<c:otherwise>
				</div>
				</div>
				</div>
			</c:otherwise>
		</c:choose>

		<c:set var="count" value="${count + 1}" scope="page" />
	</c:forEach>
	
	<div id="show"></div>