<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">	
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>

<nav class="navbar navbar-default">

	<div class="container">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#navbar-collapse-2">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="${pageContext.request.contextPath}/"> <img
				style="max-width: 20px; max-height: 20px;"
				src="${pageContext.request.contextPath}/resources/pictures/logo.png" /></a>
			<a class="navbar-brand" href="${pageContext.request.contextPath}/"> TAS Healthcheck</a>
		</div>
		
		<sec:authorize access="isAuthenticated()">
				<ul class="nav navbar-nav navbar-center">
      				<li><a href="${pageContext.request.contextPath}/applications">Applications</a></li>
      				<li><a href="${pageContext.request.contextPath}/createapplication">Create Application</a></li>
    			</ul>
    			<ul class="nav navbar-nav navbar-right">
      				<li><a href="${pageContext.request.contextPath}/logout"><span class="glyphicon glyphicon-log-in"></span> Logout</a></li>
    			</ul>
		</sec:authorize>
		
		<sec:authorize access="isAnonymous()">
	    		<ul class=" nav navbar-nav navbar-right">
      				<li><a href="${pageContext.request.contextPath}/login"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
    			</ul>
    		</sec:authorize>
	</div>

</nav>
