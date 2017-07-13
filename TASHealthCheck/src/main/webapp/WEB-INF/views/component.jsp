<script type="text/javascript">
	function add() {
		$('#connectionsGroup').append('<div class ="col-sm-2 col-sm-offset-5 cn">' +
					 '<input type="text" class="form-control margin-bottom"' +
					 'name=\"connection\" value=\"${con}\" style = \"text-align:center\"></div>');
		$('#connectionsGroup').append('<div class = "col-sm-1 cb">' +
				 '<input type="checkbox" name="core" value=' + document.getElementsByName("core").length 
		         +'></div>');
	}
	
	function remove() {
		if($(".cb").length > 1){
			$(".cb").last().remove();
			$(".cn").last().remove();
		}
}
</script>
<div class="row">
	<div class="col-sm-1 col-sm-offset-5 text-center">
		<button type="button"
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
