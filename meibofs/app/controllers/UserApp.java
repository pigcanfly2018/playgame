package controllers;

import java.util.Date;
import java.util.List;

import models.Role;
import models.User;
import models.UserRowMap;
import play.mvc.Controller;
import play.mvc.With;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.MD5;
import util.PageUtil;
import util.Product;
import util.Sp;
import util.Sqler;


@With(value = {AjaxSecure.class})
public class UserApp extends Controller{
	
	/**
	 * 
	 * @param start
	 * @param limit
	 * @param page
	 * @param sdate
	 * @param edate
	 * @param sort
	 * @param queryKey
	 * @throws Exception
	 */
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_user");
		Sqler cntsql =new Sqler("select count(1) from mb_user");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("loginname", queryKey);
			cntsql.and().left().like("loginname", queryKey);
			sql.or().like("realname", queryKey).right();
			cntsql.or().like("realname", queryKey).right();
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
		List<Role> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new UserRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	/**
	 * 保存一个客户
	 * @param user
	 */
	public static void saveUser(User user){
		if("1".equals(params.get("kact"))){
			if(user.NTexits(user.loginname)>0){
				renderText(JSONResult.failure(user.loginname+"该用户已经存在"));
			}
			user.createdate=new Date(System.currentTimeMillis());
			user.createuser=session.get(Constant.userName);
			user.flag=true;
			user.password=MD5.md5(user.password);
			user.productId=Product.code;
			if(user.issuper){
				User my=User.NTgetByLoginName(session.get(Constant.userName));
				if(my==null){
					renderText(JSONResult.failure("操作失败,您非超级管理员。"));
				}
				
			}
			if(user.NTcreat()){
				renderText(JSONResult.success(user.loginname+"用户创建成功!"));
			}else{
				renderText(JSONResult.failure(user.loginname+"用户创建失败!"));
			}
		}
		
		if("2".equals(params.get("kact"))){
			if(user.NTexits(user.loginname)==0){
				renderText(JSONResult.failure(user.loginname+"该用户不存在!"));
			}
			if(user.NTupdate()){
				renderText(JSONResult.success(user.loginname+"该用户修改成功!"));
			}else{
				renderText(JSONResult.failure(user.loginname+"该用户修改失败!"));
			}
		}
	}
	
	public static void deleteUser(String idcode){
		String loginname=idcode;
		if(PageUtil.blank(loginname)){
			renderText(JSONResult.failure("非法操作！"));
		}
		User user =new User();
		user.loginname=loginname;
		if(user.NTdelete()){
			renderText(JSONResult.success("删除用户成功!"));
		}else{
			renderText(JSONResult.failure("删除用户失败!"));
		}
	}
	
	public static void resetPwd(String idcode){
		String loginname=idcode;
		if(PageUtil.blank(loginname)){
			renderText(JSONResult.failure("非法操作！"));
		}
		User user =new User();
		user.loginname=loginname;
		user.password=MD5.md5("user#45y");
		if(user.NTupdatePwd()){
			renderText(JSONResult.success("密码设置成功!用户名:"+loginname+",密码为:user#45y"));
		}else{
			renderText(JSONResult.failure("密码设置失败!"));
		}
		
		
	}
}
