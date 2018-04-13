package models;

import java.util.Date;
import java.util.List;

import util.MD5;
import util.Sp;

public class User {
	 public String loginname;
	 public String realname;
	 public String password;
	 public Boolean flag;
	 public Boolean issuper;
	 public String productId;

	 public String rolecode;
	 public  Date createdate;
	 public String createuser;
	 public String lastloginip;
	 public Date lastlogindate;
	 /**
	  * 登录
	  * @param loginname
	  * @param password
	  * @param ip
	  * @return
	  */
	 public  boolean NTlogin(){
		  String npassword=MD5.md5(password);
		  lastlogindate=new Date(System.currentTimeMillis());
		  int count=Sp.get().getDefaultJdbc().queryForObject("select count(1) from mb_USER where flag=1 and loginname=? and password=?",
				new Object[]{loginname,npassword},Integer.class);
		  if(count>0){
			  Sp.get().getDefaultJdbc().update("update mb_USER set lastLoginIp =?,lastlogindate=? where  loginname=?", new Object[]{lastloginip,lastlogindate,loginname});
		  }
		  return count>0;
	 }
	 public static  boolean NTtestPwd(String username,String password){
		  String npassword=MD5.md5(password);
		  int count=Sp.get().getDefaultJdbc().queryForObject("select count(1) from mb_USER where flag=1 and loginname=? and password=?",
				new Object[]{username,npassword},Integer.class);
		  return count>0;
	 }
	 
	 public static  boolean NTmodPwd(String username,String password){
		  String npassword=MD5.md5(password);
		  String sql="update mb_USER set password=? where loginname=? ";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{npassword,username});
		  return count>0;
	 }
	 /**
	  * 根据用户名获取用户信息
	  * @param loginname
	  * @param password
	  * @param ip
	  * @return
	  */
	 public  User NTget(){
		  User user=(User) Sp.get().getDefaultJdbc().queryForObject("select * from  mb_USER where loginname=? ",
				new Object[]{loginname},new UserRowMap());
		  return user;
	 }
	 
	 public static User NTgetByLoginName(String loginname){
		  User user=(User) Sp.get().getDefaultJdbc().queryForObject("select * from  mb_USER where loginname=? ",
				new Object[]{loginname},new UserRowMap());
		  return user;
	 }
	 
	 /**
	  * 获取总数
	  * @return
	  */
	 @SuppressWarnings("deprecation")
	public static int NTcount(){
		  int count=Sp.get().getDefaultJdbc().queryForObject("select count(1) from  mb_USER ",
					new Object[]{},Integer.class);
		  return count;
	 }
	 
	 /**
	  * 获取用户列表信息
	  * @param loginname
	  * @param password
	  * @param ip
	  * @return
	  */
	 public static List<User> NTgetList(String loginname,String password,String ip){
		  List<User> users=Sp.get().getDefaultJdbc().query("select * from  mb_USER where flag=1 and loginname=? ",
				new Object[]{loginname},new UserRowMap());
		  return users;
	 }
	 
	 public  boolean NTcreat(){
		  String sql="insert into mb_USER(loginname,realname,password,flag,issuper,productid," +
		 		"rolecode,createdate,createuser)" +
		 		" values(?,?,?,?,?,?,?,now(),?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{loginname,realname,password,flag,
				  issuper,productId,rolecode,createuser});
		  return count>0;
	 }
	 
	 public int NTexits(String loginname){
		 int count=Sp.get().getDefaultJdbc().queryForObject("select count(1) from  mb_user where loginname=? ",
					new Object[]{loginname},Integer.class);
		 return count;
	 }
	 
	 public boolean NTupdate(){
		 String sql="update mb_USER set realname=?,flag=?,issuper=?,rolecode=? where loginname=?";
		 int count=Sp.get().getDefaultJdbc().update(sql,
					new Object[]{realname,flag,issuper,rolecode,loginname});
		 return count>0;
	 }
	 
	 public boolean NTupdatePwd(){
		 String sql="update mb_USER set password=? where loginname=?";
		 int count=Sp.get().getDefaultJdbc().update(sql,
					new Object[]{password,loginname});
		 return count>0;
	 }
	 
	 public boolean NTdelete(){
		 String sql="delete from mb_USER where loginname=?";
		 int count=Sp.get().getDefaultJdbc().update(sql,
					new Object[]{loginname});
		 return count>0;
	 }
	 
	 /**
	  * 根据角色获取是否有该权限
	  * @param username 
	  * @param funccode
	  * @return
	  */
	 public static int NTcountByRole(String username,String funccode){
		 if(NTgetByLoginName(username).issuper){
			 return 1;
		 }
		 String sql="select count(1) from mb_user a " +
		 		"inner join mb_role_func b on a.rolecode=b.rolecode " +
		 		"inner join mb_func c on b.funccode=c.funccode " +
		 		"where a.loginname=? and c.funccode=?";
		 int count=Sp.get().getDefaultJdbc().queryForObject(sql,
					new Object[]{username,funccode},Integer.class);
		 return count;
	 }
}
