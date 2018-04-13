package models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import util.MyTree;
import util.Sp;
import util.TreeNode;

public class Func implements MyTree<Func>,Serializable{
	public String funccode;
	public String funcname;
	public String pfunccode;
	public Boolean isgroup;
	public String createuser;
	public String url;
	public Date createdate;
	public Boolean isbut;
	public String icon;
	
	/**
	 * 辅助字段
	 */
	public String loginname;
	
	public boolean NTcreat(){
		String sql="insert into mb_func(funccode,funcname,pfunccode,isgroup," +
				"createuser,createdate,url,isbut,icon) values(?,?,?,?,?,?,?,?,?)";
		 int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{funccode,funcname,pfunccode,
				 isgroup,createuser,createdate,url,isbut,icon});
		  return count>0;
	}
	public boolean NTupdate(){
		String sql="update mb_func set funcname=?,pfunccode=?,isgroup=?,url=?,isbut=?,icon=? where funccode=?";
		 int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{funcname,pfunccode,
				 isgroup,url,isbut,icon,funccode});
		  return count>0;
	}
	
	public boolean NTdelete(){
		 String sql="delete from mb_func  where funccode=?";
		 int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{funccode});
		 return count>0;
	}
	
	
	
	 /**
	  * 获取总数
	  * @return
	  */
	 public static int NTcount(){
		  int count=Sp.get().getDefaultJdbc().queryForObject("select count(1) from  mb_func ",
					new Object[]{},Integer.class);
		  return count;
	 }
	 
	 
	 public static int NTexits(String funccode){
		  int count=Sp.get().getDefaultJdbc().queryForObject("select count(1) from  mb_func where funccode=? ",
					new Object[]{funccode},Integer.class);
		  return count;
	 }

	 /**
	  * @param pcode
	  * @return
	  */

	 public static List<Func> getChindsByPcode(String pcode,String username){
		 User user =User.NTgetByLoginName(username);
		 if(user.issuper){
			  String sql="select * from mb_func where pfunccode=? and isbut=0 order by funccode";
			  List<Func> funcs=Sp.get().getDefaultJdbc().query(sql,
						new Object[]{pcode},new FuncRowMap());
			  for(Func fc:funcs){
				  fc.loginname=username;
			  }
			  return funcs;
		 }else{
			 String sql="select c.funccode as funccode," +
			 		"c.funcname as funcname," +
			 		"c.pfunccode as pfunccode," +
			 		"c.isgroup as isgroup," +
			 		"c.createuser as createuser," +
			 		"c.url as url," +
			 		"c.createdate as createdate," +
			 		"c.isbut as isbut," +
			 		"c.icon as icon  from mb_user a inner join mb_role_func b on a.rolecode=b.rolecode " +
			 		"inner join mb_func c on b.funccode=c.funccode " +
			 		"where a.loginname=? and pfunccode=? and c.isbut=0  order by c.funccode";
			 List<Func> funcs=Sp.get().getDefaultJdbc().query(sql,
						new Object[]{user.loginname,pcode},new FuncRowMap());
			 for(Func fc:funcs){
				  fc.loginname=username;
			  }
			  return funcs;
		 }
	 }
	 
	 public static List<Func> getChindsByPcode(String pcode,String username,boolean includeBut){
		 User user =User.NTgetByLoginName(username);
		 if(user.issuper){
			  String sql=includeBut?"select * from mb_func where pfunccode=? order by funccode":
				  "select * from mb_func where pfunccode=? and isbut=0 order by funccode";
			  List<Func> funcs=Sp.get().getDefaultJdbc().query(sql,
						new Object[]{pcode},new FuncRowMap());
			  for(Func fc:funcs){
				  fc.loginname=username;
			  }
			  return funcs;
		 }else{
			 String sql="select c.funccode as funccode," +
			 		"c.funcname as funcname," +
			 		"c.pfunccode as pfunccode," +
			 		"c.isgroup as isgroup," +
			 		"c.createuser as createuser," +
			 		"c.url as url," +
			 		"c.createdate as createdate," +
			 		"c.isbut as isbut," +
			 		"c.icon as icon  from mb_user a inner join mb_role_func b on a.rolecode=b.rolecode " +
			 		"inner join mb_func c on b.funccode=c.funccode " +
			 		"where a.loginname=? and pfunccode=? "+(includeBut?"":"and c.isbut=0")+"  order by c.funccode";
			 List<Func> funcs=Sp.get().getDefaultJdbc().query(sql,
						new Object[]{user.loginname,pcode},new FuncRowMap());
			 for(Func fc:funcs){
				  fc.loginname=username;
			  }
			  return funcs;
		 }
	 }
	 
	 public static Func NTget(String pcode){
		  String sql="select * from mb_func where funccode=?";
		  Func funcs=(Func) Sp.get().getDefaultJdbc().queryForObject(sql,
					new Object[]{pcode},new FuncRowMap());
	      return funcs;
	 }
	 
	
	public boolean isLeaf() {
		return !isgroup;
	}
	
	public TreeNode convertNode() {
		TreeNode node =new TreeNode();
		node.id=String.valueOf(this.funccode);
		node.text=this.funcname;
		node.leaf=isLeaf();
		node.attributes.put("url", url);
		node.url=url;
		node.code=this.funccode;
		node.expanded=true;
		return node; 
	}
	
	public List<Func> getChilds(boolean includeBut) {

		return getChindsByPcode(this.funccode,this.loginname,includeBut);
	}
	
	
	
	public Func getParent() {
		
		return null;
	} 	 
}
