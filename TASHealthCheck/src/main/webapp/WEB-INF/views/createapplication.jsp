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
<title>Insert title here</title>
<script src="resources/jquery-3.2.1.min.js"></script>
<script type="text/javascript">
	function add() {
		var element = document.createElement("input");
		element.setAttribute("type", "text");
		element.setAttribute("name", "component");
		
		var group = document.getElementById("componentsGroup");
		group.appendChild(element);
	}
	
	function remove() {		
		var group = document.getElementById("componentsGroup");
		if(group.childElementCount > 0){
			group.removeChild(group.lastChild);
		}
	}
</script>
</head>
<body>
<sf:form method="POST" commandName="application">	
  	<div class="container">
  	      <div class = "row">
              <div class = "col-sm-3 col-centered">
              <h1 style = "text-align:center; font-weight: bold;">Maka the App</h1>
              </div>
          </div>
  	
  		<div class="col-sm-3">	
				<div class="form-group">
					<label for="appName">App Name</label>
			 		<sf:input class="form-control" id="appName" name="appName" path="appName" type="text"/>
			 		<sf:errors path="appName"/>
				</div>
			</div>
		</div>
  		
		
			<div class="col-sm-3">	
				<div class="form-group">
					<label for="url">Url</label>
			 		<sf:input class="form-control" id="url" name="url" path="url" type="text"/>
			 		<sf:errors path="url"/>
				</div>
			</div>
		</div>
	
	
		<div class = "row">
			<button type="button" name="addComponent" value="addComponent" onclick="add();">Add Component</button>
		</div>
		<div class = "row">
			<button type="button" name="removeComponent" value="removeComponent" onclick="remove();">Remove Component</button>
		</div>
		
		<div class = "row">
			<div class="col-sm-6 col-centered">	
				<div class="form-group" id="componentsGroup">
					<c:if test="${not empty componentsAdded}">
						<c:forEach var="com" items="${componentsAdded}">
							<input type="text" name="component" value="${com}">
						</c:forEach>
					</c:if>
				</div>
			</div>
		</div>
		
		<div class = "row">
			<div class = "col-sm-2 col-centered">
				<button type="submit" class="btn btn-success btn-block" name="submit" value="create">Create</button>
				<button type="submit" class="btn btn-error btn-block" name="submit" value="cancel">Cancel</button>
			</div>
		</div>
			
	</div>
	</sf:form>
</body>
</html>