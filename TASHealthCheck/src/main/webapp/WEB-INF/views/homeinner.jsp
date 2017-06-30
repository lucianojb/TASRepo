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
		
		$('input').click(function() {
		    var category = $(this).val();
		    $('tr.approw').hide();
		    var showAll = true;
		    var status=["down", "up", "error", "warning", "off"];
		    
		    var checkboxes = document.getElementsByClassName('checkbox');
		    for (var i=0; i<checkboxes.length; i++) {
		    	if(checkboxes[i].checked){
		    		 showAll=false;
		    		 $('.' + status[i]).show();
		    	}		
		    }
		    
		    if(showAll){
		        $('tr.approw').show();
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

<div class=row>
	<div class="col-sm-1 col-sm-offset-4">
	 <label class="control-label"
			for="down">Down</label>
		<input type="checkbox" value="3" class="checkbox" id="down" />
	</div>
	<div class="col-sm-1">
	 <label class="control-label"
			for="up">Up</label>
		<input type="checkbox" value="1" class="checkbox" id="up" />
	</div>
	<div class="col-sm-1">
	<label class="control-label"
			for="error">Error</label>
		<input type="checkbox" value="-1" class="checkbox" id="error" /> 
	</div>
	<div class="col-sm-1">
	<label class="control-label"
			for="warning">Warning</label>
		<input type="checkbox" value="2" class="checkbox" id="warning" /> 
	</div>
	<div class="col-sm-1" style="text-align:center">
	 <label class="control-label"
			for="off">Off</label>
		<input type="checkbox" value="0" class="checkbox" id="off" />
	</div>


</div>
<div class=row>
	<div class="col-sm-4 col-centered">
		<table class="table1">
			<tbody>
				<c:set var="count" value="1" scope="page" />
				<c:forEach items="${payloads}" var="payload">
					<%-- 				<c:choose> --%>
					<%-- 					<c:when test="${count % 2 == 1}"> --%>
					<!-- 						<div class="container-fluid"> -->
					<!-- 							<div class="row"> -->
					<c:choose>
						<c:when test="${payload.resultValue == 0}">
							<tr class="approw off" style="display: inline-block;">
						</c:when>
						<c:when test="${payload.resultValue == 1}">
							<tr class="approw up" style="display: inline-block;">
						</c:when>
						<c:when test="${payload.resultValue == 2}">
							<tr class="approw warning" style="display: inline-block;">
						</c:when>
						<c:when test="${payload.resultValue == 3}">
							<tr class="approw down" style="display: inline-block;">
						</c:when>
						<c:otherwise>
							<tr class="approw error" style="display: inline-block;">
						</c:otherwise>
					</c:choose>

					<td style="display: inline-block;">
						<div id="integration-list">
							<ul>
								<li style="text-align: center"><a class="expand">
										<div class="right-arrow">+</div>
										<div>
											<div>
												<c:choose>
													<c:when test="${payload.resultValue == 0}">
														<img align="left" style="max-width: 10%"
															src="${pageContext.request.contextPath}/resources/pictures/bluecircle.png" />
													</c:when>
													<c:when test="${payload.resultValue == 1}">
														<img align="left" style="max-width: 10%"
															src="${pageContext.request.contextPath}/resources/pictures/greencircle.png" />
													</c:when>
													<c:when test="${payload.resultValue == 2}">
														<img align="left" style="max-width: 10%"
															src="${pageContext.request.contextPath}/resources/pictures/yellowcircle.png" />
													</c:when>
													<c:when test="${payload.resultValue == 3}">
														<img align="left" style="max-width: 10%"
															src="${pageContext.request.contextPath}/resources/pictures/redcircle.png" />
													</c:when>
													<c:otherwise>
														<img align="left" style="max-width: 10%"
															src="${pageContext.request.contextPath}/resources/pictures/red-x.png" />
													</c:otherwise>
												</c:choose>
											</div>
											<h1 align:"center" style="font-size: 30px;">${payload.app.appName}
												<c:if test="${not empty payload.app.versionNum}">v${payload.app.versionNum}</c:if>
											</h1>


										</div>
								</a>

									<div class="detail">
										<div id="sup">
											<c:choose>
												<c:when test="${not empty payload.errorMessage}">
											${payload.errorMessage}
											<a
														href="<c:url value='/application/${payload.app.appID}/${payload.app.appName}'/>"
														class="btn btn-success btn-block" role="button">Details</a>
												</c:when>
												<c:when test="${payload.resultValue == 0}">
									Healthcheck parsing for application is manually turned off or has scheduled downtime
									<a
														href="<c:url value='/application/${payload.app.appID}/${payload.app.appName}'/>"
														class="btn btn-success btn-block" role="button">Details</a>
												</c:when>
												<c:when test="${empty payload.connections}">
											Application hasfnfnhhgnghnnghngnhgnghnghnghnghngnhghngnhgnh no connections
											<a
														href="<c:url value='/application/${payload.app.appID}/${payload.app.appName}'/>"
														class="btn btn-success btn-block" role="button">Details</a>
												</c:when>
												<c:otherwise>
													<div class="col-sm-12 col-centered">
														<table id="myTable"
															class="table-responsive table-striped tablesorter"
															style="font-size: 20px;">
															<thead class="thead-inverse">
																<tr>
																	<th class="col-sm-4"
																		style="text-align: center; font-size: 20px;">Status</th>
																	<th class="col-sm-4"
																		style="text-align: center; font-size: 20px;">Connection</th>
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
														<a
															href="<c:url value='/application/${payload.app.appID}/${payload.app.appName}'/>"
															class="btn btn-success btn-block" role="button">Details</a>
													</div>
												</c:otherwise>
											</c:choose>
										</div>
									</div></li>
							</ul>
						</div>
					</td>
					</tr>
					<c:set var="count" value="${count + 1}" scope="page" />
				</c:forEach>
				</div>
				</div>
			</tbody>
		</table>
		<div id="show"></div>