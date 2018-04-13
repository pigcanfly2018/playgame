$(function(){
	//大屏幕整体居中
	initialWidth();
	$(window).resize(function(){
		initialWidth();
	})
});
//大屏幕整体居中
function initialWidth(){
	var screenWidth=$(window).width();
	if(screenWidth>=700){
		$('body,.btns').removeClass('bodyCenter').addClass('bodyCenter');
	}else{
		$('body,.btns').removeClass('bodyCenter');
	}
}
