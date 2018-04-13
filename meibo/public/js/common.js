var nowID=0;//动画显示到第几个效果
var index=0;//当前动画效果显示index
var startIndex=0;//从第几个开始
$(function() {
	//actionGoing(4,1);//第一个参数表示总共出现到第几个动画，第二个参数表示从第几个动画开始
	
	//开门大吉
	$('.content-kmdj .sp-km-btn').on('click',function(){
		$('.content-kmdj .sp-km-start').addClass('active');
		setTimeout(function(){
			$('.content-kmdj .sp-km-left').addClass('active');
			$('.content-kmdj .sp-km-right').addClass('active');
		},500);
		setTimeout(function(){
			$('.content-kmdj .sp-km-go').addClass('active');
			$('.content-kmdj .sp-km-btn').fadeOut();
			$('.content-kmdj .sp-km-start').fadeOut();
		},1500);
		
		$.ajax({
			type:'post',
			url:'/ajax/getApplyZhongqiu',
			data:{level:1},
			dataType:'text',
			success:function(msg){
				
			},
			error:function(){
			}
		});
		
		
		setTimeout(function(){
			$('.content-kmdj').hide();
		},2500);
		setTimeout(function(){
			if(nowID>1){
				$('.action').eq(1).fadeIn(500);
				imgCenter();
				index=1;
			}
		},5000);
	});
	//金口大开
	//眨眼
	setInterval(function(){
		setTimeout(function(){
			$('.content-jkdk .sp-lion-eyes').show();
		},100);
		setTimeout(function(){
			$('.content-jkdk .sp-lion-eyes').hide();
		},200);
		setTimeout(function(){
			$('.content-jkdk .sp-lion-eyes').show();
		},300);
		setTimeout(function(){
			$('.content-jkdk .sp-lion-eyes').hide();
		},400);
		setTimeout(function(){
			$('.content-jkdk .sp-lion-eyes').show();
		},1000);
		setTimeout(function(){
			$('.content-jkdk .sp-lion-eyes').hide();
		},1300);
	},3000)
	$('.content-jkdk .sp-jk-go').on('click',function(){
		$('.content-jkdk .sp-couplet').animate({
			'height':365
		});
		$('.content-jkdk .sp-jk-go').animate({
			'top':420
		});
		
		$.ajax({
			type:'post',
			url:'/ajax/getApplyZhongqiu',
			data:{level:2},
			dataType:'text',
			success:function(msg){
				
			},
			error:function(){
			}
		});
		
		setTimeout(function(){
			$('.content-jkdk').hide();
			if(nowID>2){
				$('.action').eq(2).fadeIn(500);
				//花开富贵
				autoGo(status);
				index=2;
			}
			imgCenter();
		},2000);
	});
	//花开富贵
	$('.content-hkfk .sp-btn .sp-img-btn').on('click',function(){
		$('.content-hkfk .sp-btn').hide();
		$('.content-hkfk .sp-light').css('transform','scale(1)');
		
		$.ajax({
			type:'post',
			url:'/ajax/getApplyZhongqiu',
			data:{level:3},
			dataType:'text',
			success:function(msg){
				
			},
			error:function(){
			}
		});
		
		
		setTimeout(function(){
			$('.content-hkfk').hide();
			if(nowID>3){
				$('.action').eq(3).fadeIn(500);
				//可爱月兔
				$('.content-kayt .sp-ka-start').addClass('active');
				setTimeout(function(){
					$('.content-kayt .sp-btn').animate({
						'opacity':1
					});
				},1000);
			};
			index=3;
			imgCenter();
		},2000);
	});
	//可爱月兔
	$('.content-kayt .sp-btn .sp-img-btn').on('click',function(){
		$('.content-kayt .sp-btn').hide();
		$('.content-kayt .sp-ka-txt').addClass('active');
		
		$.ajax({
			type:'post',
			url:'/ajax/getApplyZhongqiu',
			data:{level:4},
			dataType:'text',
			success:function(msg){
				
			},
			error:function(){
			}
		});
		
		setTimeout(function(){
			$('.content-kayt').hide();
			if(nowID>4){
				$('.action').eq(4).fadeIn(500);
				index=4;
				//嫦娥奔月
				$('.content-ceby').addClass('active');
				imgCenter();
			}
		},2000);
	});
	//嫦娥奔月
	$('.content-ceby').addClass('active');
	setTimeout(function() {
		$('.content-ceby .sp-start').animate({
			'opacity': 0
		}, 500);
		$('.content-ceby .sp-stop').animate({
			'opacity': 1
		}, 500);
		$('.content-ceby .sp-btn').animate({
			'opacity': 1
		}, 500);
	}, 3000);
	lightGo();

	$('.content-ceby .sp-btn .sp-img-btn').on('click', function() {
		
		$.ajax({
			type:'post',
			url:'/ajax/getApplyZhongqiu',
			data:{level:5},
			dataType:'text',
			success:function(msg){
				
			},
			error:function(){
			}
		});
		
		showCouplet();
	});
});

//显示对联
function showCouplet() {
	$('.content-ceby .sp-btn').animate({
		'opacity': 0
	}, 500);
	$('.content-ceby .sp-stop').animate({
		'opacity': 0
	}, 500);
	$('.content-ceby .sp-go').animate({
		'opacity': 1
	}, 500);
	$('.content-ceby .sp-go').css('z-index', 5);
	$('.content-ceby .sp-hand').animate({
		'opacity': 1
	}, 0);
	$('.content-ceby .sp-couplet').animate({
		'opacity': 1
	}, 500);
	setTimeout(function() {
		$('.content-ceby').hide();
	}, 5000);
}
//自动播放
function autoGo(bl){
	for(var i=0;i<15;i++){
		var w=Math.random()*260;
		var h=Math.random()*160;
		var rd=Math.random()+0.3;
		$('.content-hkfk').append('<span class="sp-flower" style="margin-left: '+w+'px;margin-top:'+h+'px;transform: scale('+rd+');"><i class="i-img"></i></span>');
	}
	var c=0;
	var f1;
	setInterval(f1=function(){
		$('.content-hkfk .sp-flower').eq(c++).addClass('active');
		if(c>$('.content-hkfk .sp-flower').length){
			clearInterval(f1);
			$('.content-hkfk .sp-btn').animate({
				'opacity':1
			});
		}
	},100);
}
//按钮光环发散
function lightGo() {
	var i = 0.9;
	setInterval(function() {
		$('.sp-btn').append('<span class="light"><img src="public/images/zhongqiu/pt-ce-btn-light.png" style="transform: scale(' + i + ');"></span>');
		i += 0.05;
		if(i > 1.3) {
			i = 0.9;
			$('.sp-btn .light').remove();
		}
	}, 100)
}
function imgCenter(){
	//浮动层居中
	var screeH=$(window).height();
	var dialogShowH=$('.dialogShow').height();
	var marginTop=(screeH-dialogShowH)/2;
	if(marginTop<0){
		marginTop=20;
	}else{
		marginTop-=50;
	}
	
	var left=$('.action').eq(index).width();
	$('.dialogShow').css('margin-top',marginTop);
	$('.dialogShow').css('margin-left',-(left/2));
}
//初始化动画个数
function nowGo(num,num1){
	nowID=num;
	startIndex=num1-1;
}
//动画开始  第一个参数allCount表示总共出现到第几个动画，第二个参数startNum表示从第几个动画开始
function actionGoing(allCount,startNum){
	
	setTimeout(this,3000)
	//初始化参数
	nowGo(allCount,startNum);
	if(nowID>=1){
		if(nowID>=(startIndex+1)){
			$('.action').eq(startIndex).fadeIn(1000);
			index=startIndex;
			switch(startIndex){
				case 2:
					//花开富贵
					autoGo(status);
					break;
				case 3:
					//可爱月兔
					$('.content-kayt .sp-ka-start').addClass('active');
					setTimeout(function(){
						$('.content-kayt .sp-btn').animate({
							'opacity':1
						});
					},1000);
					break;
				case 5:
					//嫦娥奔月
					$('.content-ceby').addClass('active');
					setTimeout(function() {
						$('.content-ceby .sp-start').animate({
							'opacity': 0
						}, 500);
						$('.content-ceby .sp-stop').animate({
							'opacity': 1
						}, 500);
						$('.content-ceby .sp-btn').animate({
							'opacity': 1
						}, 500);
					}, 3000);
					lightGo();
			}
		}
	}
	imgCenter();
}
