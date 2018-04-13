package controllers;

import net.sf.oval.internal.Log;
import models.Customer;
import models.CustomerStatus;
import models.Discount;
import models.PtGame;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import service.MeiBoService;
import bsz.exch.service.Plat;

import com.ws.service.PlatService;
import com.ws.service.TokenService;

public class MeiBo extends Controller {
	
	@Before
	 public static void before(){
		if(IPApp.isxss()){
			renderText("您的输入存在非法字符!");
		}
		
		if(IPApp.fitter()){
			render("/errors/error.html");
		}
		
	}
	
	static Log log =Log.getLog(MeiBo.class);

	public static String[] visitors=new String[]{"dawvisitor00",
		"dawvisitor01","dawvisitor02","dawvisitor03","dawvisitor04","dawvisitor05",
		"dawvisitor06","dawvisitor07","dawvisitor08","dawvisitor09","dawvisitor10",
		"dawvisitor11","dawvisitor12","dawvisitor13","dawvisitor14","dawvisitor15",
		"dawvisitor16","dawvisitor17","dawvisitor18","dawvisitor19","dawvisitor20",
		"dawvisitor21","dawvisitor22","dawvisitor23","dawvisitor24","dawvisitor25",
		"dawvisitor26","dawvisitor27","dawvisitor28","dawvisitor29","dawvisitor30"};
	
    public static void index() {
    	//request.current().
    	String  reg_code=params.get("s8");
		if(reg_code !=null){
			session.put("reg_code", reg_code);
		}
        render();
    }
    
    public static void html(String method) {
    	if(method != null){
    		if(method.equals("index") || method.equals("reg")){
    			String  reg_code=params.get("s8");
    			if(reg_code !=null){
    				session.put("reg_code", reg_code);
    			}
    		}
    	}
        render("/MeiBo/"+method+".html");
    }
    
    public static void help(String sub){
    	render("/Help/"+sub+".html");
    }
    
    public static void proDetail(Long id){
    	Discount discount=MeiBoService.getDiscount(id);
		if(discount==null) notFound();
		 render(discount);
    }
    
    public static void goToPTGameForIndex(String type,String psite){
    	Plat plat =null;
    	plat=Plat.PT;
    	
     	if(plat.name().equals(Plat.PT.name())&&(!MeiBoService.getPtStatus())){
     		String errorMsg="由于游戏处于维护中，系统拒绝你本次访问请求，详细维护时间请您查看公告。";
    		render("/errors/403.html",errorMsg);
     	}
     	
     	String url="";
    	String login_name=session.get("username");
    	String pturl = (String)Play.configuration.get("static.pt");
    	String gotourl=params.get("gotourl");
    	if(login_name!=null){
    		Customer cust=Customer.getCustomer(login_name);
    		if(cust!=null){
    			if("3".equals(type)){
		    		if(!cust.pt_actived){
		    			String errorMsg="系统拒绝您的访问请求，请您联系在线客服为您处理。";
			    		render("/errors/406.html",errorMsg);	
		    		}
		    		String mode=params.get("mode");
		    		if( mode == null)
		    			mode = "1";
		    		String ptcode=params.get("ptcode");
		    		
		    		String token=TokenService.get(login_name);
		    		if(gotourl == null){
		    			url = pturl+"token="+token+"&login_name="+login_name+"&url=/goToGame/3?mode="+mode+"&ptcode="+ptcode;
		    		}else{
		    			url = pturl+"token="+token+"&login_name="+login_name+"&url="+gotourl;
		    		}
		    		
		    		redirect(url);
		    	}
    		}
    	}
    	
    	if("3".equals(type)){
    		
    		String ptcode=params.get("ptcode");
    		String mode=params.get("mode");
    		if(mode == null)
    			mode = "1";
    		if(gotourl == null){
    			url = pturl+"&url=/goToGame/3?mode="+mode+"&ptcode="+ptcode;
    		}else{
    			url = pturl+"&url="+gotourl;
    		}
    		
    		redirect(url);
    		
    		
    		
    	}
    	
    	
    }
    public static void goToGame(String type,String psite){
    	Plat plat =null;
    	if("2".equals(type))plat=Plat.AG;
    	if("1".equals(type))plat=Plat.BBIN;
    	if("3".equals(type))plat=Plat.PT;
    	if("4".equals(type))plat=Plat.KG;
    	if("5".equals(type))plat=Plat.MG;
    	if("6".equals(type))plat=Plat.VS;
    	if("7".equals(type))plat=Plat.PP;
     	String ip=IPApp.getIpAddr();
     	if((plat.name().equals(Plat.AG.name())&&(!MeiBoService.getAgStatus()))||
     			(plat.name().equals(Plat.BBIN.name())&&(!MeiBoService.getBbinStatus()))||
     			(plat.name().equals(Plat.PT.name())&&(!MeiBoService.getPtStatus()))||
     			(plat.name().equals(Plat.KG.name())&&(!MeiBoService.getKgStatus()))||
     			(plat.name().equals(Plat.VS.name())&&(!MeiBoService.getSbStatus()))||
     			(plat.name().equals(Plat.PP.name())&&(!MeiBoService.getPpStatus()))||
     			(plat.name().equals(Plat.MG.name())&&(!MeiBoService.getMgStatus()))){
     		String errorMsg="由于游戏处于维护中，系统拒绝你本次访问请求，详细维护时间请您查看公告。";
    		render("/errors/403.html",errorMsg);
     	}
     	
    	String url="";
    	String login_name=session.get("username");
    	if(login_name!=null){
			Customer cust=Customer.getCustomer(login_name);
			if(cust!=null){
				CustomerStatus cs = CustomerStatus.NTgetCustomerByName(cust.login_name);
		    	if("1".equals(type)){
		    		if(!cust.bbin_actived){
		    			String errorMsg="系统拒绝您的访问请求，请您联系在线客服为您处理。";
			    		render("/errors/406.html",errorMsg);	
		    		}
		    	
		    		if(cust.auto_transfer_flag){
			    		String order_no=""+System.currentTimeMillis();
	            		PlatService.transferOutEx(Plat.BBIN, login_name, ip, "游戏登入","    登入BBIN网页版", login_name, order_no, null);
	            		PlatService.transferIn(Plat.BBIN, login_name, ip, "游戏登入",   "    登入BBIN网页版", login_name, order_no, null);
		    		}
            		url=PlatService.forward(Plat.BBIN, login_name, ip,psite,"0",params.get("gamecode"));
            		
		    		redirect(url);
		    	}
		    	
		    	if("2".equals(type)){
		    		if(!cust.ag_actived){
		    			String errorMsg="系统拒绝您的访问请求，请您联系在线客服为您处理。";
			    		render("/errors/406.html",errorMsg);	
		    		}
		    		if(cust.auto_transfer_flag){
			    		String order_no=""+System.currentTimeMillis();
	            		PlatService.transferOutEx(Plat.AG, login_name, ip, "游戏登入","    登入AG网页版", login_name, order_no, null);
	            		PlatService.transferIn(Plat.AG, login_name, ip, "游戏登入", "    登入AG网页版", login_name, order_no, null);
		    		}
		    		String gamecode=params.get("gamecode");
            		url=PlatService.forward(Plat.AG, login_name, ip,"","0",gamecode);
		    		redirect(url);
		    	}
		    	
		    	if("6".equals(type)){
		    		if(!cust.sb_actived){
		    			String errorMsg="系统拒绝您的访问请求，请您联系在线客服为您处理。";
			    		render("/errors/406.html",errorMsg);	
		    		}
		    		if(cust.auto_transfer_flag){
			    		String order_no=""+System.currentTimeMillis();
	            		PlatService.transferOutEx(Plat.VS, login_name, ip, "游戏登入","    登入申博网页版", login_name, order_no, null);
	            		PlatService.transferIn(Plat.VS, login_name, ip, "游戏登入", "    登入申博网页版", login_name, order_no, null);
		    		}
		    		String gamecode=params.get("gamecode");
            		url=PlatService.forward(Plat.VS, login_name, ip,request.get().domain,"0",gamecode);
		    		redirect(url);
		    	}
		    	
		    	if("7".equals(type)){
		    		System.out.println(cust.pp_actived);
		    		if(!cust.pp_actived){
		    			String errorMsg="系统拒绝您的访问请求，请您联系在线客服为您处理。";
			    		render("/errors/406.html",errorMsg);	
		    		}
		    		if(cust.auto_transfer_flag){
			    		String order_no=""+System.currentTimeMillis();
	            		PlatService.transferOutEx(Plat.PP, login_name, ip, "游戏登入","    登入PP网页版", login_name, order_no, null);
	            		PlatService.transferIn(Plat.PP, login_name, ip, "游戏登入", "    登入PP网页版", login_name, order_no, null);
		    		}
		    		String gamecode=params.get("gamecode");
            		url=PlatService.forward(Plat.PP, login_name, ip,request.get().domain,"0",gamecode);
		    		redirect(url);
		    	}
		    	
		    	
		    	if("3".equals(type)){
		    		if(!cust.pt_actived){
		    			String errorMsg="系统拒绝您的访问请求，请您联系在线客服为您处理。";
			    		render("/errors/406.html",errorMsg);	
		    		}
		    		String mode=params.get("mode");
		    		if("1".equals(mode) || mode == null){
		    			PlatService.logout(Plat.PT, login_name, ip);
		    			if(cust.auto_transfer_flag){
			    			String order_no=""+System.currentTimeMillis();
		            		PlatService.transferOutEx(Plat.PT, login_name, ip, "游戏登入","    登入PT网页版", login_name, order_no, null);
		            		PlatService.transferIn(Plat.PT, login_name, ip, "游戏登入", "    登入PT网页版", login_name, order_no, null);
		    		    }
	            		
			    		String u =cust.pt_game;
			    		String p=cust.pt_pwd;
			    		String ptcode=params.get("ptcode");
			    		PtGame pt=PtGame.getByCode(ptcode);
			    		String game_name="";
			    		if(pt!=null){
			    			if(pt.cn_name!=null&&!"".equals(pt.cn_name)){
			    				game_name=pt.cn_name;
			    			}else{
			    				game_name=pt.game_name;
			    			}
			    		}
			    		mode = "1";
			    		render("/MeiBo/goToGame_pt.html",u,p,ptcode,game_name,mode);
		    		}else{
		    			String u =visitors[(int)(Math.random()*30)].toUpperCase();
		        		String p="a123a123";
		        		String ptcode=params.get("ptcode");
		        		PtGame pt=PtGame.getByCode(ptcode);
		        		String game_name="";
			    		if(pt!=null){
			    			if(pt.cn_name!=null&&!"".equals(pt.cn_name)){
			    				game_name=pt.cn_name;
			    			}else{
			    				game_name=pt.game_name;
			    			}
			    		}
			    		PlatService.logout(Plat.PT, login_name, ip);
			    		render("/MeiBo/goToGame_pt.html",u,p,ptcode,game_name,mode);	
		    		}
		    	}
		    	
		    	if("4".equals(type)){
		    		if(!cust.kg_actived){
		    			String errorMsg="系统拒绝您的访问请求，请您联系在线客服为您处理。";
			    		render("/errors/406.html",errorMsg);	
		    		}
		    		if(cust.auto_transfer_flag){
			    		String order_no=""+System.currentTimeMillis();
	            		PlatService.transferOutEx(Plat.KG, login_name, ip, "游戏登入","    登入KG网页版", login_name, order_no, null);
	            		PlatService.transferIn(Plat.KG, login_name, ip, "游戏登入", "    登入KG网页版", login_name, order_no, null);
		    		}
            		//url="http://kg.8dabet.org/"+PlatService.forward(Plat.KG, login_name, ip,"","0","");
		    		url="http://kg.win8da.com/"+PlatService.forward(Plat.KG, login_name, ip,"","0","");
		    		redirect(url);
		    	}
		    	
		    	if("5".equals(type)){
		    		if(!cust.mg_actived){
		    			String errorMsg="系统拒绝您的访问请求，请您联系在线客服为您处理。";
			    		render("/errors/406.html",errorMsg);	
		    		}
		    		String gamecode=params.get("gamecode");
		    		String order_no=""+System.currentTimeMillis();
		    		String mode=params.get("mode");
		    		if("1".equals(mode) || mode == null){
		    			if(cust.auto_transfer_flag){
		            		PlatService.transferOutEx(Plat.MG, login_name, ip, "游戏登入","    登入MG网页版", login_name, order_no, null);
		            		PlatService.transferIn(Plat.MG, login_name, ip, "游戏登入", "    登入MG网页版", login_name, order_no, null);
			    		}
			    		
			    		String is_html5=params.get("is_html5");
			    		if(is_html5.equals("false")){
			    			url="https://redirect.contdelivery.com/Casino/Default.aspx?applicationid=1023&serverid=2635&csid=2635&theme=igamingA&usertype=0&gameid="+gamecode+"&sEXT1="+cust.mg_game+"&sEXT2="+cust.mg_pwd+"&ul=zh";
				    		redirect(url);
			    		}else{
//			    			if(gamecode.equals("girlsWithGunsJungleHeatDesktop") || gamecode.equals("scroogeDesktop") || gamecode.equals("santasWildRideDesktop")
//			    					|| gamecode.equals("vinylCountdownDesktop") ||gamecode.equals("highlanderDesktop") ||gamecode.equals("hollyJollyPenguinsDesktop") 
//			    					|| gamecode.equals("wackyPandaDesktop")){
			    				url="https://mobile22.gameassists.co.uk/mobilewebservices_40/casino/game/launch/iGamingA_Desktop/"+gamecode+"/zh-cn/?lobbyURL=https://www.8da2016.com&bankingURL=https://www.8da2016.com&username="+cust.mg_game+"&password="+cust.mg_pwd+"&currencyFormat=%23%2C%23%23%23.%23%23&logintype=fullUPE&xmanEndPoints=https://xplay22.gameassists.co.uk/xman/x.x&host=Desktop";
					    		redirect(url);
			    			//}
//			    			url="https://mobile22.gameassists.co.uk/mobilewebservices_40/casino/game/launch/GoldenTree/"+gamecode+"/zh-cn/?lobbyURL=https://www.8da2016.com&bankingURL=https://www.8da2016.com&username="+cust.mg_game+"&password="+cust.mg_pwd+"&currencyFormat=%23%2C%23%23%23.%23%23&logintype=fullUPE&xmanEndPoints=https://xplay22.gameassists.co.uk/xman/x.x";
//				    		redirect(url);
			    		}
		    			
		    		}else{
		    			String is_html5=params.get("is_html5");
		    			if(is_html5.equals("false")){
		    				url="https://redirect.CONTDELIVERY.COM/Casino/Default.aspx?applicationid=1023&sext1=demo&sext2=demo&csid=16113&serverid=16113&gameid="+gamecode+"&ul=en&theme=igamingA4&usertype=0&variant=instantplay";				   
		    				redirect(url);
			    		}else{
			    			//url="https://mobile2206.gameassists.co.uk/MobileWebServices_40/casino/game/launch/iGamingA4HTML5/"+gamecode+"/zh-cn/?lobbyURL=https://www.8da2016.com&bankingURL=https://www.8da2016.com&currencyFormat=%23%2C%23%23%23.%23%23&ispracticeplay=true&isRGI=true&logintype=fullUPE&xmanEndPoints=https://xplay2206.gameassists.co.uk/xman/x.x";
			    			url="https://mobile22.gameassists.co.uk/mobilewebservices_40/casino/game/launch/iGamingA_Desktop/"+gamecode+"/zh-cn/?lobbyURL=https://www.8da2016.com&bankingURL=https://www.8da2016.com&currencyFormat=%23%2C%23%23%23.%23%23&logintype=fullUPE&xmanEndPoints=https://xplay22.gameassists.co.uk/xman/x.x&host=Desktop&ispracticeplay=true";
				    		  
			    			redirect(url);
			    		}
		    			
		    		
		    		}
		    	
		    		
		    		//url=GameAPI.ForwardGame(cust.mg_game, cust.mg_pwd, "MG",psite,ip);
		    		//url="https://igaminga.gameassists.co.uk/aurora/?gameid="+gamecode+"&sEXT1="+cust.mg_game+"&sEXT2="+cust.mg_pwd+"&ul=zh&theme=igamingA&usertype=0";
		    		
		    	}
		     	
			}
    	}
    	if("1".equals(type)){
    		url=PlatService.forward(Plat.BBIN, visitors[(int)(Math.random()*30)], ip,"","1",params.get("gamecode"));

    		redirect(url);
    	}
    	if("6".equals(type)){
    		url=PlatService.forward(Plat.VS, visitors[(int)(Math.random()*30)], ip,request.get().domain,"1","");

    		redirect(url);
    	}
    	if("2".equals(type)){
    		String gamecode=params.get("gamecode");
    		url=PlatService.forward(Plat.AG, visitors[(int)(Math.random()*30)], ip,"","1",gamecode);
    		redirect(url);
    	}
    	
    	if("7".equals(type)){
    		
    		String gamecode=params.get("gamecode");
    		url=PlatService.forward(Plat.PP, visitors[(int)(Math.random()*30)], ip,request.get().domain,"1",gamecode);
    		redirect(url);
    	}
    	
    	if("3".equals(type)){
    		String u =visitors[(int)(Math.random()*30)].toUpperCase();
    		String p="a123a123";
    		String ptcode=params.get("ptcode");
    		String mode=params.get("mode");
    		if(mode == null)
    			mode = "1";
    		PtGame pt=PtGame.getByCode(ptcode);
    		String game_name="";
    		if(pt!=null){
    			if(pt.cn_name!=null&&!"".equals(pt.cn_name)){
    				game_name=pt.cn_name;
    			}else{
    				game_name=pt.game_name;
    			}
    		}
    		
    		
    		render("/MeiBo/goToGame_pt.html",u,p,ptcode,game_name,mode);
    	}
    	if("4".equals(type)){
    		//url="http://kg.8dabet.org/"+PlatService.forward(Plat.KG, visitors[(int)(Math.random()*30)], ip,"","1","");
    		url="http://kg.win8da.com/"+PlatService.forward(Plat.KG, visitors[(int)(Math.random()*30)], ip,"","1","");
    		redirect(url);
    	}
    	
    	if("5".equals(type)){
    		String u =visitors[(int)(Math.random()*30)];
    		String p="a123a123";
    		String gamecode=params.get("gamecode");
    		String mode=params.get("mode");
    		if("1".equals(mode) || mode == null){
    			String is_html5=params.get("is_html5");
	    		if(is_html5.equals("false")){
	    			url="https://redirect.contdelivery.com/Casino/Default.aspx?applicationid=1023&serverid=2635&csid=2635&theme=igamingA&usertype=0&gameid="+gamecode+"&sEXT1="+u+"&sEXT2="+p+"&ul=zh";
		    		redirect(url);
	    		}else{
	    			
//	    			if(gamecode.equals("girlsWithGunsJungleHeatDesktop") || gamecode.equals("scroogeDesktop") || gamecode.equals("santasWildRideDesktop")
//	    					|| gamecode.equals("vinylCountdownDesktop") ||gamecode.equals("highlanderDesktop") ||gamecode.equals("hollyJollyPenguinsDesktop") 
//	    					|| gamecode.equals("wackyPandaDesktop")){
	    				url="https://mobile22.gameassists.co.uk/mobilewebservices_40/casino/game/launch/iGamingA_Desktop/"+gamecode+"/zh-cn/?lobbyURL=https://www.8da2016.com&bankingURL=https://www.8da2016.com&username="+u+"&password="+p+"&currencyFormat=%23%2C%23%23%23.%23%23&logintype=fullUPE&xmanEndPoints=https://xplay22.gameassists.co.uk/xman/x.x&host=Desktop";
			    		redirect(url);
	    		//	}
	    			
//	    			url="https://mobile22.gameassists.co.uk/mobilewebservices_40/casino/game/launch/GoldenTree/"+gamecode+"/zh-cn/?lobbyURL=https://www.8da2016.com&bankingURL=https://www.8da2016.com&username="+u+"&password="+p+"&currencyFormat=%23%2C%23%23%23.%23%23&logintype=fullUPE&xmanEndPoints=https://xplay22.gameassists.co.uk/xman/x.x";
//		    		redirect(url);
	    		}
    		}else{
    			String is_html5=params.get("is_html5");
    			if(is_html5.equals("false")){
    				url="https://redirect.CONTDELIVERY.COM/Casino/Default.aspx?applicationid=1023&sext1=demo&sext2=demo&csid=16113&serverid=16113&gameid="+gamecode+"&ul=en&theme=igamingA4&usertype=0&variant=instantplay";				    		
    				redirect(url);
	    		}else{
	    			//url="https://mobile2206.gameassists.co.uk/MobileWebServices_40/casino/game/launch/iGamingA4HTML5/"+gamecode+"/zh-cn/?lobbyURL=https://www.8da2016.com&bankingURL=https://www.8da2016.com&currencyFormat=%23%2C%23%23%23.%23%23&ispracticeplay=true&isRGI=true&logintype=fullUPE&xmanEndPoints=https://xplay2206.gameassists.co.uk/xman/x.x";
	    			url="https://mobile22.gameassists.co.uk/mobilewebservices_40/casino/game/launch/iGamingA_Desktop/"+gamecode+"/zh-cn/?lobbyURL=https://www.8da2016.com&bankingURL=https://www.8da2016.com&currencyFormat=%23%2C%23%23%23.%23%23&logintype=fullUPE&xmanEndPoints=https://xplay22.gameassists.co.uk/xman/x.x&host=Desktop&ispracticeplay=true";
	    			redirect(url);
	    		}
    		}
    		
    		//url="https://igaminga.gameassists.co.uk/aurora/?gameid="+gamecode+"&sEXT1="+u+"&sEXT2="+p+"&ul=zh&theme=igamingA&usertype=0";
    		//redirect(url);
    	}
    	
    }
    
    
    public static void ptCenter(){
    	String login_name=session.get("username");
    	if(login_name!=null){
    		redirect("/user.php");
    	}else{
    		redirect("/");
    	}
    }
    
    public static void chpay_return(){
    	String login_name=session.get("username");
    	if(login_name!=null){
    		redirect("/deposit_list.php");
    	}else{
    		redirect("/");
    	}
    }
    
    public static void eggswf(){
    	String domain = (request.secure?"https":"http")+"://"+request.host;
    	if(domain.contains("238da") || domain.contains("668da") || domain.contains("258da") || domain.contains("268da")|| domain.contains("278da")|| domain.contains("298da")||
    			domain.contains("8da188") || domain.contains("8da88") || domain.contains("8da2016") ){
    		if(domain.contains("https")){
    			
    		}else{
    			domain = domain.replaceAll("http", "https");
    		}
    		
    	}
    	log.info("domain"+domain);
    	redirect(domain+"/public/images/score/egg.swf");
    	//redirect(play.Play.configuration.getProperty("static.url")+"/public/images/score/egg.swf");
    }
}