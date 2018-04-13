package models;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class Billboard {

	public Long board_id;
	public String login_name;
	public BigDecimal bet_amount;
    public Date create_date;
    public Integer flag;
    
    public  Long NTcreat(){
		  final String sql="insert into  mb_billboard (login_name,bet_amount,create_date,flag) values(?,?,now(),0)";
		  final Object[] objects=new Object[]{login_name,bet_amount};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		  return keyHolder.getKey().longValue();
    }
    
    public static boolean NTUpdateBillboard(){
    	 String sql="update mb_billboard set flag=? where create_date < CURDATE() ";
    	 int  count= Sp.get().getDefaultJdbc().update(sql,new Object[]{1});
    	 return count>0;
    }
    
}
