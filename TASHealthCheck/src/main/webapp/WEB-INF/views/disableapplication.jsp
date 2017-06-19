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
<script src="<c:url value="/resources/jquery-3.2.1.min.js"/>"></script>
<script>
	$(function () {
		$("#datetimepicker").datetimepicker();
	});
</script>
</head>
<body>
	<sf:form method="POST" commandName="downSchedule">
		
		<c:choose>
			<c:when test="${application.activeState}">
			${application.appName} healthchecks manually ON
			</c:when>
			<c:otherwise>
			${application.appName} healthchecks manually OFF
	</c:otherwise>
		</c:choose>
		<button type="submit" class="btn btn-danger btn-block" name="submit"
			value="continue">Toggle status</button>

		<br/> <br/>
		Or Schedule a planned down time <br/> <br/>
		
		<sf:input type="hidden" path="appID" value="${application.appID}" />
		
		<sf:input class="form-control" id="startDate" name="startDate"
							path="startDate" type="text" placeholder="MM/dd/yyyy HH:mm:ss"/>
		<sf:errors path="startDate" />
							
				
		<sf:input class="form-control" id="endDate" name="endDate"
							path="endDate" type="text" placeholder="MM/dd/yyyy HH:mm:sss"/>
		<sf:errors path="endDate" />
		
		${dateError}
							

		<button type="submit" class="btn btn-error btn-block" name="submit"
			value="schedule">Schedule</button>


		<br /> <br />
		<button type="submit" class="btn btn-error btn-block" name="submit"
			value="cancel">Cancel</button>
			
			
			<br/>
			<br/>
			Scheduled downtimes <br/>
			<table>
			<tr>
				<td>Start time</td>
				<td>End time</td>
			</tr>
		<c:forEach items="${scheduledTimes}" var="sched">
			<tr>
				<td>${sched.startDate}</td>
				<td>${sched.endDate}</td>
			</tr>
		</c:forEach>
		</table>
			
	</sf:form>
</body>
</html>