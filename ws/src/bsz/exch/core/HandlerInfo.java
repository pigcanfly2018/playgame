package bsz.exch.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HandlerInfo {
	
	private String name;
	
	private Map<String,Method> services =new HashMap<String,Method>();
	
	private Method before;
	
	private Method after;
	
	private Object handler;
	
	private HandlerInfo(){}
	
	public HandlerInfo(String name,Object handler){
		this.name=name;
		this.handler=handler;
   }
   public void addService(String service,Method method){
	   if(!services.containsKey(service)){
		   services.put(service, method);
	   }
   }
   
   public int getServiceSize(){
	   return services.size();
   }
   public Method getService(String service) {
	   return services.get(service);
   }
   
   public Object getHandler(){
	   return handler;
   }
   
   public void setBefore(Method method){
	   this.before=method;
   }
   
   public Method getBefore(){
	   return this.before;
   }
   
   public void setAfter(Method method){
	   this.after=method;
   }
   
   public Method getAfter(){
	   return this.after;
   }
   
   
   
}
