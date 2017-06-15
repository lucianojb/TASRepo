<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ page session="false"%>
<html>
<head>
<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
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
<title>Home</title>
</head>
<body>

	<jsp:include page="header.jsp" />

	<c:forEach items="${payloads}" var="payload">
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-4 col-sm-offset-2">
					<div id="integration-list">
						<ul>
							<li><a class="expand">
									<div class="right-arrow">+</div>
									<div id>
										<h2>
											<c:choose>
												<c:when test="${payload.resultValue == 0}">
													<img
														src="${pageContext.request.contextPath}/resources/pictures/bluecircle.png" />
												</c:when>
												<c:when test="${payload.resultValue == 1}">
													<img
														src="${pageContext.request.contextPath}/resources/pictures/greencircle.png" />
												</c:when>
												<c:when test="${payload.resultValue == 2}">
													<img
														src="${pageContext.request.contextPath}/resources/pictures/yellowcircle.png" />
												</c:when>
												<c:when test="${payload.resultValue == 3}">
													<img
														src="${pageContext.request.contextPath}/resources/pictures/redcircle.png" />
												</c:when>
												<c:otherwise>
													<img
														src="${pageContext.request.contextPath}/resources/pictures/red-x.png" />
												</c:otherwise>
											</c:choose>
											${payload.app.appName} v${payload.app.versionNum}
										</h2>

									</div>
							</a>

								<div class="detail">
									<div id="sup">
										<c:choose>
										<c:when test="${payload.resultValue == 0 }">
											Application health checks manually turned off
										</c:when>
										<c:when test="${not empty payload.errorMessage}">
											${payload.errorMessage}
										</c:when>
										<c:when test="${empty payload.connections}">
											Application has no connections
										</c:when>
										<c:otherwise>
											<table id="myTable"
												class="table-responsive table-striped tablesorter">
												<thead class="thead-inverse">
													<tr>
														<th style="text-align: center">Status</th>
														<th style="text-align: center">Connection</th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${payload.connections}" var="conn">
													<tr>
														<c:choose>
															<c:when test="${conn.value}">
																<td align="center"><img src="${pageContext.request.contextPath}/resources/pictures/greencircle.png" /></td>
																<td align="center">${conn.key}</td>
															</c:when>
															<c:otherwise>
																<td align="center"><img src="${pageContext.request.contextPath}/resources/pictures/redcircle.png" /></td>
																<td align="center">${conn.key}</td>														</c:otherwise>
														</c:choose>
													</tr>
													</c:forEach>
											</table>
											</c:otherwise>
										</c:choose>
									</div></li>

						</ul>
					</div>
				</div>
			</div>
		</div>
	</c:forEach>
</body>
</html>
