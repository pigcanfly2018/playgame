package controllers;

import java.util.Date;
import java.util.List;

import models.Role;
import models.RoleFunc;
import models.RoleRowMap;
import play.mvc.Controller;
import play.mvc.With;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

/**
 * 功能与角色管理
 * @author Administrator
 */
@With(value = {AjaxSecure.class})
public class RoleApp extends Controller{
	public static void index(String tabId){
		render(tabId);
	}
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_role");
		Sqler cntsql =new Sqler("select count(1) from mb_role");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("rolecode", queryKey);
			cntsql.and().left().like("rolecode", queryKey);
			sql.or().like("rolename", queryKey).right();
			cntsql.or().like("rolename", queryKey).right();
		}
		if(!(sdate==null||"".equals(sdate))){
			Date date =DateUtil.stringToDate(sdate, "yyyy-MM-dd");
			sql.and().ebigger("createDate",date);
			cntsql.and().ebigger("createDate",date);
		}
		if(!(edate==null||"".equals(edate))){
			Date date =DateUtil.stringToDate(edate, "yyyy-MM-dd");
			sql.and().esmaller("createDate",date);
			cntsql.and().esmaller("createDate",date);
		}
		sql.orberByDesc("createDate");
		
		List<Role> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new RoleRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count;
		renderJSON(p);
	}
	/**
	 * 保存角色
	 * @param role
	 */
	public static void saveRole(Role role){
		
		
		if("1".equals(params.get("kact"))){
			if(role.NTexits(role.rolecode)>0){
				renderText(JSONResult.failure(role.rolecode+"该角色代码已经存在"));
			}
			role.createdate=new Date(System.currentTimeMillis());
			role.createuser=session.get(Constant.userName);
			if(role.NTcreat()){
				renderText(JSONResult.success(role.rolecode+"角色创建成功!"));
			}else{
				renderText(JSONResult.failure(role.rolecode+"角色创建失败!"));
			}
		}
		
		if("2".equals(params.get("kact"))){
			if(role.NTexits(role.rolecode)==0){
				renderText(JSONResult.failure(role.rolecode+"该角色代码不存在!"));
			}
			if(role.NTupdate()){
				renderText(JSONResult.success(role.rolecode+"角色修改成功!"));
			}else{
				renderText(JSONResult.failure(role.rolecode+"角色修改失败!"));
			}
		}
	}
	
	/**
	 * 删除角色
	 * @param rolecode
	 */
	public static void deleteRole(String idcode){
		String rolecode=idcode;
		if(PageUtil.blank(rolecode)){
			renderText(JSONResult.failure("非法操作！"));
		}
		Role role =new Role();
		role.rolecode=rolecode;
		if(role.NTdelete()){
			renderText(JSONResult.success("删除角色成功!"));
		}else{
			renderText(JSONResult.failure("删除角色失败!"));
		}
	}
	
	public static void grant(String[]funccodes,String rolecode){
		if(PageUtil.blank(rolecode)){
			renderText(JSONResult.failure("非法操作！"));
		}
		String createuser=session.get(Constant.userName);
		RoleFunc rf=new RoleFunc();
		rf.rolecode=rolecode;
		try{
			rf.NTdeleteByRolecode(rolecode);
			for(String funccode:funccodes){
				if(PageUtil.blank(funccode)){
					continue;
				}
				RoleFunc r=new RoleFunc();
				r.rolecode=rolecode;
				r.funccode=funccode;
				r.createdate=new Date(System.currentTimeMillis());
				r.createuser=createuser;
				r.NTcreate();
		
			}
			renderText(JSONResult.success("功能分配成功!"));
		}catch(Exception e){
			e.printStackTrace();
			renderText(JSONResult.failure("功能分配失败!"));
		}

	}
}
