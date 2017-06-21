<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

Now looking at application ${app.appName} v${app.versionNum}
	<p></p>
	<p></p>
	
	<c:choose>
	<c:when  test="${not empty healthPayload.errorMessage}">
		<img src="${pageContext.request.contextPath}/resources/pictures/red-x.png" />
		<h4>An error occurred: ${healthPayload.errorMessage}</h4>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${healthPayload.resultValue == 0}">
				<img src="${pageContext.request.contextPath}/resources/pictures/bluecircle.png" />
			</c:when>
			<c:when test="${healthPayload.resultValue == 1}">
				<img src="${pageContext.request.contextPath}/resources/pictures/greencircle.png" />
			</c:when>
			<c:when test="${healthPayload.resultValue == 2}">
				<img src="${pageContext.request.contextPath}/resources/pictures/yellowcircle.png" />
			</c:when>
			<c:when test="${healthPayload.resultValue == 3}">
				<img src="${pageContext.request.contextPath}/resources/pictures/redcircle.png" />
			</c:when>
			<c:otherwise>
				<img src="${pageContext.request.contextPath}/resources/pictures/red-x.png" />
			</c:otherwise>
		</c:choose>
		
		<br>
		<br>
		
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
				<c:choose>
					<c:when test="${conn.value}">
						<img src="${pageContext.request.contextPath}/resources/pictures/greencircle.png" /> ${conn.key}: ${conn.value}<br>
					</c:when>
					<c:otherwise>
						<img src="${pageContext.request.contextPath}/resources/pictures/redcircle.png" /> ${conn.key}: ${conn.value}<br>
					</c:otherwise>
				</c:choose>
		</c:forEach>
		</c:otherwise>
		</c:choose>
	</c:otherwise>
	</c:choose>