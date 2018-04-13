package controllers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import play.Play;
import util.Dom4jUtil;

public class Test {

	public static void main(String[] args)throws Exception {
        String xml="<?xml version=\"1.0\"?> <request action=\"userverf\"><element id=\"20111224120101020\">"
          		+ "<properties name=\"pcode\">dd</properties><properties name=\"gcode\">aa</properties>"
          		+ "<properties name=\"userid\">dd</properties><properties name=\"password\"></properties>"
          		+ "<properties name=\"token\">ss</properties></element></request> ";
          Map<String,String> req =new HashMap<String,String>();
          Document xmlDoc = Dom4jUtil.String2Doc(xml); 
          String action=xmlDoc.getRootElement().attributeValue("action");
          Element e=xmlDoc.getRootElement().element("element");
          String id=e.attributeValue("id");
          req.put("action", action);
          req.put("id", id);
          List<Element> properties=e.elements("properties");
          for(Element ee:properties){
        	  req.put(ee.attributeValue("name"), ee.getTextTrim());
          }
          
          StringBuffer rp=new StringBuffer();
          rp.append("<?xml version=\"1.0\"?>");
          rp.append("<response action=\"userverf\">");
          rp.append("<element id=\""+req.get("id")+"\">");
          rp.append("<properties name=\"pcode\">"+req.get("pcode")+"</properties>");
          rp.append("<properties name=\"gcode\">"+req.get("gcode")+"</properties>");
          rp.append("<properties name=\"status\">"+req.get("status")+"</properties>");
          rp.append("<properties name=\"errdesc\">"+req.get("errdesc")+"</properties>");
          rp.append("<properties name=\"username\">"+req.get("errdesc")+"</properties>");
          rp.append("<properties name=\"userid\">"+req.get("errdesc")+"</properties>");
          rp.append("<properties name=\"actype\">1</properties>");
          rp.append("<properties name=\"pwd\">"+req.get("pwd")+"</properties>");
          rp.append("<properties name=\"gamelevel\">"+req.get("gamelevel")+"</properties>");
          rp.append("<properties name=\"vip\">"+req.get("vip")+"</properties>");
          rp.append("<properties name=\"domain\">www.8dabet.com</properties>");
          rp.append("<properties name=\"ip\">124.248.226.202</properties>");
          rp.append("</element>");
          rp.append("</response>");
          
          String password="abcd123456eeeeee";
          password=password.substring(4, password.length()-6);
          System.out.println(password);
          for(int i=1;i<1000;i++){
        	  BigDecimal credit= new BigDecimal(i+0.2);
        	  credit=  new BigDecimal((credit.longValue()/10)*10+8);
        	  i=i+10;
        	  System.out.println(credit);
          }
          
          Play.start();

	}

}
