<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
	
<%@ page session="false"%>
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
<script type="text/javascript" src="<c:url value="/resources/jquery.filterizr.min.js"/>"></script>

<script type="text/javascript">
        $(function() {
            //Initialize filterizr with default options
            $('.filtr-container').filterizr();
        });
    </script>

<title>Home</title>
</head>
<body>

	<jsp:include page="header.jsp" />
	
	<div class="row">
            <ul class="simplefilter">
                <li class="active" data-multifilter="all">All</li>
                <li data-multifilter="1">Off</li>
                <li data-multifilter="2">Up</li>
                <li data-multifilter="3">Some</li>
                <li data-multifilter="4">Down</li>
                <li data-multifilter="5">Error</li>
            </ul>
        </div>
	
	<div id="inner-home">
		<jsp:include page="homeinner.jsp" />
	</div>
</body>
</html>