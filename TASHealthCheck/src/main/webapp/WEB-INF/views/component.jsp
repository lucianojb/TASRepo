<script type="text/javascript">
	function add() {
		var element = document.createElement("input");
		element.setAttribute("type", "text");
		element.setAttribute("name", "connection");
		element.setAttribute("class","form-control margin-bottom");
		
		var checkElement = document.createElement("input");
		checkElement.setAttribute("type", "checkbox");
		checkElement.setAttribute("name", "core");
		checkElement.setAttribute("value", document.getElementsByName("core").length);
		
		var group = document.getElementById("connectionsGroup");
		group.appendChild(element);
		group.appendChild(checkElement);
	}
	
	function remove() {		
		var group = document.getElementById("connectionsGroup");
		if(group.childElementCount > 3){
			var element = group.lastChild;
			if(element.tagName != "INPUT"){
				group.removeChild(element);
				element = group.lastChild;
			}
			group.removeChild(element);
			element = group.lastChild;
			if(element.tagName != "INPUT"){
				group.removeChild(element);
				element = group.lastChild;
			}
			group.removeChild(element);
		}
}
</script>
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
