$(document).ready(function () {
	"use strict";
	//旋转角度
	var angles=720;
	//可抽奖次数
	var clickNum = 0;
	//旋转次数
	var rotNum = 0;
	//中奖通知
	var notice = null;
	//中奖位置
	var num = 1;
	//转盘初始化
	var color = ["rgba(255,255,255,0)", "rgba(250,255,255,0)", "rgba(0,200,0,0.5)", "rgba(0,0,0,1)"];
	var info = ["DW手表", "388元", "5%", "288元", "20", "188元", "三只松鼠", "88元", "0.15%", "58元", "周大福", "18元"];
	var info1 = ['男士/女士', '筹码币', '存送优惠', '筹码币', ' 积分', '筹码币', '大礼包', '筹码币', '洗码', '筹码币', '金牌', '筹码币'];

	canvasRun();
	$('#BTN_PrizeDraw').bind('click', function () {
		$('#BTN_PrizeDraw').attr("disabled", true);
		clickNum = $("#cishu").attr('times');
		if (clickNum >= 1) {
			//转盘旋转过程“开始抽奖”按钮无法点击
			
			//可抽奖次数减一
			clickNum = clickNum - 1;
			
			runCupwithout();
			$('#BTN_PrizeDraw').find("p").text("剩余"+clickNum+"次");
			$("#cishu").attr('times',clickNum) ;
			$.ajax({
				type:'post',
      			url:'/ajax/wuyiGift',
	 			data:{},
	 			dataType:'text',
	 			success:function(msg){
	 				var data =eval("("+msg+")");
	 				if(data.success){
	 					var msg=data.message;
	 					num = msg;
	 					//转盘旋转
	 					runCup();
	 					
	 					//旋转次数加一
	 					rotNum = rotNum + 1;
	 					//$("#hongbao_credit").html(msg);
					}else{
						alert(data.message);
					}
	 			},
	 			error:function(){
	 			}
	 		});
			
			
			//“开始抽奖”按钮无法点击恢复点击
			setTimeout(function () {
				alert(notice);
				angles= angles + 720;
				$('#BTN_PrizeDraw').removeAttr("disabled", true);
			}, 10000);
		} else {
			alert("亲，抽奖次数已用光,请你明日再接再厉！");
			$('#BTN_PrizeDraw').removeAttr("disabled", true);
		}
	});

	//转盘旋转
	function runCup() {
		probability();
		var degValue = 'rotate(' + angles + 'deg' + ')';
		$('#Turntable').css('-o-transform', degValue); //Opera
		$('#Turntable').css('-ms-transform', degValue); //IE浏览器
		$('#Turntable').css('-moz-transform', degValue); //Firefox
		$('#Turntable').css('-webkit-transform', degValue); //Chrome和Safari
		$('#Turntable').css('transform', degValue);
	}
	
	//转盘旋转
	function runCupwithout() {
		//probability();
		var degValue = 'rotate(' + angles + 'deg' + ')';
		$('#Turntable').css('-o-transform', degValue); //Opera
		$('#Turntable').css('-ms-transform', degValue); //IE浏览器
		$('#Turntable').css('-moz-transform', degValue); //Firefox
		$('#Turntable').css('-webkit-transform', degValue); //Chrome和Safari
		$('#Turntable').css('transform', degValue);
	}



	function probability() {
		//获取随机数
		//var num = parseInt(Math.random() * (11 - 0 + 0) + 0);
		//概率
		//alert(num)
		if (num == 0) {
			angles = 2160 * rotNum + 1800;
			notice ="恭喜您获得了“"+ info[0] + info1[0] +"”的奖励!";
		}
		//概率
		else if (num == 1) {
			angles = 2160 * rotNum + 1830;
			notice ="恭喜您获得了“"+ info[11] + info1[11] +"”的奖励!";
		}
		//概率
		else if (num == 2) {
			angles = 2160 * rotNum + 1860;
			notice ="恭喜您获得了“"+ info[10] + info1[10] +"”的奖励!";
		}
		//概率
		else if (num == 3) {
			angles = 2160 * rotNum + 1890;
			notice ="恭喜您获得了“"+ info[9] + info1[9] +"”的奖励!";
		}
		//概率
		else if (num == 4) {
			angles = 2160 * rotNum + 1920;
			notice ="恭喜您获得了“"+ info[8] + info1[8] +"”的奖励!";
		}
		//概率
		else if (num == 5) {
			angles = 2160 * rotNum + 1950;
			notice ="恭喜您获得了“"+ info[7] + info1[7] +"”的奖励!";
		}
		//概率
		else if (num == 6) {
			angles = 2160 * rotNum + 1980;
			notice ="恭喜您获得了“"+ info[6] + info1[6] +"”的奖励!";
		}
		//概率
		else if (num == 7) {
			angles = 2160 * rotNum + 2010;
			notice ="恭喜您获得了“"+ info[5] + info1[5] +"”的奖励!";
			//概率
		} else if (num == 8) {
			angles = 2160 * rotNum + 2040;
			notice ="恭喜您获得了“"+ info[4] + info1[4] +"”的奖励!";
		}
		//概率
		else if (num == 9) {
			angles = 2160 * rotNum + 2070;
			notice ="恭喜您获得了“"+ info[3] + info1[3] +"”的奖励!";
		}
		//概率
		else if (num == 10) {
			angles = 2160 * rotNum + 2100;
			notice ="恭喜您获得了“"+ info[2] + info1[2] +"”的奖励!";
		}
		//概率
		else if (num == 11) {
			angles = 2160 * rotNum + 2130;
			notice ="恭喜您获得了“"+ info[1] + info1[1] +"”的奖励!";
		}
	}

	//绘制转盘
	function canvasRun() {
		var Turntable = document.getElementById('Turntable');
		var TurntableCont = Turntable.getContext('2d');
		createCircle();
		createCirText();

		//外圆
		function createCircle() {
			var startAngle = 0; 
			var endAngle = 0; 
			
			for (var i = 0; i < 12; i++) {
				startAngle = Math.PI * (i / 6 - 1 / 12);
				endAngle = startAngle + Math.PI * (1 / 6);
				TurntableCont.save();
				TurntableCont.beginPath();
				TurntableCont.arc(300, 300, 240, startAngle, endAngle, false);
				TurntableCont.lineWidth = 350;
				if (i % 2 === 0) {
					TurntableCont.strokeStyle = color[0];
				} else {
					TurntableCont.strokeStyle = color[1];
				}
				TurntableCont.stroke();
				TurntableCont.restore();
			}
		}

		//各奖项
		function createCirText() {
			TurntableCont.textAlign = 'center';
			TurntableCont.textBaseline = 'middle';
			TurntableCont.fillStyle = color[3];
			var step = 2 * Math.PI / 12;
			for (var i = 0; i < 12; i++) {
				TurntableCont.save();
				TurntableCont.beginPath();
				TurntableCont.translate(300, 300);
				TurntableCont.rotate(i * step);
				TurntableCont.font = "24px Impact,Arial Black,Microsoft YaHei";
				TurntableCont.fillStyle = color[3];
				TurntableCont.fillText(info[i], 0, -195, 100);
				TurntableCont.textAlign = "center";
				TurntableCont.font = " 16px Arial,Microsoft YaHei";
				TurntableCont.fillText(info1[i], 0, -170, 100);
				TurntableCont.closePath();
				TurntableCont.restore();
			
				
			}

		}
	}
});
