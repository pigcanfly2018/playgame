package bsz.exch.core;

import java.util.HashMap;
import java.util.Map;

/**
 * sql 缓存 cache
 * @author robin
 *
 */
public class SQLProCache {

	private SQLProCache(){
		
	}
	private static SQLProCache  cache = new SQLProCache();
	
	
	public static SQLProCache instance(){
		   if(cache==null){
			   cache = new SQLProCache();
		   }
		   return cache;
	}
	
	private  Map<String,SQLInfo> sqlMap =new HashMap<String,SQLInfo>();
	
	
	public SQLInfo addSqlInfo(InterFace inter){
		if(!sqlMap.containsKey(inter.getId())){
			SQLInfo info=new SQLInfo(inter.getStatement().trim());
			info.processSql();
			sqlMap.put(inter.getId(), info);
		}
		return sqlMap.get(inter.getId());
	}
	
	
	
	
	
	
}
