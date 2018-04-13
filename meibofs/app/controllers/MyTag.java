package controllers;

import groovy.lang.Closure;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import models.DictRender;
import models.User;
import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;
import util.Constant;
import util.DateUtil;

@FastTags.Namespace("sb") 
public class MyTag extends FastTags {
	public static void _diff(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		 String s1 = (String)args.get("s1");
		 String s2 = (String)args.get("s2");
		 Integer s1cols = (Integer)args.get("s1cols");
		 Integer s2cols = (Integer)args.get("s2cols");
		 if(s1==null)s1="";
		 if(s2==null)s2="";
		 if(s1cols==null||s1cols==0)s1cols=1;
		 if(s2cols==null||s2cols==0)s2cols=1;
		 if(s1.equals(s2)){
			 out.print("<td colspan="+s1cols+">"+s1+"</td><td colspan="+s2cols+">"+s2+"</td>");
		 }else{
			 out.print("<td colspan="+s1cols+" style='color:red'>"+s1+"</td><td colspan="+s2cols+" style='color:red'>"+s2+"</td>");
		 }
	}
	
	public static void _bijiao(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		 String s1 = (String)args.get("s1");
		 String s2 = (String)args.get("s2");
		 if(! StringUtils.trimToEmpty(s1) .equals(StringUtils.trimToEmpty(s2))){
			 body.setProperty("_flag", true);
		 }else{
			 body.setProperty("_flag", false);
		 }
		 body.call();
	}
	
	public static void _diffdate(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		 Date date1 = (Date)args.get("s1");
		 Date date2 = (Date)args.get("s2");
		 Integer s1cols = (Integer)args.get("s1cols");
		 Integer s2cols = (Integer)args.get("s2cols");
		 String s1=DateUtil.dateToString(date1, "yyyy-MM-dd");
		 String s2=DateUtil.dateToString(date2, "yyyy-MM-dd");
		 if(s1==null)s1="";
		 if(s2==null)s2="";
		 if(s1cols==null||s1cols==0)s1cols=1;
		 if(s2cols==null||s2cols==0)s2cols=1;
		 if(s1.equals(s2)){
			 out.print("<td colspan="+s1cols+">"+s1+"</td><td colspan="+s2cols+">"+s2+"</td>");
		 }else{
			 out.print("<td colspan="+s1cols+" style='color:red'>"+s1+"</td><td colspan="+s2cols+" style='color:red'>"+s2+"</td>");
		 }
	}
	
	/**
	 *  code 1:custform
	 *  code 2:deposit
	 *  code 3:withdraw
	 * @param args
	 * @param body
	 * @param out
	 * @param template
	 * @param fromLine
	 */
	public static void _status(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		 Integer flag = (Integer)args.get("flag");
		 String code = (String)args.get("code");
		 DictRender dictRender =new DictRender();
		 out.print(dictRender.status(flag,code));
	}
	
	public static void _role(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		 String fcode = (String)args.get("fcode");
		 String user = (String)args.get("user");
		 if(User.NTcountByRole(user, fcode)>0){
			 body.setProperty("_flag", true);
		 }else{
			 body.setProperty("_flag", false);
		 }
		 body.call();
	}
}