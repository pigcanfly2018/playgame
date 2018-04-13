package bsz.exch.utils;

import java.util.List;

public class LogHelper {
	
	
	public static String dbLog(String ds,String sql,List<String> params){
		StringBuffer sbparams =new StringBuffer();
		for(String s:params){
			if(s==null){
				sbparams.append("null,");
			}else{
			sbparams.append(s.toString()+",");
			}
		}
		if(params.size()>0){
			sbparams.deleteCharAt(sbparams.length()-1);
		}
		String log="DataSource:"+ds+" Execute SQL:"+sql.replaceAll("\n", "").replaceAll("\t", "")+" Params:["+sbparams+"]";
		return log;
	}
	
	public static String dbLogObj(String ds,String sql,List<Object> params){
		StringBuffer sbparams =new StringBuffer();
		for(Object s:params){
			if(s==null){
				sbparams.append("null,");
			}else{
			sbparams.append(s.toString()+",");
			}
		}
		if(params.size()>0){
			sbparams.deleteCharAt(sbparams.length()-1);
		}
		String log="DataSource:"+ds+" Execute SQL:"+sql.replaceAll("\n", "").replaceAll("\t", "")+" Params:["+sbparams+"]";
		return log;
	}

}
