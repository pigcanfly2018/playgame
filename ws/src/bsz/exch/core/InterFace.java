package bsz.exch.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能
 * @author robin
 *
 */
public class InterFace {
	
	/**
	 * 功能ID
	 */
	private String id ;
	
	/**
	 * 功能名
	 */
	private String name;
	
	
	/**
	 * 功能解释
	 */
	private String explain;
	
	
	/**
	 * 参数
	 */
	private List<String>  reqEn = new ArrayList<String>();
	
	private List<String>  reqCn = new ArrayList<String>();
	/**
	 *响应
	 */
	private List<String>  replyEn = new ArrayList<String>();
	private List<String>  replyCn = new ArrayList<String>();
	
	/**
	 * 语句
	 */
	private String statement;
	
	/**
	 * 功能类型  
	 */
	private String type;
	
	/**
	 * 功能位置
	 */
	private String location;
	
	/**
	 * 数据源
	 */
	private String dataSource;
	
	/**
	 * 是否超时
	 */
	private String timeout;
	
	/**
	 * 是否可用
	 */
	private String disabled ="false";
	
	
	private String service;
	
	/**
	 * 角色
	 */
	private String roles;
	
	/**
	 * 产品
	 */
	public String product;
	
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	/**
	 * 获取接口类型
	 * @return
	 */
	public String getType(){
		return type;
	}
	
	/**
	 * 获取接口ID
	 * @return
	 */
	public String getId(){
		return id;
	}
	
	
	public void addReqEn(String s){
		reqEn.add(s);
	}
	public void addReqCn(String s){
		reqCn.add(s);
	}
	public void addReplyEn(String s){
		replyEn.add(s);
	}
	public void addReplyCn(String s){
		replyCn.add(s);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public List<String> getReqEn() {
		return reqEn;
	}

	public void setReqEn(List<String> reqEn) {
		this.reqEn = reqEn;
	}

	public List<String> getReqCn() {
		return reqCn;
	}

	public void setReqCn(List<String> reqCn) {
		this.reqCn = reqCn;
	}

	public List<String> getReplyEn() {
		return replyEn;
	}

	public void setReplyEn(List<String> replyEn) {
		this.replyEn = replyEn;
	}

	public List<String> getReplyCn() {
		return replyCn;
	}

	public void setReplyCn(List<String> replyCn) {
		this.replyCn = replyCn;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

   public boolean haveRole(String role){
	   if(roles==null||"".equals(roles)) return true;
	   String[] r=roles.split(",");
	   for(String s:r){
		   if(s.trim().equals(role))return true;
	   }
	   return false;
   }

public String getProduct() {
	return product;
}

public void setProduct(String product) {
	this.product = product;
}

}
