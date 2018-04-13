package models;

import java.util.Date;
import java.util.List;

import util.Sp;

public class Role {
    public String rolecode;
    public String rolename;
    public Date createdate;
    public String createuser;
    public  boolean NTcreat(){
		  String sql="insert into mb_role (rolecode,rolename,createdate,createuser) values(?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{rolecode,rolename,createdate,createuser});
		  return count>0;
	 }
	 public  boolean NTupdate(){
		  String sql="update mb_role set rolename=? where rolecode=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{rolename,rolecode});
		  return count>0;
	 }
	 public  Role NTget(String rolecode){
		  Role role=(Role) Sp.get().getDefaultJdbc().queryForObject("select * from  mb_role where rolecode=? ",
					new Object[]{rolecode},new RoleRowMap());
		  return role;
	 }
	 public int NTexits(String rolecode){
		 int count=Sp.get().getDefaultJdbc().queryForObject("select count(1) from  mb_role where rolecode=? ",
					new Object[]{rolecode},Integer.class);
		 return count;
	 }
	 public boolean NTdelete(){
		 int count=Sp.get().getDefaultJdbc().update("delete from mb_role where rolecode=? ",
					new Object[]{rolecode});
		 System.out.println(count);
		 return count>0;
	 }
	 
	 public static List<Role> NTall(){
			List<Role> roleList=Sp.get().getDefaultJdbc().query("select * from mb_role order by rolecode asc",new RoleRowMap());
            return roleList;
	 }
	 
	 
}