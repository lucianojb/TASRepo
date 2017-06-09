<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
<link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
	<title>Home</title>
</head>
<body>
<jsp:include page="header.jsp" />
<div class="container">
		<div class="row">
			<div class = "col-sm-8 col-centered">
				<table id="myTable" class="table table-striped tablesorter">
				<thead class="thead-inverse">
					<tr>
						<th style="text-align: center">Application</th>
						<th style="text-align: center">Version</th>
						<th style="text-align: center">Status</th>
					</tr>
					</thead>
					<tbody>
					<tr>
					<td align="center">LEOFA</td>
					<td align="center">v3.5.6</td>
					<td align="center">v3.5.6</td>
					</tr>
					<tr>
					<td align="center">IdeaFactory</td>
					<td align="center">v2.7.8</td>
					<td align="center">v3.5.6</td>
					</tr>
				</table>
		</div>
	</div>
</div>		
</body>
</html>
