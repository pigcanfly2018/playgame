package util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.FetchType;


public class JSONResult {
	
	
	
	/**
	 * 转换一些不必要转换的bean
	 */
	public static List<Map> conver(Collection list) throws Exception {

		List<Map> listMap = new ArrayList<Map>();

		for (Object o : list) {

			Map map = new HashMap();

			Field[] fields = o.getClass().getFields();

			for (Field f : fields) {

				Annotation[] ans = f.getAnnotations();

				boolean hasIgnore = false;

				if (ans != null) {
					
					for (Annotation a : ans) {

						if (a.annotationType().equals(IgnoreJson.class)) {
							
							hasIgnore = true;

							break;
						}
					}
				}

				if (!hasIgnore) {

					f.setAccessible(true);
					
					map.put(f.getName(), f.get(o));

				}

			}

			listMap.add(map);

		}

		return listMap;

	}
	
	public static List<Map> conver(List list) throws Exception {

		List<Map> listMap = new ArrayList<Map>();

		for (Object o : list) {

			Map map = new HashMap();

			Field[] fields = o.getClass().getFields();

			for (Field f : fields) {

				Annotation[] ans = f.getAnnotations();

				boolean hasIgnore = false;

				if (ans != null) {
					
					for (Annotation a : ans) {
						
						if (a.annotationType().equals(IgnoreJson.class)) {

							hasIgnore = true;

							break;
						}
					}
				}

				if (!hasIgnore) {

					f.setAccessible(true);
					
					map.put(f.getName(), f.get(o));

				}

			}

			listMap.add(map);

		}

		return listMap;

	}
	
	public static Annotation  isExistAnn(Field field, Class annClass){
		
		Annotation[] ans = field.getAnnotations();

		if (ans != null) {
			
			for (Annotation a : ans) {
				
				if (a.annotationType().equals(annClass)) {
					
					return a;
				}
				
			}
			
			return null;
			
		}else{
			
			return null;
		}
		
		
	}
	
	public static List<Map> conver(List list,boolean lazyClob) throws Exception {

		List<Map> listMap = new ArrayList<Map>();

		for (Object o : list) {

			Map map = new HashMap();

			Field[] fields = o.getClass().getFields();

			for (Field f : fields) {

				Annotation[] ans = f.getAnnotations();

				boolean hasIgnore = false;

				/**
				 * 替换下面注释代码
				 */
				
				if(isExistAnn(f,IgnoreJson.class)!=null){
					
					hasIgnore = true;
				}
				
				Basic b=(Basic)isExistAnn(f,Basic.class);
				
				if(b!=null&&b.fetch().equals(FetchType.LAZY)){
					
					hasIgnore = true;
				}
				
/** 
				if (ans != null) {
					
					for (Annotation a : ans) {
						
						if (a.annotationType().equals(IgnoreJson.class)) {

							hasIgnore = true;

							break;
						}
						if(lazyClob){
						if (a.annotationType().equals(Basic.class)) {

						     	Basic b=(Basic)a;
						     	
						     	if(b.fetch().equals(FetchType.LAZY)){

								hasIgnore = true;

								break;
						     	}
							}
					  }
					}
				}
*/
				if (!hasIgnore) {

					f.setAccessible(true);
					
					if(f.getType().equals(Date.class)||f.getType().equals(java.util.Date.class)
							||f.getType().equals(java.sql.Timestamp.class)){
						
						DateFormat dateFormate=(DateFormat) isExistAnn(f,DateFormat.class);
						
						if(dateFormate==null){
							
							map.put(f.getName(), DateUtil.dateToString((java.util.Date)f.get(o), "yyyy-MM-dd HH:mm:ss"));
							
						}else{
							
							map.put(f.getName(), DateUtil.dateToString((java.util.Date)f.get(o), dateFormate.format()));
						}
						
						
						
					}else{

					  map.put(f.getName(), f.get(o));
					
					}

				}

			}

			listMap.add(map);

		}

		return listMap;

	}
	public static Map converObj(Object obj) throws Exception {



			Map map = new HashMap();

			Field[] fields = obj.getClass().getFields();

			for (Field f : fields) {

				Annotation[] ans = f.getAnnotations();

				boolean hasIgnore = false;
				
				/**
				 * 代替注释下面代码
				 */
                if(isExistAnn(f,IgnoreJson.class)!=null){
					
					hasIgnore = true;
				}
				
/**

				if (ans != null) {
					
					for (Annotation a : ans) {
	
						if (a.annotationType().equals(IgnoreJson.class)) {

							hasIgnore = true;

							break;
						}
					}
				}
 * 
 */
				if (!hasIgnore) {

					f.setAccessible(true);

					// map.put(f.getName(), f.get(obj));
					
					if(f.getType().equals(Date.class)||f.getType().equals(java.util.Date.class)
							||f.getType().equals(java.sql.Timestamp.class)){
						
                       DateFormat dateFormate=(DateFormat) isExistAnn(f,DateFormat.class);
						
						if(dateFormate==null){
							
							map.put(f.getName(), DateUtil.dateToString((java.util.Date)f.get(obj), "yyyy-MM-dd HH:mm:ss"));
							
						}else{
							
							map.put(f.getName(), DateUtil.dateToString((java.util.Date)f.get(obj), dateFormate.format()));
						}
						
						
					}else{

					  map.put(f.getName(), f.get(obj));
					
					}


				}

			}


		return map;

	}
	public static String success(String message) {

		return "{success:true,message:'" + message + "'}";
	}

	public static String failure(String message) {
 
		return "{failure:true,message:'" + message + "'}";
	}
	
	
	public static String adultsuccess(String message,String accountkey) {

		return "{status:1,message:'" + message + ",accountkey:'"+accountkey+"''}";
	}

	public static String adultfailure(String message) {
 
		return "{status:0,message:'" + message + "'}";
	}
	
	
	public static String forbidden(String message) {

		return "{failure:true,forbidden:true,message:'" + message + "'}";
	}
	
	
	
	

}
