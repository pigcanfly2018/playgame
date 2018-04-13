// JavaScript Document

//首页banner事件
function home_banner(){
	
	//初始定义
	var banner = $("#pics"),
		noArrow = $(".no-arrow"),
		noNum = $(".no-num");//获取banner容器，以便插入切换按钮和底部编号容器
	
	banner.each(function(i){
						 
		var imgArea = banner.eq(i).find(".imgArea"),//获取图片容器
		img = imgArea.find(".item"),//获取所有图片
		thisIndex = 0,//当前图片编号
		st,//settimeout
	    delay = 4000;//默认时间间隔4秒
		//初始化区域
		img.eq(thisIndex).addClass("current").animate({"margin-left":0},0);//由于默认所有图片不显示，因此需要初始设置显示第一张图片
		var bannerCount = img.size(),//获取图片数量
			bannerWidth = banner.eq(i).width();//获取图片宽度
	
		//下一帧编号
		var nextIndex = function(){
			var n = (thisIndex + 1 < bannerCount) ? (thisIndex + 1) : 0;
			return(n);
		}
		
		//前一帧编号
		var prevIndex = function(){
			var n = (thisIndex > 0) ? (thisIndex - 1) : (bannerCount - 1);
			return(n);
		}
	
		//大于1张图时，显示左右箭头、底部编号
		if(bannerCount>1){
			
			//插入左右箭头和底部编号容器
			var arrow = $("<div class=\"arrow\"></div>").appendTo(banner.eq(i)),
				arrow_l = $("<a href=\"javascript:;\" class=\"arrow-left\"><</a>").click(function(){toRight(prevIndex())}).appendTo(arrow),//左箭头
				arrow_r = $("<a href=\"javascript:;\" class=\"arrow-right\">></a>").click(function(){toLeft(nextIndex())}).insertAfter(arrow_l),//右箭头
				bar = $("<div class=\"number\"></div>").appendTo(banner.eq(i));//底部编号容器
			//插入底部编号
			img.each(function test(index, element) {
				$("<b></b>").appendTo(bar).mouseover(function(){toOption(index)});
			});
			//初始化底部编号
			var num = banner.eq(i).find(".number").find("b");
			var arrowWidth = banner.eq(i).find(".number").outerWidth() / 2;
			bar.attr("style","margin-left:-" +arrowWidth + "px;" )
			num.eq(thisIndex).addClass("current");//初始化设置第一个编号标红
		
			start();//开始切换
		}
	
		//切换至第index张图
		//方法一，自动向左切换
		function toLeft(index){
			if(img.is(":animated") ){return;}
			else{
			clearTimeout(st);//清除timeout
			var currentItem = imgArea.find(".current");
			currentItem.animate({"margin-left":-bannerWidth},400).removeClass("current")
			currentItem.siblings().attr("style","margin-left:" + bannerWidth + "px;");//当前图片0秒滚动到右侧
			img.eq(index).animate({"margin-left":0},400).addClass("current");
			num.removeClass("current").eq(index).addClass("current");//编号切换
			thisIndex = index;//当前图片编号
			start();//开始下一轮切换
			}
		}
		
		//方法二，自动向右切换
		function toRight(index){
			if(img.is(":animated") ){return;}
			else{
			clearTimeout(st);//清除timeout
			var currentItem = imgArea.find(".current");
			currentItem.animate({"margin-left":bannerWidth},400).removeClass("current");//当前的图片1秒滚动到右侧
			currentItem.siblings().attr("style","margin-left:-" + bannerWidth + "px;");
			img.eq(index).animate({"margin-left":0},400).addClass("current");//选中图片1秒滚动到舞台
			num.removeClass("current").eq(index).addClass("current");//编号切换
			thisIndex = index;//当前图片编号
			start();//开始下一轮切换
			}
		}
		
		//方法三，判断切换
		function toOption(index){
			var bClass = num.eq(index).attr("class");
			if(bClass == "current"){
				return;//如果当前编号已是选中状态，则不处理
			}else if(index > thisIndex){
				toLeft(index);//如果当前的选择大于当前的展示，则调用向左切换
			}else if(index < thisIndex){
				toRight(index);//如果当前的选择小于当前的展示，则调用向右切换
			}
		}
		
		//定时切换
		function start(){st = setTimeout(function(){toLeft(nextIndex())},delay);}
		
		//绑定鼠标悬浮事件
		banner.eq(i).mouseover(function(){
			var arrowItem = $(this).find(".arrow");//编号
			arrowItem.show();
			clearTimeout(st);//清除timeout
		}).mouseleave(function(){
			var arrowItem = $(this).find(".arrow");//编号
			arrowItem.hide();
			start();//开始下一轮切换
		})
	})
	
	noArrow.each(function(i){
		var arrowArea = $(this).find(".arrow");
		arrowArea.addClass("hidden");
	})
	noNum.each(function(i){
		var numArea = $(this).find(".number");
		numArea.addClass("hidden");
	})
}
