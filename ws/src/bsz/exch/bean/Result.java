package bsz.exch.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import bsz.exch.utils.DateUtil;

public class Result implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
	private String taskId;
	private String funId;
	private String time; 
	private String flag ="-1";
	private String timeout ="-1";
	private String msg="";
	private int start=0;
	private int limit=0;
	private int length=0;
	private int total=0;
	private List<String>fields =null;
	private List<String[]>data =null;
	private boolean isList=false;
    //单个结果
	private String res="";
	private Result(){}
	
	public static Result create(String taskId,String funId){
		Result result = new Result();
		result.taskId=taskId;
		result.funId=funId;
		result.time=DateUtil.dateToString(new Date(System.currentTimeMillis()),"yyyy-MM-dd HH:mm:ss");
		return result;
	}
	
	public static Result from(String xml){
		try{
			Builder builder = new Builder();
			Document doc = builder.build(xml,null);
			Result result  = new Result();
			Element e =doc.getRootElement();
			result.taskId=e.getAttribute("taskId").getValue();
			result.funId=e.getFirstChildElement("funId").getValue();
			result.time=e.getFirstChildElement("time").getValue();
			Element message =e.getFirstChildElement("message");
			result.flag=message.getAttributeValue("flag");
			result.isList=Boolean.valueOf(message.getAttributeValue("isList"));
			result.timeout=message.getAttributeValue("timeout");
			result.msg=message.getValue();
			Element page =e.getFirstChildElement("page");
			result.start=Integer.valueOf(page.getAttributeValue("start"));
			result.limit=Integer.valueOf(page.getAttributeValue("limit"));
			result.total=Integer.valueOf(page.getAttributeValue("total"));
			result.length=Integer.valueOf(page.getAttributeValue("length"));
			String fields =e.getFirstChildElement("fields").getValue();
			String[] fs=StringUtils.splitPreserveAllTokens(fields,"|");;
			result.addFields(fs);
			Element data =e.getFirstChildElement("data");
			Elements records =data.getChildElements();
			for(int j=0;j<records.size();j++){
				Element r = records.get(j);
				String c=r.getValue();
				String[] re=StringUtils.splitPreserveAllTokens(c,"<|>");
				result.addRecord(re);
			}
			result.res=e.getFirstChildElement("res").getValue();
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return null;
	}
	//设置相应域
	public void addField(String f){
		if(fields==null){
			fields=new ArrayList<String>();
		}
		fields.add(f);
	}
	
	public void addFields(String[] fs){
		if(fields==null){
			fields=new ArrayList<String>();
		}
		for(String f:fs){
		    fields.add(f);
		}
	}
	
	public void addFields(List<String> fs){
		if(fields==null){
			fields=new ArrayList<String>();
		}
		for(String f:fs){
		    fields.add(f);
		}
	}
	//添加一行结果
	public void addRecord(String [] record){
		if(data==null){
			isList=true;
			data =new ArrayList<String[]>();
		}
		data.add(record);
	}
	//添加一行结果
	public void addRecord(List<String> record){
		if(data==null){
			isList=true;
			data =new ArrayList<String[]>();
		}
		data.add(record.toArray(new String[0]));
	}
	
	/**
	 * 创建异常
	 * @param taskId
	 * @param funId
	 * @param flag
	 * @param msg
	 * @return
	 */
	public static Result createError(String taskId,String funId,String flag,String msg){
		Result result =new Result();
		result.taskId=taskId;
		result.funId=funId;
		result.flag=flag;
		result.msg=msg;
		result.time=DateUtil.dateToString(new Date(System.currentTimeMillis()),"yyyy-MM-dd HH:mm:ss");
		
	    return result;
	}
	
	
	public static Result createSuccess(String taskId,String funId,String res){
		Result result =new Result();
		result.taskId=taskId;
		result.funId=funId;
		result.flag="-1";
		result.res=res;
		result.isList=false;
		result.time=DateUtil.dateToString(new Date(System.currentTimeMillis()),"yyyy-MM-dd HH:mm:ss");
		
	    return result;
	}
	
	
	/**
	<result taskId="b1da2598-51c8-4a57f-ae47-fb1056334344">
		<funId>t</funId>
		<time>2015-07-15 12:11:10</time>
		<message flag="1" timeout="100"><![CDATA[]]></message>
		<page start="0" limit="20" length="100" total="1000"></page>
		<fields><![CDATA[userName<|>tshs]]></fields>
		<data>
		   <record><![CDATA[aa<|>bb<|>cc<|>ee]]></record>
		   <record><![CDATA[aa<|>bb<|>cc<|>ee]]></record>
		   <record><![CDATA[aa<|>bb<|>cc<|>ee]]></record>
		   <record><![CDATA[aa<|>bb<|>cc<|>ee]]></record>
		   <record><![CDATA[aa<|>bb<|>cc<|>ee]]></record>
		</data>
	</result>
 * 
 */
	public String toXml(){
		
		StringBuffer sb =new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<result taskId=\""+taskId+"\">");
		sb.append("<funId>");
		sb.append(funId);
		sb.append("</funId>");
		sb.append("<time>");
		sb.append(time);
		sb.append("</time>");
		sb.append("<message"+" flag=\""+flag+"\""+" timeout=\""+timeout+"\" isList=\""+isList+"\">");
		sb.append("<![CDATA["+msg+"]]>");
		sb.append("</message>");
		sb.append("<page"+" start=\""+start+"\" limit=\""+limit+"\" length=\""+length+"\" total=\""+total+"\"></page>"); 
	
		sb.append("<fields><![CDATA[");
		if(fields!=null&&fields.size()>0){
			for(String k:fields){
				sb.append(k+"|");
			}
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append("]]></fields>");
		sb.append("<data>");
		if(data!=null&&data.size()>0){
			for(String[] ss :data){
				sb.append("<record><![CDATA[");
				for(String s:ss){
					sb.append(s+"<|>");
				}
				sb.deleteCharAt(sb.length()-1);
				sb.deleteCharAt(sb.length()-1);
				sb.deleteCharAt(sb.length()-1);
				sb.append("]]></record>");
			}
		}
		sb.append("</data>");
		sb.append("<res><![CDATA[");
		sb.append(res);
		sb.append("]]></res>");
		sb.append("</result>");
		return sb.toString();
	}
	
	
	public void setFlag(String flag){
		this.flag=flag;
	}
	public void setRes(String res){
		this.res=res;
	}
	public void setIsList(boolean isList){
		this.isList=isList;
	}
	
	public String getFlag(){
		return this.flag;
	}
	public String getRes(){
		return this.res;
	}
	public boolean isList(){
		return this.isList;
	}
	
	/**
	 * 把单个结果转化成boolean类型
	 */
	public boolean getBooleanOfRes(){
		try{
			return Boolean.valueOf(res);
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public int getIntOfRes(){
		try{
			return Integer.valueOf(res);
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	public BigDecimal getBigDecimalOfRes(){
		try{
			return new BigDecimal(res);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public Long getLongOfRes(){
		try{
			return Long.valueOf(res);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public void setPage(int start,int limit,int total){
		 this.start=start;
		 this.limit=limit;
		 this.total=total;
	}
	
	public void setLength(int length){
		this.length=length;
	}
	
	/**
	 * 判断是否执行成功
	 * @return
	 */
	public boolean success(){
		return "-1".equals(this.flag);
	}
	
	public static void main(String[] args){
//		Result result = Result.createNetworkError("sdsdsd", "sddddddddd");
//		result.addFields(new String[]{"aa","bb"});
//		result.addRecord(new String[]{"uuu","wwww"});
//		System.out.println(Result.from(result.toXml()).toXml());
	}
	
	

}
