package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Customer;
import net.sf.oval.internal.Log;

import org.dom4j.Document;
import org.dom4j.Element;

import play.libs.IO;
import play.mvc.Controller;
import service.CustomerService;
import util.Dom4jUtil;
import util.MD5;
import bsz.exch.service.Plat;

import com.ws.service.PlatService;

public class ClientApp extends Controller{
	static Log log =Log.getLog(ClientApp.class);	
	public static void login(){
	    Map<String,String> req =new HashMap<String,String>();
		try{
		  byte[] bodybyte=IO.readContent(request.current().body);
          StringBuilder sb = new StringBuilder();   
          sb.append(new String(bodybyte));
           log.info(sb.toString());
            Document xmlDoc = Dom4jUtil.String2Doc(sb.toString()); 
            String action=xmlDoc.getRootElement().attributeValue("action");
            Element e=xmlDoc.getRootElement().element("element");
            String id=e.attributeValue("id");
            req.put("action", action);
            req.put("id", id);
            List<Element> properties=e.elements("properties");
            for(Element ee:properties){
          	  req.put(ee.attributeValue("name"), ee.getTextTrim());
            }
            String login_name=req.get("userid");
            String password=req.get("password");
            if(password.length()>10)
            password=password.substring(4, password.length()-6);
            String token=req.get("token").toLowerCase();
            String md5_token= MD5.MD5_SHA(req.get("pcode")+req.get("gcode")+req.get("userid")+req.get("password")+"DFG54fgh45").toLowerCase();
            if(!md5_token.equals(token)){
            	System.out.println(token+" <> "+md5_token);
            	StringBuffer rp=new StringBuffer();
 	            rp.append("<?xml version=\"1.0\"?>");
 	            rp.append("<response action=\"userverf\">");
 	            rp.append("<element id=\""+req.get("id")+"\">");
 	            rp.append("<properties name=\"pcode\">"+req.get("pcode")+"</properties>");
 	            rp.append("<properties name=\"gcode\">"+req.get("gcode")+"</properties>");
 	            rp.append("<properties name=\"status\">-2</properties>");
 	            rp.append("<properties name=\"errdesc\">SystemError</properties>");
 	            rp.append("<properties name=\"username\"></properties>");
                rp.append("<properties name=\"userid\"></properties>");
                rp.append("<properties name=\"actype\">1</properties>");
                rp.append("<properties name=\"pwd\"></properties>");
                rp.append("<properties name=\"gamelevel\"></properties>");
                rp.append("<properties name=\"vip\"></properties>");
 	            rp.append("<properties name=\"domain\">www.8dabet.com</properties>");
 	            rp.append("<properties name=\"ip\">124.248.226.202</properties>");
 	            rp.append("</element>");
 	            rp.append("</response>");
 	            log.info(rp.toString());
 	            renderXml(rp.toString());
            }
        	String ip=IPApp.getIpAddr();
            Customer customer=CustomerService.login(login_name, password,ip);
            String status="0";
            String errdesc="";
			if(customer==null){
				status="1";
				errdesc="用户名或者密码错误!";
			}
			if(!customer.flag){
				status="1";
				errdesc="用户名或者密码错误!";
			}
            StringBuffer rp=new StringBuffer();
            rp.append("<?xml version=\"1.0\"?>");
            rp.append("<response action=\"userverf\">");
            rp.append("<element id=\""+req.get("id")+"\">");
            rp.append("<properties name=\"pcode\">"+req.get("pcode")+"</properties>");
            rp.append("<properties name=\"gcode\">"+req.get("gcode")+"</properties>");
            rp.append("<properties name=\"status\">"+status+"</properties>");
            rp.append("<properties name=\"errdesc\">"+errdesc+"</properties>");
            rp.append("<properties name=\"username\">"+login_name+"</properties>");
            if("0".equals(status)){
            	if(!customer.ag_actived) throw new Exception("the account is not actived");
        		String order_no=""+System.currentTimeMillis();
        		PlatService.transferOutEx(Plat.AG, login_name, ip, "API转出","AG客户端登录", login_name, order_no, null);
        		PlatService.transferIn(Plat.AG, login_name, ip, "API转出", "AG客户端登录", login_name, order_no, null);
	            rp.append("<properties name=\"userid\">"+customer.ag_game+"</properties>");
	            rp.append("<properties name=\"actype\">1</properties>");
	            rp.append("<properties name=\"pwd\">"+customer.ag_pwd+"</properties>");
	            rp.append("<properties name=\"gamelevel\">"+customer.cust_level+"</properties>");
	            rp.append("<properties name=\"vip\">"+(customer.cust_level==6?1:0)+"</properties>");
            }else{
                rp.append("<properties name=\"userid\"></properties>");
                rp.append("<properties name=\"actype\">1</properties>");
                rp.append("<properties name=\"pwd\"></properties>");
                rp.append("<properties name=\"gamelevel\"></properties>");
                rp.append("<properties name=\"vip\"></properties>");
            }
            rp.append("<properties name=\"domain\">www.8dabet.com</properties>");
            rp.append("<properties name=\"ip\">124.248.226.202</properties>");
            rp.append("</element>");
            rp.append("</response>");
            log.info(rp.toString());
            renderXml(rp.toString());
		}catch(Exception e){
			e.printStackTrace();
			 StringBuffer rp=new StringBuffer();
	            rp.append("<?xml version=\"1.0\"?>");
	            rp.append("<response action=\"userverf\">");
	            rp.append("<element id=\""+req.get("id")+"\">");
	            rp.append("<properties name=\"pcode\">"+req.get("pcode")+"</properties>");
	            rp.append("<properties name=\"gcode\">"+req.get("gcode")+"</properties>");
	            rp.append("<properties name=\"status\">-2</properties>");
	            rp.append("<properties name=\"errdesc\">SystemError</properties>");
	            rp.append("<properties name=\"username\"></properties>");
                rp.append("<properties name=\"userid\"></properties>");
                rp.append("<properties name=\"actype\">1</properties>");
                rp.append("<properties name=\"pwd\"></properties>");
                rp.append("<properties name=\"gamelevel\"></properties>");
                rp.append("<properties name=\"vip\"></properties>");
	            rp.append("<properties name=\"domain\">www.8dabet.com</properties>");
	            rp.append("<properties name=\"ip\">124.248.226.202</properties>");
	            rp.append("</element>");
	            rp.append("</response>");
	            log.info(rp.toString());
	            renderXml(rp.toString());
		}
	}
}
