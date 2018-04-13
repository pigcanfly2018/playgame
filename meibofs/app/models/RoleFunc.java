package models;

import java.util.Date;
import java.util.List;

import util.Sp;

public class RoleFunc {
	   public String rolecode;
       public String funccode;
       public Date createdate;
       public String createuser;
       public List<RoleFunc> NTgetRF(String rolecode){
    		List<RoleFunc> roleList=Sp.get().getDefaultJdbc().query("select * from mb_role_func where rolecode =?  order by rolecode asc",
    				new Object[]{rolecode},new RoleFuncRowMap());
            return roleList;
       }
       
       public boolean NTdeleteByRolecode(String rolecode){
    	   String sql="delete from mb_role_func where rolecode=?";
    		 int count=Sp.get().getDefaultJdbc().update(sql,
 					new Object[]{rolecode});
 		   return count>0;
       }
       
       public boolean NTcreate(){
    	   String sql="insert into mb_role_func(rolecode,funccode,createdate,createuser) values(?,?,?,?)";
    	   int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{rolecode,funccode,createdate,createuser});
    	   return count>0;
       }
}
