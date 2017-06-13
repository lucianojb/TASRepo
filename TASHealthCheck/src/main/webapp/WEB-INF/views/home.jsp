<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page session="false" %>
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
	  $(".expand").on( "click", function() {
	    $(this).next().slideToggle(200);
	    $expand = $(this).find(">:first-child");
	    
	    if($expand.text() == "+") {
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

	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-4 col-sm-offset-2">
				<div id="integration-list">
					<ul>
						<li><a class="expand">
								<div class="right-arrow">+</div>
								<div id>
									<h2><img src="${pageContext.request.contextPath}/resources/pictures/bluecircle.png"/> LEOFA v3.5.6
									</h2>
									
								</div>
						</a>

							<div class="detail">
								<div id="sup">

									<table id="myTable"
										class="table-responsive table-striped tablesorter">
										<thead class="thead-inverse">
											<tr>
												<th style="text-align: center">Status</th>
												<th style="text-align: center">Connection</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td align="center"><img
													src="${pageContext.request.contextPath}/resources/pictures/greencircle.png" /></td>
												<td align="center">Server</td>
											</tr>
											<tr>
												<td align="center"><img
													src="${pageContext.request.contextPath}/resources/pictures/redcircle.png" /></td>
												<td align="center">Database</td>
											</tr>
									</table>
								</div></li>

					</ul>
				</div>
			</div>
			<div class="col-sm-4">
				<div id="integration-list">
					<ul>
						<li><a class="expand">
								<div class="right-arrow">+</div>
								<div>
									<h2>
										<img
											src="${pageContext.request.contextPath}/resources/pictures/bluecircle.png" />
										IdeaFactory v3.5.6
									</h2>
								</div>
						</a>

							<div class="detail">
								<table id="myTable"
									class="table-responsive table-striped tablesorter">
									<thead class="thead-inverse">
										<tr>
											<th style="text-align: center">Status</th>
											<th style="text-align: center">Connection</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td align="center"><img
												src="${pageContext.request.contextPath}/resources/pictures/greencircle.png" /></td>
											<td align="center">Server</td>
										</tr>
										<tr>
											<td align="center"><img
												src="${pageContext.request.contextPath}/resources/pictures/redcircle.png" /></td>
											<td align="center">Database</td>
										</tr>
								</table>
							</div></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
