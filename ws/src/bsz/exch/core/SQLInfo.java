package bsz.exch.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLInfo {
	
	private String org_sql;
	
	private String sql;
	
	private List<String> params =new ArrayList<String>();
	
	
	private  boolean process =false; 
	
	public static String pstr="[#]{1,1}([^#]*)[#]{1,1}";
	
	public static Pattern p = Pattern.compile(pstr); 
	
	public SQLInfo(String org_sql){
		this.org_sql=org_sql;
	}
	
	public String getSql(){
		return this.sql;
	}
	
	/**
	 * 获取SQL语句参数
	 * @return
	 */
	public List<String> getParams(){
		return params;
	}
	public void processSql(){
		if(!process){
		        Matcher m = p.matcher(org_sql);
		        while (m.find()) {  
		        	params.add(m.group(1));
		        }
		      	sql=org_sql.replaceAll(pstr, "?");
		      	process=true;
		}
	}
	public static void main(String[] args){
		SQLInfo info =new SQLInfo("select * from t_uer where id=#id# and code=#code# and kjh=#kjh#");
		info.processSql();
		System.out.println(info.getSql());
	}

}
