package models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class Hongbao {
	
	public Long mb_hongbao_id;
	public Long cust_id;
	public Long gift_id;
	public String login_name;
	public Date create_date;
	public BigDecimal credit;
	public Integer status;
	public String content;
	public String level;
	public String gift_no;
	
	
	  public static List<Hongbao> NTgetToday(Long cust_id){
		  String sql="SELECT * FROM mb_hongbao WHERE create_date>=CURDATE() and cust_id=?";
		  List<Hongbao> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{cust_id},new HongbaoRowMap());
		  return list;
	  }
	  
	  public static List<Hongbao> NTgetLeft(Long cust_id){
		  String sql="SELECT * FROM mb_hongbao WHERE create_date>='2018-03-01 10:00:00' and "
		  		+ "create_date<'2018-03-15 18:00:00' and cust_id=? and status = 0 ";
		  List<Hongbao> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{cust_id},new HongbaoRowMap());
		  return list;
	  }
	  
	  public static int NTgetCountToday(Long cust_id){
		  String sql="SELECT COUNT(1) FROM mb_hongbao WHERE create_date>CURDATE() AND cust_id=?";
		  return Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{cust_id},Integer.class);
	  }
	  
	  public static List<Hongbao> NTgetAll(Long cust_id){
		  String sql="SELECT * FROM mb_hongbao WHERE  cust_id=?";
		  List<Hongbao> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{cust_id},new HongbaoRowMap());
		  return list;
	  }
	  
	  public static Hongbao NTgetSpecialOne(Long cust_id,String level){
		  String sql="SELECT * FROM mb_hongbao WHERE  cust_id=? and level=?";
		  List<Hongbao> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{cust_id,level},new HongbaoRowMap());
		  if(list.size()>0){
			  return list.get(0);
		  }
		  return null;
	  }
	  
	  public static long createHongbao(Hongbao hongbao){
		  String sql="insert into  mb_hongbao (cust_id,gift_id,login_name,create_date,credit,status,content,level) values(?,?,?,now(),?,0,?,?)";
		  Object[] objs=new Object[]{hongbao.cust_id,hongbao.gift_id,hongbao.login_name,hongbao.credit,hongbao.content,hongbao.level};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objs), keyHolder);
		  return keyHolder.getKey().longValue();
	 }
	  
	  public static boolean createHongbao(Long cust_id,String login_name,BigDecimal credit,String order_no){
			
			 String sql="insert into  mb_hongbao (cust_id,login_name,create_date,credit,status,content) values(?,?,now(),?,0,?)";
			 Object [] objs =new Object[]{cust_id,login_name,credit,order_no};
		     int i=Sp.get().getDefaultJdbc().update(sql, objs);  
		     return i>0;
			 
		}
	  
	 public static boolean finishHongbao(Long gift_id,Long mb_hongbao_id){
		  String sql="update mb_hongbao set gift_id=?,status=1 where mb_hongbao_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{gift_id,mb_hongbao_id});
		  return count>0;
	 }
	 
	 public static boolean finishHongbao(String gift_no,Long mb_hongbao_id){
		  String sql="update mb_hongbao set gift_no=?,status=1 where mb_hongbao_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{gift_no,mb_hongbao_id});
		  return count>0;
	 }
	  
}
