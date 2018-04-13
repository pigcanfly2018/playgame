package bsz.exch.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import bsz.exch.core.WsCxtHelper;

/**
 * 权限缓存
 * @author robin
 */
public class RoleCache {
	
	private static RoleCache cache =new RoleCache();
	
	private RoleCache(){
		
	}
	
	public static  RoleCache instance(){
		return cache;
	}
	
	private Map<String,String> map =new HashMap<String,String>();
	
	public String getRole(String userName,String password){
		if(map.containsKey(userName+password)){
			return map.get(userName+password);
		}
		JdbcTemplate jdbcTemplate=(JdbcTemplate)WsCxtHelper.getBean("wsjdbc");
		String sql="select role from ws_user where user_name=?  and user_pwd=? ";
		List<Map<String,Object>>list=(List<Map<String,Object>>)jdbcTemplate.queryForList(sql, new Object[]{userName,password});
		if(list.size()==0)return null;
		String role=list.get(0).get("role").toString();
		System.out.println("加载用户:"+userName+" 权限:"+role);
		map.put(userName+password, role);
		return role;
	}
	
	
	
	
	
}
