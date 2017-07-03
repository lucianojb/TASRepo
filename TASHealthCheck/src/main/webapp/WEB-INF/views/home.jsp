<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page session="false"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
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
		setInterval(function() {
			$.get( "${pageContext.request.contextPath}/homeinner", function( data ) {
				$( "#inner-home" ).html( data );
			});}, 20000 * ${fn:length(payloads)});
	});
</script>

<script>
var checkedboxes = "${checkedBoxes}";
var checkboxes = document.getElementsByClassName('checkbox1');
for (var i=0; i<checkboxes.length; i++) {
	if(checkedboxes[i]){
		checkboxes[i].checked=true;
	}}
</script>
<title>Home</title>
</head>
<body>

	<jsp:include page="header.jsp" />
	<div class = "row">
		<div class="col-sm-4 col-centered">
	<sf:form id="form" method="POST">
	<c:choose>
        <c:when test="${checkboxValues[0]}">
            <input type="checkbox" id="checkbox-1" name="filterCheckBox" value="-1" onclick="submit()" checked>Off
        </c:when>
        <c:otherwise>
             <input type="checkbox" id="checkbox-1" name="filterCheckBox" value="-1" onclick="submit()">Off
        </c:otherwise>
        </c:choose>
        <c:choose>
        <c:when test="${checkboxValues[1]}">
        <input type="checkbox" id="checkbox0" name="filterCheckBox" value="0" onclick="submit()" checked>Up
        </c:when>
        <c:otherwise>
        <input type="checkbox" id="checkbox0" name="filterCheckBox" value="0" onclick="submit()">Up
        </c:otherwise>
        </c:choose>
        <c:choose>
        <c:when test="${checkboxValues[2]}">
        <input type="checkbox" id="checkbox1" name="filterCheckBox" value="1" onclick="submit()" checked>Error
        </c:when>
        <c:otherwise>
        <input type="checkbox" id="checkbox1" name="filterCheckBox" value="1" onclick="submit()">Error
        </c:otherwise>
        </c:choose>
        <c:choose>
        <c:when test="${checkboxValues[3]}">
        <input type="checkbox" id="checkbox2" name="filterCheckBox" value="2" onclick="submit()" checked>Some
        </c:when>
        <c:otherwise>
        <input type="checkbox" id="checkbox2" name="filterCheckBox" value="2" onclick="submit()">Some
        </c:otherwise>
        </c:choose>
        <c:choose>
        <c:when test="${checkboxValues[4]}">
        <input type="checkbox" id="checkbox3" name="filterCheckBox" value="3" onclick="submit()" checked>Down
        </c:when>
        <c:otherwise>
        <input type="checkbox" id="checkbox3" name="filterCheckBox" value="3" onclick="submit()">Down
        </c:otherwise>
        </c:choose>
	
	</sf:form>
	</div>
	<div id="inner-home">
		<jsp:include page="homeinner.jsp" />
	</div>
</body>
</html>