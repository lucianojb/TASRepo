<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

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
		    $('td.approw').hide();
// 		    $('tr.approw').hide();
		    var showAll = true;
		    var status=["down", "up", "error", "warn", "off"];
		    var count = 1;
		    var divs = [];
		    var divsdupe =[];
		    
		    var checkboxes = document.getElementsByClassName('checkbox1');
		    for (var i=0; i<checkboxes.length; i++) {
		    	if(checkboxes[i].checked){
		    		 showAll=false;	
		    		
		    		 $('.' + status[i]).each(function( i, val ) {
		    			  divs.push(val);
		    			});
		    		 
		    		  $('.' + status[i]).show();
		    		 
		    	}		
		    }

// 		    $.each(divs, function(i, el){
// 		        if($.inArray(el, divsdupe) === -1) divsdupe.push(el);
// 		    });
		    
// 		    for (var i=0; i<divsdupe.length; i+=2) {
		    	
// 				if(i == divsdupe.length-1){
// //			    	$('.table1').append(divsdupe[i]);
//  			    	$('.table1').find('tbody').append($('<tr class="approw">').append(divsdupe[i]));

// 				}
// 				else{
// 			    	var tr = $(".table1 tr:last");
// 			    	$('.table1').find('tbody').append($('<tr class="approw">'));
// 			    	$('.table1 tr.approw:last').append(divsdupe[i]);
// 			    	$('.table1 tr.approw:last').append(divsdupe[i+1]);		
// // 			    			divsdupe[i+1]));
// 				}		    	
// 		    }
		    
		    if(showAll){
		        $('td.approw').show();
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

</div>
<div class=row>
	<div class="container">
		<table class="table table1">
			<thead>
				<tr>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:set var="count" value="1" scope="page" />


				<c:forEach items="${payloads}" var="payload">
					<c:if test="${count % 2 == 1}">
						<tr class="approw" style= "border:none;">
					</c:if>

					<c:choose>
						<c:when test="${payload.resultValue == 0}">
							<td class="approw off" align="center">
						</c:when>
						<c:when test="${payload.resultValue == 1}">
							<td class="approw up" align="center">
						</c:when>
						<c:when test="${payload.resultValue == 2}">
							<td class="approw warn" align="center">
						</c:when>
						<c:when test="${payload.resultValue == 3}">
							<td class="approw down" align="center">
						</c:when>
						<c:otherwise>
							<td class="approw error" align="center">
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
					</div>
					<c:if test="${count % 2 != 1}">
						</tr>
					</c:if>

					<c:set var="count" value="${count + 1}" scope="page" />
				</c:forEach>
				</div>
				</div>
			</tbody>
		</table>
	</div>
	<div id="show"></div>