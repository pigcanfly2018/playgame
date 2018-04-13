package models;

import java.util.Date;

import util.Sp;

public class Recovery {
	
	public Long rec_id;
	
	public String create_user;
	
	public String msg;
	
	public Date create_date;
	
	public String msg_type;
	
	
	public boolean NTcreate(){
		String sql="insert into sb_recovery(create_user,msg,create_date,msg_type) values(?,?,now(),?)";
		int count = Sp.get().getDefaultJdbc().update(sql,new Object[] {create_user, msg,msg_type});
		return count > 0;
	}
	
	

}
