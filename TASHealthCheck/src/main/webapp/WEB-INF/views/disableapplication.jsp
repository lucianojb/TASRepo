<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Schedule Downtime</title>
<script type="text/javascript" src="<c:url value="/resources/jquery-3.2.1.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/jquery-ui.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/jquery.datetimepicker.full.min.js"/>"></script>
<link type="text/css" href="<c:url value="/resources/css/jquery.datetimepicker.css" />" rel="stylesheet">	
	
<script type="text/javascript">
	$(function(){
		jQuery.noConflict();
		$('#startDate').datetimepicker({
			formatTime:'g:i A', 
			format: 'm/d/Y h:i A',
			step: 15
		});
		$('#endDate').datetimepicker({
			formatTime:'g:i A', 
			format: 'm/d/Y h:i A',
			step: 15
		});
	});
</script>

</head>
<body>
	<jsp:include page="header.jsp" />

	<div class="container">
		<div class="row">
			<div class="col-sm-4 col-centered row-bordered">
				<h1 style="text-align: center;">Downtime for
					${application.appName}</h1>
			</div>
		</div>
		<sf:form method="POST" commandName="downSchedule">
			<div class="row">
				<div class="col-sm-4 col-centered top-buffer">
					<c:choose>
						<c:when test="${application.activeState}">
							<h2 style="text-align: center;">${application.appName}
								Healthchecks are currently enabled</h2>
							<button type="submit" class="btn btn-danger btn-block"
								name="submit" value="continue">Turn HealthChecks Off</button>
						</c:when>
						<c:otherwise>
							<h2 style="text-align: center;">${application.appName}
								Healthchecks are currently disabled</h2>
							<button type="submit" class="btn btn-success btn-block"
								name="submit" value="continue">Turn HealthChecks On</button>
						</c:otherwise>
					</c:choose>
				</div>
			</div>

			<div class="row top-buffer">
				<div class="col-sm-4 col-centered row-bordered">
					<h1 style="text-align: center;">Schedule Downtime</h1>
				</div>
			</div>

			<div class="row top-buffer">
			
				<c:choose>
				<c:when test="${not empty scheduledTimes}">
					<div class="col-sm-3 col-sm-offset-2">
				</c:when>
				<c:otherwise>
					<div class="col-sm-3 col-centered">
				</c:otherwise>
				</c:choose>
					<sf:input type="hidden" path="appID" value="${application.appID}" />

					<h2 style="text-align: center">Start Date and Time:</h2>
					<sf:input class="form-control" id="startDate" name="startDate"
						path="startDate" type="text" placeholder="MM/dd/yyyy hh:mm"
						autocomplete="off" style="text-align:center" />
					<sf:errors path="startDate" />
					<br />

					<h2 style="text-align: center">End Date and Time:</h2>
					<sf:input class="form-control" id="endDate" name="endDate"
						path="endDate" type="text" placeholder="MM/dd/yyyy hh:mm"
						autocomplete="off" style="text-align:center" />
					<sf:errors path="endDate" />
					<div class="row top-buffer"></div>
				</div>
				<div class="col-sm-6">
					<c:if test="${not empty scheduledTimes}">
						<h2 style="text-align: center">Scheduled Downtimes</h2>
						<table class="table-striped" style="width: 100%">
							<thead class="thead-inverse">
								<tr>
									<td align="center">Start time</td>
									<td align="center">End time</td>
									<td align="center"></td>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${scheduledTimes}" var="sched">
									<tr>
										<td align="center"><fmt:formatDate
												value="${sched.startDate}" pattern="MM/dd/yyyy hh:mm aa" /></td>
										<td align="center"><fmt:formatDate
												value="${sched.endDate}" pattern="MM/dd/yyyy hh:mm aa" /></td>
										<td><a
											href="<c:url value='/deleteschedule/${sched.schedID}'/>">Delete</a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:if>
				</div>
			</div>
			
		<c:if test="${not empty dateError}">
					<div class = "row" style="padding-bottom:20px">
						<div class="col-sm-3 col-centered">
							<h2 style="text-align:center">${dateError}</h2>
						</div>
					</div>
		</c:if>

		<div class="row top-buffer">
				<div class="col-sm-3 col-centered">

					<button type="submit" class="btn btn-success btn-block"
						name="submit" value="schedule">Schedule</button>
				</div>

			</div>

			<div class="row top-buffer">
				<div class="col-sm-3 col-centered">
					<button type="submit" class="btn btn-error btn-block" name="submit"
						value="cancel">Return to App List</button>
				</div>
			</div>

		</sf:form>
	</div>

</body>
</html>