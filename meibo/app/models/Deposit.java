package models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
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
  
  public static int NTgetNoDoCnt(Long cust_id){
      String sql ="select count(1) from mb_deposit where cust_id=? and status=1";
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
  
  public static BigDecimal NTgetSumAll(Long cust_id){
	  String sql="SELECT SUM(amount) FROM mb_deposit WHERE STATUS=3 AND cust_id=?";
	  return (BigDecimal) Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{cust_id},new BigDecimalRowMap());
  }
  
  
  public static BigDecimal NTgetSumToday(Long cust_id){
	  String sql="SELECT SUM(amount) FROM mb_deposit WHERE create_date>CURDATE()  AND STATUS=3 AND cust_id=?";
	  return (BigDecimal) Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{cust_id},new BigDecimalRowMap());
  }
  
  public static BigDecimal NTgetSumWuyi(Long cust_id){
	  String sql="SELECT SUM(amount) FROM mb_deposit WHERE create_date > '2017-05-01 00:00:00'  AND STATUS=3 AND cust_id=?";
	  return (BigDecimal) Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{cust_id},new BigDecimalRowMap());
  }
  
  public static BigDecimal NTgetSumLastDay(Long cust_id){
	  String sql="SELECT SUM(amount) FROM mb_deposit WHERE create_date < CURDATE() and create_date > date_sub(curdate(),interval 1 day)  AND STATUS=3 AND cust_id=?";
	  return (BigDecimal) Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{cust_id},new BigDecimalRowMap());
  }
  
  public static BigDecimal NTgetSumLastFourDays(Long cust_id){
	  String sql="SELECT SUM(amount) FROM mb_deposit WHERE create_date < CURDATE() and create_date > date_sub(curdate(),interval 4 day)  AND STATUS=3 AND cust_id=?";
	  return (BigDecimal) Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{cust_id},new BigDecimalRowMap());
  }
  
  public static BigDecimal NTgetSumNewYear(Long cust_id){
	  String sql="SELECT SUM(amount) FROM mb_deposit WHERE create_date > '2018-01-01 00:00:00' and create_date < '2018-01-12 23:59:59'  AND STATUS=3 AND cust_id=?";
	  return (BigDecimal) Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{cust_id},new BigDecimalRowMap());
  }
  
  //历史盈亏
  public static BigDecimal NTgetStatCust(Long cust_id){
	  String sql="SELECT (SELECT IFNULL(SUM(amount),0) a FROM mb_deposit WHERE STATUS=3 AND cust_id=? )-(SELECT IFNULL(SUM(amount),0) b FROM mb_withdraw WHERE STATUS=5 AND cust_id= ?) c";
	  return (BigDecimal) Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{cust_id,cust_id},new BigDecimalRowMap());
  }
  //统计满周奖品，存款额度
  public static List<FullWeek> getFullWeek(String startdate,String enddate){
	  //统计
	 String sql = "SELECT weekamount,login_name FROM ( SELECT SUM(b.amount) weekamount,b.login_name FROM mb_deposit b , mb_customer c  WHERE b.login_name=c.login_name AND  b.create_date > '"+startdate+"' AND b.create_date <'"+enddate+"' AND c.create_date > '"+startdate+"' AND c.create_date <'"+enddate+"' AND b.STATUS=3 "
     +"GROUP BY b.login_name  ORDER BY weekamount DESC ) AS weeks WHERE weekamount>=100";
	  List<FullWeek> lis =  Sp.get().getDefaultJdbc().query(sql,new FullWeekRowMap());
	  if(lis.size()>0){
		  
		  return lis;
	  }
	  return null;
	
  }

/*

	public static List<FullWeek> getFirstDeposit(List<FullWeek> lis2) {
		// TODO Auto-generated method stub
		List<FullWeek> result=null;
		for(FullWeek fw:lis2){
			System.out.println(fw.login_name);
			 String sql = " SELECT amount as weekamount,login_name FROM mb_deposit WHERE login_name='"+fw.login_name+"' limit 1";
			 List<FullWeek> first =   Sp.get().getDefaultJdbc().query(sql,new FullWeekRowMap());
				 fw.weekamount=fw.weekamount-first.get(0).weekamount;
			result.add(fw);
		}
		return result;
	}
  
*/  
  
}
