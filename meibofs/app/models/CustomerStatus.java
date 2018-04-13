package models;

import java.util.Date;
import java.util.List;

import util.Sp;

public class CustomerStatus {
	
	public Long cust_id;
    public String login_name;
    
    public Boolean mg_transfer_flag;
    public Boolean pt_transfer_flag;
    public Boolean ag_transfer_flag;
    public Boolean bbin_transfer_flag;
    public Boolean kg_transfer_flag;
    public Date create_date;
    
    

    public CustomerStatus() {
		super();
		this.mg_transfer_flag = false;
		this.pt_transfer_flag = false;
		this.ag_transfer_flag = false;
		this.bbin_transfer_flag = false;
		this.kg_transfer_flag = false;
	}

	public  boolean NTcreat(){
		  String sql="insert into  mb_customer_status (cust_id,login_name,"
		  		+ "mg_transfer_flag,pt_transfer_flag,ag_transfer_flag,bbin_transfer_flag,kg_transfer_flag,create_date) values(?,?,?,?,?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{cust_id,login_name,
				  mg_transfer_flag,pt_transfer_flag,ag_transfer_flag,bbin_transfer_flag,kg_transfer_flag,create_date});
		  return count>0;
	 }
    
    public static CustomerStatus NTgetCustomerByName(String login_name){
		  String sql="select * from mb_customer_status where login_name =?";
		  List<CustomerStatus> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{login_name},new CustomerStatusRowMap());
		  if(list.size()>0){
			  return list.get(0);
		  }
		  return null;
	 }
    
    public boolean NTmodCustFlag(){
		 String sql="update mb_customer_status set mg_transfer_flag=?,pt_transfer_flag=?,ag_transfer_flag=?,bbin_transfer_flag=?,kg_transfer_flag=? where cust_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{mg_transfer_flag,pt_transfer_flag,ag_transfer_flag,bbin_transfer_flag,kg_transfer_flag,cust_id});
		  return count>0; 
	}
    
    public boolean resetTransferFlag(){
		 String sql="update mb_customer_status set mg_transfer_flag=0,pt_transfer_flag=0,ag_transfer_flag=0,bbin_transfer_flag=0,kg_transfer_flag=0 where cust_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{cust_id});
		  return count>0; 
	}
    
    
    public static CustomerStatus NTgetCustomerById(Long cust_id){
		  String sql="select * from mb_customer_status where cust_id =?";
		  List<CustomerStatus> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{cust_id},new CustomerStatusRowMap());
		  if(list.size()>0){
			  return list.get(0);
		  }
		  return null;
	 }
    
}
