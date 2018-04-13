package models;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class Deposit {
	public Long deposit_id;
    public String dep_no;
    public Long cust_id;
    public String login_name;
    public String real_name;
    public BigDecimal amount;
    public BigDecimal poundage;
    public Integer pdage_status;
    public Integer status;
    public String bank_name;
    public String account_name;
    public String deposit_type;
    public String location;
    public Date deposit_date;
    public String remarks;
    public Long pic_id;
    public Date create_date;
    public String create_user;
    public String ip;
    public Date audit_date;
    public String sb_game;
    public Boolean is_sb;
    public String order_no;
    public Boolean locked;
    public Date locked_date;
 
  public  Long NTcreat(){
	  String sql="insert into  mb_deposit (dep_no,cust_id,login_name,real_name,"
	  		+ "amount,poundage,pdage_status,status,bank_name,account_name,"
	  		+ "deposit_type,location,deposit_date,remarks,pic_id,create_date,"
	  		+ "create_user,ip,audit_date,sb_game,is_sb,order_no,locked,locked_date) "
	  		+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?,?,?,?)";
	  Object[] objects=new Object[]{dep_no,cust_id,login_name,
			  real_name,amount,poundage,pdage_status,status,bank_name,account_name,
			  deposit_type,location,deposit_date,remarks,pic_id,create_user,
			  ip,audit_date,sb_game,is_sb,order_no,locked,locked_date};
	  KeyHolder keyHolder = new GeneratedKeyHolder();
	  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
	  return keyHolder.getKey().longValue();
 }
  

  
  public static Deposit NTget(Long deposit_id){
	      String sql ="select * from mb_deposit where deposit_id=?";
	      Deposit dep=(Deposit) Sp.get().getDefaultJdbc().queryForObject(sql,
					new Object[]{deposit_id},new DepositRowMap());
	      return dep;
  }
  
  public static int NTgetCount(Long cust_id){
      String sql ="select count(1) from mb_deposit where cust_id=? and status=3";
      return Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{cust_id},Integer.class);
}
  
  
  public static boolean NTchangeStatus(Long deposit_id,Integer status,Integer pdage_status){
	  String sql ="update mb_deposit set status=?,pdage_status=? where deposit_id=?";
	  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{status,pdage_status,deposit_id});
	  return count>0;
  }
  
  public static boolean NTrecAuditDate(Long deposit_id){
	  String sql ="update mb_deposit set audit_date=now() where deposit_id=?";
	  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{deposit_id});
	  return count>0;
  }
  
  public static int NTgetCountToday(Long cust_id){
	  String sql="SELECT COUNT(1) FROM mb_deposit WHERE create_date>CURDATE() AND STATUS=3 AND cust_id=?";
	  return Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{cust_id},Integer.class);
  }
  public static int NTgetCountMoreThan500Today(Long cust_id){
	  String sql="SELECT COUNT(1) FROM mb_deposit WHERE create_date>CURDATE() AND STATUS=3 AND cust_id=? and amount>=500";
	  return Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{cust_id},Integer.class);
  }
  
}
