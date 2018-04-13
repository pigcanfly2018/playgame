package models;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class Bank {
	public Long bank_id;
    public String bank_name;
    public String account;
    public String account_name;
    public String bank_dot;
    public Integer cust_level;
    public String remarks;
    public String create_user;
    public String specifiedname;
    public Date create_date;
    public Boolean available;
    
	
    public  Long NTcreat(){
		  final String sql="insert into  mb_bank (bank_name,account,account_name,bank_dot,cust_level,remarks,create_user,specifiedname,create_date,available) values(?,?,?,?,?,?,?,?,?,?)";
		  final Object[] objects=new Object[]{bank_name,account,account_name,bank_dot,cust_level,remarks,create_user,specifiedname,create_date,available};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		  return keyHolder.getKey().longValue();
	 }
    
    public  boolean NTupdate(){
		  String sql="update mb_bank set bank_name=?,account=?,account_name=?,bank_dot=?,cust_level=?,remarks=?,specifiedname=?,available=? where  bank_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{bank_name,account,account_name,bank_dot,cust_level,remarks,StringUtils.trimToNull(specifiedname),available,bank_id});
		  return count>0;
	 }
    
    public static boolean deleteBank(Long id){
    	 String sql="delete from mb_bank where bank_id=?";
     	 int  count= Sp.get().getDefaultJdbc().update(sql,new Object[]{id});
     	 if(count>0)return true;return false;
    }


}
