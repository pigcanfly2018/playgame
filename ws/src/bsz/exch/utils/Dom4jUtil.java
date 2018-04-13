package bsz.exch.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class Dom4jUtil {
	public static Document String2Doc(String s) throws DocumentException{
		return DocumentHelper.parseText(s);//将字符串转为XML

	}

	public static String doc2String(Document document){
		String s="";
	       try{
	        ByteArrayOutputStream out=new ByteArrayOutputStream();
	        OutputFormat format=new OutputFormat("  ",true,"UTF-8");
	        XMLWriter writer=new XMLWriter(out,format);
	        writer.write(document);
	        s=out.toString("UTF-8");
	       }catch(IOException e){
	    	   	e.printStackTrace();
	      }
	      return s;
		
	}
}
