package bsz.exch.core;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bsz.exch.utils.ScanUtil;

public class HandlerFactory {

	private static final Logger logger = LoggerFactory.getLogger(HandlerFactory.class);
	private static Map<String, HandlerInfo> maps = new HashMap<String, HandlerInfo>();
	
	
	private static void loadClass(String packages){
		try{
			ScanUtil scan = new ScanUtil(packages);
	    	List<String> list=scan.getFullyQualifiedClassNameList(); 
	    	for(String s:list){
	    		try {
	    			if(s.endsWith("Handler")){
	    				Class c=Class.forName(s);
	    				Handler ann=(Handler)c.getAnnotation(Handler.class);
	    				if(ann!=null){
	    					String type=ann.name();
	    					Object o=Class.forName(s).newInstance();
	    					HandlerInfo info =new HandlerInfo(type,o);
	    					Method[] methods= c.getDeclaredMethods();
	    					for(Method m :methods){
	    						Service service=(Service)m.getAnnotation(Service.class);
	    						if(service!=null){
	    							info.addService(service.name(), m);
	    							logger.info(type + " - " + service.name());
	    						}
	    						//前置方法
	    						Before before =(Before)m.getAnnotation(Before.class);
	    						if(before!=null){
	    							info.setBefore(m);
	    						}
	    						//后置方法
	    						After after =(After)m.getAnnotation(After.class);
	    						if(after!=null){
	    							info.setAfter(m);
	    						}
	    						
	    					}
	    					if(info.getServiceSize()==0){
	    						 logger.error(s+" Must Realize The Default Method 'Process'!");
	    					}
	    					if(!maps.containsKey(type)){
	    						maps.put(type, info);
	     					   logger.info(type + "=" + s);
	     					}
	    				}
	    			}
				} catch (Exception ex) {
					logger.info("装载" + s + "失败，" + ex.getMessage());
				}
	    	}
			}catch(Exception e){
				logger.info("查找 "+packages+" 失败" + e.getMessage());
			}
		
	}

	
	public static void loadClass0() {
		loadClass("bsz.exch.handlers");
		loadClass("com.product.bda.handler");
		loadClass("com.product.bojin.handler");
	}
	
	public static HandlerInfo factory(String name){
		if(maps.containsKey(name)){
			return maps.get(name);
		}
		return null;
	}

	static class JarFileFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			if (name.endsWith(".jar")||name.endsWith(".class")) {
				return true;
			}
			return false;
		}
	}
}