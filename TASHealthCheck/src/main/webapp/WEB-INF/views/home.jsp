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

		<div id="integration-list">
			<ul>
				<li style="text-align: center"><a class="expand">
						<div class="right-arrow">+</div>
						<div>
							<div>
							<c:choose>
								<c:when test="${payload.resultValue == 0}">
									<img align="left"
										src="${pageContext.request.contextPath}/resources/pictures/bluecircle.png" />
								</c:when>
								<c:when test="${payload.resultValue == 1}">
									<img align="left"
										src="${pageContext.request.contextPath}/resources/pictures/greencircle.png" />
								</c:when>
								<c:when test="${payload.resultValue == 2}">
									<img align="left"
										src="${pageContext.request.contextPath}/resources/pictures/yellowcircle.png" />
								</c:when>
								<c:when test="${payload.resultValue == 3}">
									<img align="left"
										src="${pageContext.request.contextPath}/resources/pictures/redcircle.png" />
								</c:when>
								<c:otherwise>
									<img align="left"
										src="${pageContext.request.contextPath}/resources/pictures/red-x.png" />
								</c:otherwise>
							</c:choose>
						</div>
							<h1 align:"center" style="font-size: 30px;">${payload.app.appName} v${payload.app.versionNum}</h1>


						</div>
				</a>

					<div class="detail">
						<div id="sup">
							<c:choose>
								<c:when test="${not empty payload.errorMessage}">
											${payload.errorMessage}
											<a href="<c:url value='/application/${payload.app.appID}'/>" class="btn btn-success btn-block" role="button">Details</a>
										</c:when>
								<c:when test="${empty payload.connections}">
											Application has no connections
											<a href="<c:url value='/application/${payload.app.appID}'/>" class="btn btn-success btn-block" role="button">Details</a>
										</c:when>
								<c:otherwise>
								<div class = "col-sm-12 col-centered">
									<table id="myTable"
										class="table-responsive table-striped tablesorter" style="font-size: 20px;">
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
														<c:when test="${conn.value}">
															<td align="center"><img
																src="${pageContext.request.contextPath}/resources/pictures/greencircle.png" /></td>
															<td align="center">${conn.key}</td>
														</c:when>
														<c:otherwise>
															<td align="center"><img
																src="${pageContext.request.contextPath}/resources/pictures/redcircle.png" /></td>
															<td align="center">${conn.key}</td>
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
</body>
</html>