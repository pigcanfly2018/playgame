package models;

import util.Sp;

public class Config {
	
	public Long config_id;
	
	public String config_name;
	
	public String config_value;
	
	public String config_describe;
	
	public String config_level;
	
	public Integer maxlimit;
	
	public String sortpriority;
	
	public static  Config NTget(Long config_id){
		Config config=(Config) Sp.get().getDefaultJdbc().queryForObject("select * from  mb_config where config_id=? ",
				new Object[]{config_id},new ConfigRowMap());
		return config;
	 }
	
	public static boolean NTexits(String config_name){ 
		String sql="select count(1) from mb_config where config_name=?";
		int count=Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{config_name},Integer.class);
		return count>0;
	}
	
	 public  boolean NTcreat(){
		  String sql="insert into mb_config(config_name,config_value,config_describe,config_level,maxlimit,product,sortpriority)" +
		 		" values(?,?,?,?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{config_name,config_value,config_describe,config_level,maxlimit,"8da",sortpriority});
		  return count>0;
	 }
	 
	 public boolean NTupdate(){
		 String sql="update mb_config set config_value=?,config_describe=?,config_level=?,maxlimit=?,sortpriority=? where config_id=?";
		 int count=Sp.get().getDefaultJdbc().update(sql,
					new Object[]{config_value,config_describe,config_level,maxlimit,sortpriority,config_id});
		 return count>0;
	 }

}
