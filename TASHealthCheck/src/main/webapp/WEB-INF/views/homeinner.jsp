<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script type="text/javascript"
	src="<c:url value="/resources/jquery.filterizr.min.js"/>"></script>

<script type="text/javascript">
        $(function() {
            //Initialize filterizr with default options
            $('.filtr-container').filterizr('toggleFilter');
        });
</script>


<c:if test="${empty payloads}">
	<div class="row">
		<div class="col-sm-3 col-centered row-bordered">
			<h1 style="text-align: center;">No Applications Added</h1>
		</div>
	</div>
</c:if>

<div class="filtr-container container">
	<c:forEach items="${payloads}" var="payload">
		<c:choose>
			<c:when test="${payload.resultValue == -1}">
				<div class="filtr-item col-sm-3" data-category="1" style="text-align: center">
					<img align="left" style="max-width: 10%"
						src="${pageContext.request.contextPath}/resources/pictures/bluecircle.png" />
			</c:when>
			<c:when test="${payload.resultValue == 0}">
				<div class="filtr-item col-sm-3" data-category="2" style="text-align: center">
					<img align="left" style="max-width: 10%"
						src="${pageContext.request.contextPath}/resources/pictures/greencircle.png" />
			</c:when>
			<c:when test="${payload.resultValue == 2}">
				<div class="filtr-item col-sm-3" data-category="3" style="text-align: center">
					<img align="left" style="max-width: 10%"
						src="${pageContext.request.contextPath}/resources/pictures/yellowcircle.png" />
			</c:when>
			<c:when test="${payload.resultValue == 3}">
				<div class="filtr-item col-sm-3" data-category="4" style="text-align: center">
					<img align="left" style="max-width: 10%"
						src="${pageContext.request.contextPath}/resources/pictures/redcircle.png" />
			</c:when>
			<c:otherwise>
				<div class="filtr-item col-sm-3" data-category="5" style="text-align: center">
					<img align="left" style="max-width: 10%"
						src="${pageContext.request.contextPath}/resources/pictures/red-x.png" />
			</c:otherwise>
		</c:choose>
		<a href="<c:url value='/application/${payload.app.appID}/${payload.app.appName}'/>">
		<h1 style="font-size: 30px;">${payload.app.appName}
			<c:if test="${not empty payload.app.versionNum}">v${payload.app.versionNum}</c:if>
		</h1>
		</a>
</div>
</c:forEach>
</div>