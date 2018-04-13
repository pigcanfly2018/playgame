$(function() {
    $('.regbox input[name=username]').on('keyup blur', function() {
        var uName = $(this).val();
        username(uName);
    });

    function username(v) {
        var userN = v.replace(/^[\d]/, '');
        if (!userN) {
            $('.error').html('请输入正确的用户名！');
            return false;
        } else if (v.length < 4 || v.length > 16) {
            $('.error').html('请输入4-16个字符串！');
            return false;
        } else {
            $('.error').html('');
            return true;
        }
    }
    //普通登录 验证密码
    $('.regbox input[name=password]').on('keyup blur', function() {
        checkPw();
    });

    function checkPw() {
        var pw = $('.regbox input[name=password]');
        if (pw.val().length == 0) {
            $('.error').html('密码不能为空！');
            return false;
        } else if (pw.val().length < 6 || pw.val().length > 16) {
            $('.error').html('输入6-16位数字或字母组合的字符串。');
            return false;
        } else {
            $('.error').html('');
            return true;
        }
    }
    //普通登录  验证验证码
    $('.regbox input[name=verify]').on('keyup blur', function() {
        checkYzm();
    });
    
    $('#submit_pop').click(function(){

    	var loginname=$('.regbox input[name=username]').val();
    	var pwd=$('.regbox input[name=password]').val();
    	var code=$('.regbox input[name=verify]').val();
    	$.ajax({
    		type:'post',
    		url:'/ajax/login',
    		data:{
    			loginName:loginname,
    			pwd:pwd,
    			code:code
    		},
    		dataType:'text',
    		success:function(msg){
    			var data =eval("("+msg+")");
    			if(data.success){
    				
    				 window.location.href="/score.html";
    			}else{
    				
    				$("#gift_layerid02").show();
    				$("#gift_contentid02").text(data.message);
    				$("#verifyImg").click();
    				$("#reg_box").val("");
    				$("#passw").val("");
    				
    			}
    		},
    		error:function(){

    		}
    	});
    });
    
  //重载验证码
    $('#verifyImg').click(function(){
    	$(this).attr("src", "/vericode.png?rd="+new Date().getTime());
    });
   
    function checkYzm() {
        var yzm = $('.regbox input[name=verify]');
        if (yzm.val().length == 0) {
            $('.error').html('验证码不能为空');
            return false;
        } else {
        	 $('.error').html('');
            yzm = $('.regbox input[name=verify]').val();
            // $.ajax({
            //     type: "post",
            //     data: {
            //         "verify": yzm
            //     },
            //     url: "",
            //     success: function(data) {
            //         // 验证码是否正确： 1 正确 0 不正确
            //         if (data == 0) {
            //             fleshVerify();
            //            $('.error').html('验证码错误,请重新输入');
            //             return false;
            //         } else {
            //           $('.error').html('');
            //             return false;
            //         }
            //     }

            // });
        }
    }



    // 金蛋碎的动画
    function eggAction(e) {
        var imgWidth = 214;
        var numImgs = 16;
        var cont = 0;
        var animation = setInterval(function() {
            var img = e.find('img');
            img.css('margin-left', -1 * (cont * imgWidth));
            cont++;
            if (cont == numImgs) {
                clearInterval(animation); //清除
            }
            setTimeout(animation, 60);
        }, 80); 0
    }
        // 金蛋碎的动画
        function hammerAction(g) {
            var imgWidth = 200;
            var anumImgs = 6;
            var acont = 0;
            var Aanimation = setInterval(function() {
                var aimg = g.find('img');
                aimg.css('margin-left', -1 * (acont * imgWidth));
                acont++;
                if (acont == anumImgs) {
                clearInterval(Aanimation); //清除
            }
            setTimeout(Aanimation, 50);
        }, 60);
        }

        $('.egg span').mouseenter(function(){
            $(this).siblings('.eggtip').show();
        }).mouseleave(function(){
            $(this).siblings('.eggtip').hide();
        });

    function p_del(o){
        var msg= o.text();
        if(msg=="确定"){
            return true;
        }else{
            return false;
        }
    }
    $('.egg span.eggpic').click(function() {
    	
    	 if(!isLogin()){
     		$(".login_btn").click();
     		return false;
     	}else{
     		if($(this).attr("eggType")!=null){
     			$("#eggid").val($(this).attr("eggType"));
     			if($(this).attr("eggType")=='金蛋'){
     				$("#scoreid").text(50);
     				if($("#currentscoreid").val()<50){
     					$('.graycover,#gift_layerid02').show();
//     					$("#gift_contentid").text("对不起，您积分不足。");
     					return false;
     				}
     			}else if($(this).attr("eggType")=='银蛋'){
     				$("#scoreid").text(20);
     				if($("#currentscoreid").val()<20){
     					$('.graycover,#gift_layerid02').show();
     					return false;
     				}
     			}else if($(this).attr("eggType")=='白蛋'){
     				$("#scoreid").text(5);
     				if($("#currentscoreid").val()<5){
     					$('.graycover,#gift_layerid02').show();
     					//$('.graycover').show();
     					return false;
     				}
     			}
     			$("#scoreid").text($("#scoreid").text());
     			 $('.confirm,.graycover').show();
     			var hmm = $('.gold');
			        hammerAction(hmm);//锤子动画
			        $(this).addClass("curr");
			        $(this).parent('.egg').siblings().find('span').removeClass('curr');
			       
     		}
     	}
        
       
    });
    
    
    // 砸鸡蛋事件
    $('.egg span.eggpic,.confirm .yeson').on("click",function() {
    		
    		 var yeson = $(this);
    	      if(p_del(yeson)==false){
    	    	  return false;
    	      }else{
    	    	  $('.confirm,.graycover').show();
    	      }
    		$.ajax({  
		          type : "post",  
		          url : "/egg/getLottery",  
		          data : {egg:$("#eggid").val()},  
		          async : false,  
		          success : function(result){ 
		        	  var data = eval("(" + result + ")");  
		        	  
			            if(data.flag==1){
			            	$("#gift_contentid").html("<i>恭喜您！砸中了"+data.messag.name+data.messag.count+"个</i>，您的中奖号码为：<i>"+data.messag.code+"</i>。凭中奖号码联系在线客服立即为您兑换奖品。");
			            	
			            }else{
			            	$("#gift_contentid").text("对不起，您积分不足。");
			            }
		        	 
		          }  
		     });  
    		
    		 var _this = $('.curr').parent('.egg');
    	        _this.siblings('.egg').find('span.eggpic').attr("disabled",true);
    	         eggAction(_this);//金蛋碎方法
    	         setTimeout(function() {
    	            _this.find('span.eggpic').hide();
    	            _this.find('img').show();
    	        }, 300);
    	         setTimeout(function() {
    	            $('.graycover,#gift_layerid').show();
    	        }, 1500);
       
     });
    // 关闭积分消耗提示
    $('.yeson,.noon,.confirm .tipcls').click(function(){
        $('.confirm,.graycover').hide();
    });
    // 关闭中奖弹层
    $('#gift_layerid .tipcls,#gift_layerid .yesbtn').click(function() {
        $('.graycover,#gift_layerid').hide();
        $('.egg').find('img').hide();
        $('.egg').find('span.eggpic').show();
        location.reload();
    });
    $('#gift_layerid02 .tipcls,#gift_layerid02 .yesbtn').click(function(){
    	$('.graycover,#gift_layerid02').hide();
    });
    // 彩蛋砸奖——计算锤子标坐标
    $('.sec_animate').mousemove(function(e) {
        // 距离左、上的距离
        $m_top = $('.sec_animate').offset().top;
        $m_left = $('.sec_animate').offset().left;
        //容器宽高
        $m_h = $('.sec_animate').height();
        $m_w = $('.sec_animate').width();
        //图片的宽高
        $img_w = $(".gold").width();
        $img_h = $(".gold").height();
        //图片实时坐标
        $x = e.pageX - $m_left -($img_w / 5);
        $h = e.pageY - $m_top -$img_h;
        //最大最小坐标
        $max_x = $m_left + $m_w;
        $max_xx = $m_w - $img_w;
        $max_y = $m_top + $m_h;
        $max_yy = $m_h - $img_h;
        $glodtop = -($img_h/2);
        $newY = $m_top - $glodtop;
        // x轴
        if (e.pageX > $max_x) {
            $(".gold").css('left', $max_xx);
        } else if (e.pageX < $m_left) {
            $(".gold").css('left', $m_left);
        } else {
            $(".gold").css('left', $x);
        }
        // y轴
        if (e.pageY > $max_y) {
            $(".gold").css('top', $max_yy);
        } else if (e.pageY < $newY) {
            $(".gold").css('top', $glodtop);
        } else {
            $(".gold").css('top', $h);
        }
    });

});
//是否登录
function isLogin(){
	var flag=false;
	$.ajax({  
          type : "post",  
          url : "/egg/isLogin",  
          data : {},  
          async : false,  
          success : function(result){  
            var data = eval("(" + result + ")");  
            if(data.success){
            	flag= true
            }
          }  
          });  
	return flag;
 
}