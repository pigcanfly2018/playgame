/**
 * jQuery jslides 1.1.0
 *
 * http://www.cactussoft.cn
 *
 * Copyright (c) 2009 - 2013 Jerry
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 */
$(function(){
	var numpic = $('#slides').find(".swiper-slide").size()-1;
	var nownow = 0;
	var inout = 0;
	var TT = 0;
	var SPEED = 5000;


	$('#slides').find(".swiper-slide").eq(0).siblings('.swiper-slide').css({'display':'none'});


	var ulstart = '<div id="pagination">',
		ulcontent = '',
		ulend = '</div>';
	ADDLI();
	var pagination = $('#pagination a');
	
	pagination.eq(0).addClass('current')
		
	function ADDLI(){
		//var lilicount = numpic + 1;
		for(var i = 0; i <= numpic; i++){
			ulcontent +=  '<a href="#"></a>';
		}
		
		$('#slides').after(ulstart + ulcontent + ulend);	
	}

	pagination.on('click',DOTCHANGE)
	
	function DOTCHANGE(){
		
		var changenow = $(this).index();
		
		$('#slides').find(".swiper-slide").eq(nownow).css('z-index','900');
		$('#slides').find(".swiper-slide").eq(changenow).css({'z-index':'800'}).show();
		pagination.eq(changenow).addClass('current').siblings('li').removeClass('current');
		$('#slides').find(".swiper-slide").eq(nownow).fadeOut(400,function(){$('#slides').find(".swiper-slide").eq(changenow).fadeIn(500);});
		nownow = changenow;
	}
	
	pagination.mouseenter(function(){
		inout = 1;
	})
	
	pagination.mouseleave(function(){
		inout = 0;
	})
	
	function GOGO(){
		
		var NN = nownow+1;
		
		if( inout == 1 ){
			} else {
			if(nownow < numpic){
			$('#slides').find(".swiper-slide").eq(nownow).css('z-index','900');
			$('#slides').find(".swiper-slide").eq(NN).css({'z-index':'800'}).show();
			pagination.eq(NN).addClass('current').siblings('li').removeClass('current');
			$('#slides').find(".swiper-slide").eq(nownow).fadeOut(400,function(){$('#slides li').eq(NN).fadeIn(500);});
			nownow += 1;

		}else{
			NN = 0;
			$('#slides').find(".swiper-slide").eq(nownow).css('z-index','900');
			$('#slides').find(".swiper-slide").eq(NN).stop(true,true).css({'z-index':'800'}).show();
			$('#slides').find(".swiper-slide").eq(nownow).fadeOut(400,function(){$('#slides').find(".swiper-slide").eq(0).fadeIn(500);});
			pagination.eq(NN).addClass('current').siblings('li').removeClass('current');

			nownow=0;

			}
		}
		TT = setTimeout(GOGO, SPEED);
	}
	
	TT = setTimeout(GOGO, SPEED); 

})