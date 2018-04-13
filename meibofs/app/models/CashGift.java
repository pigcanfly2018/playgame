package models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;
public class CashGift {
    public Long gift_id;
    public Date kh_date;
    public String login_name;
    public String real_name;
    public Integer cust_level;
    public String gift_type;
    public String gift_code;
    public BigDecimal deposit_credit;
    public BigDecimal net_credit;
    public BigDecimal valid_credit;
    public Float rate;
    public BigDecimal payout;
    public Date cs_date;
    public String remarks;
    public Date create_date;
    public String create_user;
    public Integer status;
    public Date audit_date;
    public String audit_user;
    public String audit_msg;
    public Long cust_id;
    public String gift_no;
    public Integer transferflag;
  public  Long NTcreat(){
		  final String sql="insert into  mb_cash_gift (gift_no,kh_date,login_name,real_name,cust_level,gift_type,gift_code,deposit_credit,net_credit,valid_credit,rate,payout,cs_date,remarks,create_date,create_user,status,audit_date,audit_user,audit_msg,cust_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?,?)";
		  final Object[] objects=new Object[]{gift_no,kh_date,login_name,real_name,cust_level,gift_type,gift_code,deposit_credit,net_credit,valid_credit,rate,payout,cs_date,remarks,create_user,status,audit_date,audit_user,audit_msg,cust_id};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		  return keyHolder.getKey().longValue();
   }
  
  public static CashGift NTgetGift(Long gift_id){
		 String sql="select * from mb_cash_gift where gift_id=?";
    	 List<CashGift> giftList= Sp.get().getDefaultJdbc().query(sql, new Object[]{gift_id}, new CashGiftRowMap());
    	 if(giftList.size()>0){
    		 return giftList.get(0);
    	 }
    	 return null;
  }
  
  public static List<CashGift> NTgetGifts(String gift_id){
		 String sql="select * from mb_cash_gift where gift_id in ("+gift_id+")";
 	 List<CashGift> giftList= Sp.get().getDefaultJdbc().query(sql,  new CashGiftRowMap());
 	 int gifts = gift_id.split(",").length;
 	 if(giftList.size()==gifts){
 		 return giftList;
 	 }
 	 return null;
}
  /**
   * 未审核的
   * @param code
   * @return
   */
  public static List<CashGift> NTgetGiftsBycode(String code){
		 String sql="select * from mb_cash_gift where gift_code=? and status =1";
 	 List<CashGift> giftList= Sp.get().getDefaultJdbc().query(sql, new Object[]{code}, new CashGiftRowMap());
 	 
 	 return giftList;
}
  
  public static boolean NTdeleteGift(Long id){
 	 String sql="delete from mb_cash_gift where gift_id=?";
 	 int  count= Sp.get().getDefaultJdbc().update(sql,new Object[]{id});
 	 if(count>0)return true;return false;
  }
  
  public static boolean NTdeleteSelfGift(String gift_no){
	 	 String sql="delete from mb_self_app where gift_no=?";
	 	 int  count= Sp.get().getDefaultJdbc().update(sql,new Object[]{gift_no});
	 	 if(count>0)return true;return false;
	  }
  
  public static boolean NTcompleteGift(Long id,Integer transferflag,String remarks){
	 	 String sql="update mb_cash_gift set transferflag=?,remarks=? where gift_id=?";
	 	 int  count= Sp.get().getDefaultJdbc().update(sql,new Object[]{transferflag,remarks,id});
	 	 if(count>0)return true;return false;
  }
  
  public static int NTdeleteGiftBycode(String code){
 	 String sql="delete from mb_cash_gift where gift_code=? and status =1";
 	 int  count= Sp.get().getDefaultJdbc().update(sql,new Object[]{code});
 	 return count;
  }
  
  public static int NTAuditGiftBycode(Integer flag,String audit_user,String audit_msg,String gift_code){
	 	 String sql="update mb_cash_gift set audit_date=now(),status=?,audit_user=?,audit_msg=?  where gift_code=? and status =1";
	 	 int  count= Sp.get().getDefaultJdbc().update(sql,new Object[]{flag,audit_user,audit_msg,gift_code});
	 	 return count;
  }
  
  public static boolean NTAuditGift(Long id,Integer flag,String user,String remarks){
 	 String sql="update mb_cash_gift set audit_date=now(),status=?,audit_user=?,audit_msg=?  where gift_id=?";
 	 int  count= Sp.get().getDefaultJdbc().update(sql,new Object[]{flag,user,remarks,id});
 	
 	 return count>0;
  }
  
  public static boolean NTAuditGiftWithTransferFlag(Long id,Integer flag,String user,String remarks){
	 	 String sql="update mb_cash_gift set audit_date=now(),status=?,audit_user=?,audit_msg=?,transferflag=1  where gift_id=?";
	 	 int  count= Sp.get().getDefaultJdbc().update(sql,new Object[]{flag,user,remarks,id});
	 	 return count>0;
	  }
  
	 public  boolean NTupdate(int status,String gift_no){
		  String sql="update mb_self_app set status=? where gift_no=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{status,gift_no});
		  return count>0;
	}
  public  boolean NTupdate(){
	  		String sql="update mb_cash_gift set"
	  		+ " deposit_credit=?,net_credit=?,valid_credit=?,rate=?,payout=?,remarks=?"
	  		+ " where gift_no=?";
	  try{
		  int  count= Sp.get().getDefaultJdbc().update(sql,new Object[]{
				  deposit_credit,net_credit,valid_credit,rate,payout,remarks,
				gift_no});
		  
		  return count>0;
	  }catch(Exception ex){
		  ex.printStackTrace();
	  }
	 
	 	return false;
}

	public static boolean NTdeleteGifts(String giftId) {
		// TODO Auto-generated method stub
		String sql="delete from mb_cash_gift where gift_id in("+giftId+")";
		 int  count= Sp.get().getDefaultJdbc().update(sql);
		 if(count>0)return true;return false;
	}
  
}

