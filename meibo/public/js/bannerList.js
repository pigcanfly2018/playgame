

/*====================
 bannerList======>jsList-function
 =====================*/

function bannerListFn(a,b,c,d,e,f){
    var $bannerMaxWapDom=a;
    var windowWidth=$(window).width();
    var timeShow=0;
    var array=0;
    var timeOff=0;

    var imgPos=$bannerMaxWapDom.find("ul").find("li");

    var cloneOne=imgPos.first().clone();
    $bannerMaxWapDom.find("ul").append(cloneOne);
    $bannerMaxWapDom.find("li").width('1000');
    var liWidth=imgPos.width();
    var liLength=$bannerMaxWapDom.find("li").length;
    $bannerMaxWapDom.find("ul").width(liWidth*(liLength+1));

    var $imgBtnList=b;

    setTimeout(function(i){
        timeShow++;
        (timeShow == 1) ? $bannerMaxWapDom.find("ul").fadeIn() : $bannerMaxWapDom.find("ul").hide();
    },20);

    (e === undefined) ? e=2000 : e=e;

    function imgListBtn (){

        for (var i=0; i < liLength-1; i++ ){
            $imgBtnList.append("<span></span>");
        }

        $imgBtnList.children().eq(0).addClass("current");

        $imgBtnList.children().click(function(){
            var index=$(this).index();
            array=index;
            bannerImgList(index);
            $imgBtnList.children().eq(array).addClass("current").siblings().removeClass("current");

        });

    }

    imgListBtn();

    function bannerImgList(a){
        $bannerMaxWapDom.find("ul").animate({left: -a*windowWidth},400)
    }

    function setTime(){
        timeOff=setInterval(function(){
            // 每隔多少秒切换一次
            array++;
            move();
        },e) //e为时间参数
    }

    (f) ? setTime() : setTime;

    c.stop(true).click(function(){
        // 上一张
        array--;
        move();
    });

    d.stop(true).click(function(){
        // 下一张
        array++;
        move();
    });

    function move(){

        if (array == liLength){
            $bannerMaxWapDom.find("ul").css({left:0});
            array=1;
        }

        if (array == -1){
            $bannerMaxWapDom.find("ul").css({left:-liWidth*(liLength-1)});
            array=liLength-2
        }

        $bannerMaxWapDom.find("ul").stop(true).animate({
            left:-array*liWidth
        });

        (array == liLength-1) ? $imgBtnList.children().eq(0).addClass("current").siblings().removeClass("current") : $imgBtnList.children().eq(array).addClass("current").siblings().removeClass("current");


    }
    $bannerMaxWapDom.mouseenter(function(){
        // 鼠标移动到上面时显示
        c.fadeIn();
        d.fadeIn();
    }).mouseleave(function(){
        // 离开隐藏
        c.fadeOut();
        d.fadeOut();
    });
    // $bannerMaxWapDom.hover(function(){
    //     鼠标移动到上面时停止滚动
    //     clearInterval(timeOff);
    // },function(){(f) ? setTime() : setTime;});
}
bannerListFn(
	    $(".banner"),
	    $(".img-btn-list"),
	    $(".left-btn"),
	    $(".right-btn"),
	    2500,
	    true
	    );























