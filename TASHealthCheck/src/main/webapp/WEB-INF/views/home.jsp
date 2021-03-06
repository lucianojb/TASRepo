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
<link rel="stylesheet" href="<c:url value="/resources/css/filter.css" />">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js" type="text/javascript"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript">
	 $(function() {
		setInterval(function() {
			$.get( "${pageContext.request.contextPath}/homeinner", function( data ) {
				$( "#inner-home" ).html( data );
			});}, (20000 * ${fn:length(payloads)}) + 20000);
	});
	 
	 $(function() {
		    //Multifilter controls
		    $('.multifilter li').click(function() {
		        $(this).toggleClass('active');
		    });

		});
</script>
<script type="text/javascript" src="<c:url value="/resources/jquery.filterizr.min.js"/>"></script>


<title>Home</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	
	<div class="col-sm-3 col-centered">
            <ul class="multifilter" style="text-align:center; display: flex;">
            	<li data-multifilter="4" class="filterbutton btn">Down</li>
            	<li data-multifilter="3" class="filtermiddlebutton btn">Warning</li>
                <li data-multifilter="5" class="filtermiddlebutton btn">Error</li>
                <li data-multifilter="2" class="filtermiddlebutton btn">Up</li>
                <li data-multifilter="1" class="filterlastbutton btn">Off</li>
            </ul>
	</div>
	
	<div class = "col-sm-3 col-centered">
		<input style="width:100%; text-align:center;" type="text" class="filtr-search" name="filtr-search" placeholder="Search..." data-search>
	</div>
	
	<div id="inner-home">
		<jsp:include page="homeinner.jsp" />
	</div>
</body>
</html>