package controllers;

import java.util.List;

import models.Agent;
import models.AgentRowMap;
import models.Customer;
import models.CustomerRowMap;
import models.Func;
import models.Item;
import models.ItemRowMap;
import models.Role;
import play.mvc.Controller;
import play.mvc.With;
import util.Constant;
import util.ExtPage;
import util.JSONResult;
import util.MyTreeUtil;
import util.PageUtil;
import util.Sp;
import util.Sqler;
import util.TreeNode;
@With(value = {AjaxSecure.class})
public class ComboboxApp extends Controller{
	
	/**
	 * 获取功能树信息 
	 */
	public static void getFuncsTree(){
		Func f=Func.NTget("F000000");
		f.loginname=session.get(Constant.userName);
		TreeNode node =f.convertNode();
		MyTreeUtil.getModelTree(node,f,true,false);
		renderJSON(new TreeNode[]{node});
	}
	public static void getRoles()throws Exception{
		renderJSON(Role.NTall());
	}
	
	public static void getItems(String code)throws Exception{
		String sql="select * from mb_item where groupcode=? order by itemcode";
		List<Item> roleList=Sp.get().getDefaultJdbc().query(sql,new Object[]{code},new ItemRowMap());
		renderJSON(JSONResult.conver(roleList,true));
	}
	
	public static void getCustomersBySortName(String query,int start,int limit)throws Exception{
		Sqler sql =new Sqler("select * from mb_customer");
		Sqler cntsql =new Sqler("select count(1) from mb_customer");
		if(!PageUtil.blank(query)){
			sql.and().left().like("login_name", query);
			cntsql.and().left().like("login_name", query);
			sql.or().like("phone", query).right();
			cntsql.or().like("phone", query).right();
		}
		sql.orberByDesc("create_date");
		List<Customer> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new CustomerRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	
	public static void getAgentsBySortedName(String query,int start,int limit)throws Exception{
		Sqler sql =new Sqler("select * from mb_agent");
		Sqler cntsql =new Sqler("select count(1) from mb_agent");
		if(!PageUtil.blank(query)){
			sql.and().left().like("login_name", query);
			cntsql.and().left().like("login_name", query);
			sql.or().like("phone", query).right();
			cntsql.or().like("phone", query).right();
		}
		sql.orberByDesc("create_date");
		List<Agent> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new AgentRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	/**
	 * 获取代理
	 * @param query
	 * @param start
	 * @param limit
	 * @throws Exception
	 */
	public static void getAgentsBySortName(String query,int start,int limit)throws Exception{
		Sqler sql =new Sqler("select * from mb_customer");
		Sqler cntsql =new Sqler("select count(1) from mb_customer");
		if(!PageUtil.blank(query)){
			sql.and().left().like("login_name", query);
			cntsql.and().left().like("login_name", query);
			sql.or().like("tel", query).right();
			cntsql.or().like("tel", query).right();
		}
		sql.and().left().equals("agent", true).right();
		cntsql.and().left().equals("agent", true).right();
		sql.orberByDesc("create_date");
		List<Customer> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new CustomerRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
}
