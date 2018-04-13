// JavaScript Document

// 真人娱乐菜单展开切换
$(document).ready(function(){
   $(".amusement-menu p").click(
   function(){
     $(this).next(".state").slideToggle();
	 	if($(this).children('.arrow').hasClass('arrow-right')){
	 		$(this).children('.arrow').removeClass('arrow-right').addClass('arrow-down');
	 	}else{
	 		$(this).children('.arrow').addClass('arrow-right');
	 	}

	 
	 }
);
})

// 会员中心弹出框
$(document).ready(function(){
	$(".up-btn").toggle(function(){
		$(".up-box").show()
		},
		function(){
		$(".up-box").hide()
		})
})

// 登录／注册

		
$(document).ready(function(){
	// 判断密码是否输入一致
	$(".password-again").blur(function(){
		if($(".password-again").val()!==$(".password").val()){
		$(".password-icon").removeClass("yes-icon")
		$(".password-icon").addClass("no-icon")
		$(".password-wrong").removeClass("display-none");
			}else{
		$(".password-icon").removeClass("no-icon");
		$(".password-icon").addClass("yes-icon");
		$(".password-wrong").addClass("display-none");
				}
    })
	// 点击重置按钮清除弹出提示
	$(".reset-btn").click(function(){
		$(".password-wrong").addClass("display-none");
		})
	
	// 验证手机号码
	$(".tel-number").blur(function(){
		var isMobile=/^(?:13\d|15\d|18\d)\d{5}(\d{3}|\*{3})$/; //手机号码验证规则
        var isPhone=/^((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;   //座机验证规则
        var dianhua = $(".tel-number").val();                   //获得用户填写的号码值 赋值给变量dianhua
        if(!isMobile.test(dianhua) && !isPhone.test(dianhua)){ //如果用户输入的值不同时满足手机号和座机号的正则
           $(".tel-wrong").removeClass("display-none");
        }else{
	       $(".tel-wrong").addClass("display-none");
	    }
    })
})


// 搜索

$(document).ready(function(){
	$(".search-input").click(function(){
		$(this).val("");
		})
		if($(this).val().length==0){
			$(".search-input").blur(function(){
		$(this).val("账单号、时间、金额、类型名称");
		})
			}
	})
//游戏介绍页面
var i=0;
$(document).ready(function(){
	$(".details-tab span").click(function(){
		$(this).addClass("selected").siblings().removeClass()
		$(".tab").hide().eq($(".details-tab span").index(this)).show(); 
		})
	})

$(document).ready(function(){
	//切换隐藏
	$(".pt-tit").toggle(function(){
		$(".game-nav").hide();
		},function(){
		$(".game-nav").show();
		})
	//切换隐藏
	$(".game-nav span").click(function(){
		$(".game-nav").hide();
		$(".pt-tit div span").hide().eq($(".game-nav span").index(this)).show(); 
		$(".ptgame-tap").hide().eq($(".game-nav span").index(this)).show(); 
		})
	})
//关闭弹出框

$(document).ready(function(){
	$(".close-btn").click(function(){
		$(".alert-window").addClass("display-none")
		$(".alert").addClass("display-none")
		})
	})