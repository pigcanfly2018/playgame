function idExist(id){
	 if($('#'+id).size()>0){
		 return true;
	 }
	 return false;
}
function mark(){
	if(idExist('mark')){
		$('#mark').show();
	}else{
		$("body").append("<div id='mark'></div>")
		$('#mark').show();
	}
}
function unmark(){
	$('#mark').hide();
}
function doAjaxMark(url,method,params,callback){
	$.ajax({
		type:method,
		url:url,
		data:params,
		dataType:'text',
		timeout:40000,
		beforeSend:function(){
			 mark();
		},
		success:function(msg){
			var data =eval("("+msg+")");
			callback(data);
		},
		error:function(){

		},
		complete:function(){
			unmark();
		}
	});
}