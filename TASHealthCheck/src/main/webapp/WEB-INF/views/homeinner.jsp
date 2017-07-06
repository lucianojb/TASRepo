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

<script type="text/javascript" src="<c:url value="/resources/jquery.filterizr.min.js"/>"></script>

<script type="text/javascript">
        $(function() {
            //Initialize filterizr with default options
            $('.filtr-container').filterizr();
        });
    </script>


<c:if test="${empty payloads}">
		<div class="row">
			<div class="col-sm-3 col-centered row-bordered">
				<h1 style="text-align: center;">No Applications Added</h1>
			</div>
		</div>
</c:if>

<div class="row">
            <ul class="simplefilter">
                <li class="active" data-multifilter="all">All</li>
                <li data-multifilter="1">Off</li>
                <li data-multifilter="2">Up</li>
                <li data-multifilter="3">Some</li>
                <li data-multifilter="4">Down</li>
                <li data-multifilter="5">Error</li>
            </ul>
        </div>

<c:set var="count" value="1" scope="page" />
<div class="filtr-container">
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
							<c:choose>
								<c:when test="${payload.resultValue == -1}">
								<li class="filtr-item" data-category="1" style="text-align: center"><a class="expand">
									<div class="right-arrow">+</div>
									<div>
									<div>
									<img align="left" style="max-width: 10%"
										src="${pageContext.request.contextPath}/resources/pictures/bluecircle.png" />
								</c:when>
								<c:when test="${payload.resultValue == 0}">
								<li class="filtr-item" data-category="2" style="text-align: center"><a class="expand">
									<div class="right-arrow">+</div>
									<div>
									<div>
									<img align="left" style="max-width: 10%"
										src="${pageContext.request.contextPath}/resources/pictures/greencircle.png" />
								</c:when>
								<c:when test="${payload.resultValue == 2}">
								<li class="filtr-item" data-category="3" style="text-align: center"><a class="expand">
									<div class="right-arrow">+</div>
									<div>
									<div>
									<img align="left" style="max-width: 10%"
										src="${pageContext.request.contextPath}/resources/pictures/yellowcircle.png" />
								</c:when>
								<c:when test="${payload.resultValue == 3}">
								<li class="filtr-item" data-category="4" style="text-align: center"><a class="expand">
									<div class="right-arrow">+</div>
									<div>
									<div>
									<img align="left" style="max-width: 10%"
										src="${pageContext.request.contextPath}/resources/pictures/redcircle.png" />
								</c:when>
								<c:otherwise>
								<li class="filtr-item" data-category="5" style="text-align: center"><a class="expand">
									<div class="right-arrow">+</div>
									<div>
									<div>
									<img align="left" style="max-width: 10%"
										src="${pageContext.request.contextPath}/resources/pictures/red-x.png" />
								</c:otherwise>
							</c:choose>
						</div>
							<h1 align:"center" style="font-size: 30px;">${payload.app.appName} <c:if test="${not empty payload.app.versionNum}">v${payload.app.versionNum}</c:if></h1>


						</div>
				</a>

					<div class="detail">
						<div id="sup">
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
														<c:when test="${empty conn.value.functional}">
															<td align="center"><img style="max-width: 20%"
																src="${pageContext.request.contextPath}/resources/pictures/greycircle.png" /></td>
															<td align="center">${conn.key}</td>
														</c:when>
														<c:when test="${conn.value.functional}">
															<td align="center"><img style="max-width: 20%"
																src="${pageContext.request.contextPath}/resources/pictures/greencircle.png" /></td>
															<td align="center">${conn.key}</td>
														</c:when>
														<c:otherwise>
															<td align="center"><img style="max-width: 20%"
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
	</div>
	
	<div id="show"></div>