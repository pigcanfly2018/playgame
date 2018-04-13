
// 总奖池遮罩效果
// 当鼠标指针进入（穿过）元素时
/*$('.p2box .con').mouseenter(function() {
	$(this).children(".laycon").animate({
		opacity: "1"
	}, "normal").css({
    display: "block"
  });
}).mouseleave(function() {
	// 当鼠标指针离开元素时
	$(this).children(".laycon").animate({
		opacity: "0"
	}, "normal").css({
    display: "none"
  });
});*/
// app下载选项卡
$(".d-nav a:eq(1)").click(function(){
	$(this).addClass('onup').siblings().removeClass('onup');
	$(this).parents('.down').find('.dw1').show();
	$(this).parents('.down').find('.dw2').hide();
});
$(".d-nav a:eq(2)").click(function(){
	$(this).addClass('onup').siblings().removeClass('onup');
	$(this).parents('.down').find('.dw2').show();
	$(this).parents('.down').find('.dw1').hide();
});

// 热门推荐选项卡
function setContentTab(name, curr, n) {
	for (i = 1; i <= n; i++) {
		var menu = document.getElementById(name + i);
		var cont = document.getElementById("con_" + name + "_" + i);
		menu.className = i == curr ? "current" : "";
		if (i == curr) {
			cont.style.display = "block";
		} else {
			cont.style.display = "none";
		}
	}
}
// 显示进入按钮
$('.lipic').mouseenter(function() {
	$(this).children(".intolay").animate({
		opacity: "1",
		bottom: "0"
	}, "normal");
}).mouseleave(function() {
	// 当鼠标指针离开元素时
	$(this).children(".intolay").animate({
		opacity: "0",
		bottom: "-100%"
	}, "normal");
});

// 老虎机热门推荐
$(".tabcon>a").mouseenter(function(){
  $(this).find('.li_lay').show();
}).mouseleave(function(){
  $(this).find('.li_lay').hide();
});

$(".navtb a").hover(function(){
  //获取当前值
  var currentBackNum = parseInt($(this).index());
  $(this).addClass("on").siblings().removeClass("on");
  $(this).parents('.l_p3box').children(".tabmain").css("display","none");
  $(this).parents('.l_p3box').find('.tab0'+currentBackNum).show();
});

// 真人
$(".l_p4box .p4nav").hover(function(){
  //获取当前值
  var currentBackNum = parseInt($(this).index()+1);
  $(this).addClass("curt").siblings().removeClass("curt");
  $(this).parents('.p4box').children(".r_p4box").css("display","none");
  $(this).parents('.p4box').find('.box0'+currentBackNum).show();
});

$(".box01 li").mouseenter(function(){
  $(this).find('.intogray').show();
}).mouseleave(function(){
  $(this).find('.intogray').hide();
});


$(".tiyu,.BBINcon,.KGcon,.AGcon,.PTcon").mouseenter(function(){
  $(this).find('.zrl').show();
}).mouseleave(function(){
  $(this).find('.zrl').hide();
});

// 左侧浮动栏——惊喜天天送图片关闭
// $('.cls').click(function(){
//   $(".Rides_L_game").fadeOut();
// });

$('.r_chiose_li a').click(function(){
  $(this).addClass("onup").siblings().removeClass('onup');
  var hrefImg = $(this).attr('vv-src');
  // alert(hrefImg);
  $('#PayIcon_WeChat').attr('src',hrefImg);
});

//适配不同尺寸图片大小
//图片加载
imgLoad = function() {
  $('[vivi-src]').each(function() {
    var t = $(this),
    tt = t.offset().top,
    wt = $(window).scrollTop();
    t.is('img') ? t.attr('src', t.attr('vivi-src')) : t.bgImage();
    if (t.is('img') && t.find('img').length>0) {
      t.removeAttr('vivi-src');
    };
  });
};

(function(e) {
  "use strict";
  //图片自适应高宽填充
  e.fn.bgImage = function(c) {
    return this.each(function() {
      var t = e(this),
      i = new Image();
      i.src = t.attr('vivi-src');
      i.onload = function() {
        t.html('<img src="' + i.src + '" onclick="zoom(this, this.src, 0, 0,0)" />');
        if (i.width > i.height) {
          t.find('img').css({
            'height': t.height(),
            'display': 'block',
            'position': 'absolute',
            'top': '0'
          });
          t.find('img').css({
            'left': -((t.find('img').width() - t.width()) / 2)
          });
          // 修正页面切换后图片自适应高度填充方法
          if (t.find('img').width() == 0) {
            t.find('img').remove();
          }else{
            t.removeAttr('vivi-src');
          }
        } else if (i.width < i.height) {
          t.find('img').css({
            'width': t.width(),
            'display': 'block',
            'position': 'absolute',
            'left': '0'
          });
          t.find('img').css({
            'top': -((t.find('img').height() - t.width()) / 2)
          });
          // 修正页面切换后图片自适应高度填充方法
          if (t.find('img').height() == 0) {
            t.find('img').remove();
          }else{
            t.removeAttr('vivi-src');
          }
        } else {
          t.find('img').css({
            'width': t.width(),
            'display': 'block'
          });
          // 修正页面切换后图片自适应高度填充方法
          t.removeAttr('vivi-src');
        }
        c && c();
      };
      i.onerror = function() {
        t.html('<img src="//ww2.sinaimg.cn/mw690/70f52a24gw1f86t2hn4s3j205f05fglh.jpg" />');
        t.find('img').css({
          'width': t.width(),
          'display': 'block'
        });
        // 修正页面切换后图片自适应高度填充方法
        t.removeAttr('vivi-src');
        c && c();
      };
    });
};
})(jQuery);

var zoomstatus = 1;
function zoom(obj, zimg, nocover, pn, showexif) {
  $F('_zoom', arguments);
}
// 适配不同大小图片——触发项
$(function() {
  imgLoad();
  // winScroll(imgLoad);
});

// 中奖榜单——显示
if (!document.all) {
  $(window).scroll(function(){
    if( $(window).scrollTop() >= 0){
      if($(window).scrollTop() + $(window).height() + $('.user_scroll').height()>=$('body>#foot').offset().top){
          $('.user_scroll').removeClass('addfloat');
          return false;
        }
        $('.user_scroll').addClass('addfloat');
        return false;
    }else{
         $('.user_scroll').removeClass('addfloat');
        return false;
      }
  });
}

//显示返回顶部按钮
$(window).scroll(function(){
  if($(window).scrollTop()>400){
    $('.gotop').fadeIn();
  }else{
    $('.gotop').fadeOut();
  }
});
// 返回顶部
$('.gotop').click(function(){
   $("body,html").animate({
     scrollTop: 0
    }, 500);
});