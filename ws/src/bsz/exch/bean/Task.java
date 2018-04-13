package bsz.exch.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import bsz.exch.utils.DateUtil;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;




public class Task {
	/**
	 * 请求包的UUID
	 */
	private String id;
	
	/**
	 * 请求包创建时间
	 */
	private String time;
	
	/**
	 * 请求功能Id
	 */
	private String funId;
	
	/**
	 * 参数key
	 */
	private List<String> keys =new ArrayList<String>();
	
	/**
	 * 参数
	 */
	private Hashtable<String,String> params=new Hashtable<String,String>();
	
	/**
	 * 开始记录数
	 */
	private int start = 0 ;
	
	/**
	 * 数据大小
	 */
	private int limit = 20;
	
	/**
	 * 用户名
	 */
	private String wsuser = "8da_mobile";
	
	/**
	 * 密码
	 */
	private String pwd = "753951";
	
	/**
	 * 是否需要计算总数
	 */
	private boolean count=true;
	
	/**
	 * 是否需要分页
	 */
	private boolean activePage=true;
	
	
	/**
	 * 创建时间
	 * @return
	 */
	public static Task create(String funId){
		Task task  = new Task();
		task.id = UUID.randomUUID().toString();
		task.funId=funId;
		task.time=DateUtil.dateToString(new Date(System.currentTimeMillis()),"yyyy-MM-dd HH:mm:ss");
		return task;
	}
	

    
	public static Task from(String xml){
		try{
			Builder builder = new Builder();
			Document doc = builder.build(xml,null);
			
			Task task  = new Task();
			Element e =doc.getRootElement();
			System.out.println(e.toXML());
			
			task.id=e.getAttribute("id").getValue();
			task.wsuser=e.getAttribute("wsuser").getValue();
			task.pwd=e.getAttribute("pwd").getValue();
			task.funId=e.getFirstChildElement("funId").getValue();
			task.time=e.getFirstChildElement("time").getValue();
			Element page=e.getFirstChildElement("page");
			task.start=Integer.valueOf(page.getAttribute("start").getValue());
			task.limit=Integer.valueOf(page.getAttribute("limit").getValue());
			task.count=Boolean.valueOf(page.getAttribute("count").getValue());
			task.activePage=Boolean.valueOf(page.getAttribute("activePage").getValue());
			Element  param=e.getFirstChildElement("params");
			Elements elements =param.getChildElements();
			for(int i=0;i<elements.size();i++){
				Element e0=elements.get(i);
				String name=e0.getAttribute("name").getValue();
				task.keys.add(name);
				task.params.put(name, e0.getValue());
			}
			return task;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	/**
	 * 增加一个参数
	 * @param param
	 * @param value
	 */
	public void addParam(String key,Object value){
		keys.add(key);
		params.put(key,value.toString());
	}
	
	public String getParam(String key){
		return params.get(key);
	}
	
	/**
	 * 设置一个参数
	 * @param key
	 * @param value
	 */
	public void setParam(String key,Object value){
		removeParam(key);
		addParam(key,value);
	}
	
	/**
	 * 判断参数是否为null或者blank
	 * @param key
	 * @return
	 */
	public boolean ifNullOrBlankParam(String key){
		if(getParam(key)==null||"".equals(getParam(key))){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断参数是否为NULL
	 * @param key
	 * @return
	 */
	public boolean ifNullParam(String key){
		if(getParam(key)==null){
			return true;
		}
		return false;
	}
	/**
	 * 删除一个参数
	 * @param param
	 */
	public void removeParam(String key){
		 for (Iterator<String> it = keys.iterator(); it.hasNext();) {  
	            String p = it.next();  
	            if(p.equals(key)){
	            	it.remove();
	            	break;
	            }  
	        }  
		params.remove(key);
	}
	
	/**
	 * 获取任务ID
	 * @return
	 */
	public String getId(){
		return this.id;
	}
	
	
	public boolean isCount(){
		return count;
	}
	
	public boolean isActivePage(){
		return activePage;
	}
	
	/**
	 * 设置是否分页
	 * @param activePage
	 * @return
	 */
	public Task setActivePage(boolean activePage){
		this.activePage=activePage;
		return this;
	}
	
	/**
	 * 设置是否统计总数
	 * @param count
	 * @return
	 */
	public Task setCount(boolean count){
		this.count=count;
		return this;
	}
	/**
	 * 设置page信息
	 * @param start
	 * @param limit
	 */
    public void setPage(int start,int limit){
    	this.start=start;
    	this.limit=limit;
    }
	
    
    public String getFunId(){
    	return funId;
    }
    
    
	
	
	
	
	public int getStart() {
		return start;
	}


	public int getLimit() {
		return limit;
	}


	
	public String getUser(){
		return this.wsuser;
	}
	
	public String getPwd(){
		return this.pwd;
	}

	public String toXml(){
		StringBuffer sb =new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<task id=\""+id+"\" wsuser=\""+wsuser+"\" pwd=\""+pwd+"\">");
		sb.append("<funId>");
		sb.append(funId);
		sb.append("</funId>");
		sb.append("<time>");
		sb.append(time);
		sb.append("</time>");
		sb.append("<params>");
		if(keys.size()>0){
			for(String k:keys){
				sb.append("<param name=\""+k+"\">");
				sb.append("<![CDATA[");
				sb.append(params.get(k));
				sb.append("]]>");
				sb.append("</param>");
			}
		}
		sb.append("</params>");
		
		sb.append("<page start=\""+start+"\" limit=\""+limit+"\" count=\""+count+"\" activePage=\""+activePage+"\" />");
		sb.append("</task>");
		return sb.toString();
	}
	
	
	
	public static void main(String [] args){
		Task task =Task.create("t");
		task.addParam("userName", "zhangsan");
		task.addParam("tshs", "ddd");
		System.out.println(task.toXml());
		
	}
	
	
	
	
}
