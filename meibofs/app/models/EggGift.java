package models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class EggGift {
	
	public Long gift_id;
    public String gift_code;
    public Long cust_id;
    public String login_name;
    public String real_name;
    public String trophy_code;
    public String trophy_name;
    public Integer trophy_count;
    public Date create_date;
    public String create_user;
    public Integer status;
    public String remark;
    public String audit_user;
    public Date audit_date;
    public String ref_gift_code;
    public Long trophy_id;
    public BigDecimal score;
    public BigDecimal cost;
    public String trophy_type;
    
    public static Long NTcreat(String gift_code,Long cust_id,String login_name,String real_name,String trophy_code,String trophy_name,
    		int trophy_count,int status,String ref_gift_code,BigDecimal score,BigDecimal cost,String trophy_type){
		  String sql="insert into  mb_egg_gift (gift_code,cust_id,login_name,real_name,trophy_code,trophy_name,trophy_count,"
		  		+ "status,ref_gift_code,score,cost,trophy_type,create_date,create_user) values(?,?,?,?,?,?,?,?,?,?,?,?,now(),?)";
		  Object[] objects =new Object[]{gift_code,cust_id,login_name,real_name,trophy_code,trophy_name,
				  trophy_count,status,ref_gift_code,score,cost,trophy_type,login_name};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		  return keyHolder.getKey().longValue();
	 }
	 public static EggGift NTget(Long id){
		 String sql="select * from mb_egg_gift where gift_id=?";
    	 List<EggGift> giftList= Sp.get().getDefaultJdbc().query(sql, new Object[]{id}, new EggGiftRowMap());
    	 if(giftList.size()>0){
    		 return giftList.get(0);
    	 }
    	 return null;
	 }
	 public static boolean NTAuditGift(Long giftId, Integer status, String user,String remarks){
		 String sql ="update mb_egg_gift set status=? ,audit_user=?,remark=?, audit_date=now() where gift_id =? ";
		 int c=Sp.get().getDefaultJdbc().update(sql, new Object[]{status,user,remarks,giftId});
		 return c>0;
	 }

}
