<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Application</title>
<script src="resources/jquery-3.2.1.min.js"></script>
<script type="text/javascript">
	function add() {
		var element = document.createElement("input");
		element.setAttribute("type", "text");
		element.setAttribute("name", "connection");
		element.setAttribute("class","form-control margin-bottom");
		
		var group = document.getElementById("connectionsGroup");
		group.appendChild(element);
	}
	
	function remove() {		
		var group = document.getElementById("connectionsGroup");
		if(group.childElementCount > 2){
			var element = group.lastChild;
			if(element.id != "conlabel"){
				group.removeChild(element);
				element = group.lastChild;
			}
			
			group.removeChild(element);
		}
	}
</script>
</head>
<body>
	<jsp:include page="header.jsp" />
	<sf:form method="POST" commandName="application">
		<div class="container">
			<div class="row">
				<div class="col-sm-3 col-centered  row-bordered">
					<h1 style="text-align: center; font-weight: bold;">Add
						Application</h1>
				</div>
			</div>

			<div class="col-sm-3 col-centered top-buffer">
				<div class="form-group">
					<label class="control-label" for="appName">Application Name</label>
					<sf:input class="form-control" id="appName" name="appName"
						path="appName" type="text" />
					<sf:errors path="appName" />
				</div>
			</div>

			<div class="col-sm-3 col-centered">
				<div class="form-group">
					<label class="control-label" for="url">URL</label>
					<sf:input class="form-control" id="url" name="url" path="url"
						type="text" />
					<sf:errors path="url" />
				</div>
			</div>

			<div class="row">
				<div class="col-sm-3 col-centered">
					<div class="form-group" id="connectionsGroup">
						<label class="control-label" for="connectionsGroup">Connections</label>
						<input type="text" class="form-control margin-bottom"
							name="connection" value="${con}">
						<c:if test="${not empty connectionsAdded}">
							<c:forEach var="con" items="${connectionsAdded}">
								<input type="text" class="form-control margin-bottom"
									name="connection" value="${con}">
							</c:forEach>
						</c:if>
					</div>
				</div>
				<div>
					<c:if test="${not empty connectionInvalid}">
						<c:out value="${connectionInvalid}"></c:out>
					</c:if>
				</div>
			</div>

			<div class="row">
				<div class="col-sm-1 col-sm-offset-5 text-center">
					<button type="button" style="display: block"
						class="btn btn-danger btn-circle btn-lg" name="removeConnection"
						value="removeConnection" onclick="remove();">
						<i class="glyphicon glyphicon-minus"></i>
					</button>
				</div>
				<div class="col-sm-1 text-center">
					<button type="button" style="text-align: center"
						class="btn btn-warning btn-circle btn-lg" name="addConnection"
						value="addConnection" onclick="add();">
						<i class="glyphicon glyphicon-plus"></i>
					</button>
				</div>
			</div>
		</div>

		<div class="row top-buffer">
			<div class="col-sm-2 col-centered">
				<button type="submit" class="btn btn-success btn-block"
					name="submit" value="create">Create</button>
				<button type="submit" class="btn btn-error btn-block" name="submit"
					value="cancel">Cancel</button>
			</div>
		</div>
		</div>
	</sf:form>
	</div>
</body>
</html>