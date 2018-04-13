package models;

import java.util.List;

import util.Sp;

public class Config {
	
	public Long config_id;
	
	public String config_name;
	
	public String config_value;
	
	public String config_describe;
	
	public String config_level;
	
	public String sortpriority;
	
	public Integer maxlimit;
	
	public Integer para;
	
	public static  Config NTget(Long config_id){
		Config config=(Config) Sp.get().getDefaultJdbc().queryForObject("select * from  mb_config where config_id=? ",
				new Object[]{config_id},new ConfigRowMap());
		return config;
	 }
	
	public static List<Config> getOpenWeiXin(){
		String sql="SELECT * FROM mb_config WHERE  config_value='开' and config_name like '%wexin%' ";
		
		List<Config> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{},new ConfigRowMap());
		return list;
	}
	
	public static List<Config> getOpenZhifubao(){
		String sql="SELECT * FROM mb_config WHERE  config_value='开' and config_name like '%ali%' ";
		
		List<Config> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{},new ConfigRowMap());
		return list;
	}
	
	public static List<Config> getOpenQQ(){
		String sql="SELECT * FROM mb_config WHERE  config_value='开' and config_name like '%qq%' ";
		
		List<Config> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{},new ConfigRowMap());
		return list;
	}
	
	public static List<Config> getOpenYinLian(){
		String sql="SELECT * FROM mb_config WHERE  config_value='开' and config_name like '%yinlian%' ";
		
		List<Config> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{},new ConfigRowMap());
		return list;
	}

	public static List<Config> getOpenJd() {
		// TODO Auto-generated method stub
		String sql="SELECT * FROM mb_config WHERE  config_value='开' and ( config_name like '%jd%' or config_name like '%jingdong%' ) ";
		
		List<Config> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{},new ConfigRowMap());
		return list;	
	}
	
	public static Config getConfig(String config_name){
		  String sql="select * from mb_config where config_value='开' and config_name =?";
		  List<Config> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{config_name},new ConfigRowMap());
		  if(list.size()>0){
			  return list.get(0);
		  }
		  return null;
	 }
	
	public static Config getConfig(String sortpriority,String config_level){
		  String sql="select * from mb_config where config_value='开' and sortpriority =? and config_level like ? ";
		  List<Config> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{sortpriority,config_level},new ConfigRowMap());
		  if(list.size()>0){
			  return list.get(0);
		  }
		  return null;
	 }
	
	
	public static boolean NTexits(String config_name){ 
		String sql="select count(1) from mb_config where config_name=?";
		int count=Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{config_name},Integer.class);
		return count>0;
	}
	
	 public  boolean NTcreat(){
		  String sql="insert into mb_config(config_name,config_value,config_describe,config_level,maxlimit,product)" +
		 		" values(?,?,?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{config_name,config_value,config_describe,config_level,maxlimit,"8da"});
		  return count>0;
	 }
	 
	 public boolean NTupdate(){
		 String sql="update mb_config set config_value=?,config_describe=?,config_level=?,maxlimit=? where config_id=?";
		 int count=Sp.get().getDefaultJdbc().update(sql,
					new Object[]{config_value,config_describe,config_level,maxlimit,config_id});
		 return count>0;
	 }

}
