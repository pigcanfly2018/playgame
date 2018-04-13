package controllers;

import java.util.Date;
import java.util.List;

import models.Func;
import models.FuncRowMap;
import models.RoleFunc;
import play.mvc.Controller;
import play.mvc.With;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.MyTree;
import util.MyTreeUtil;
import util.PageUtil;
import util.Sp;
import util.Sqler;
import util.TreeHandler;
import util.TreeNode;

/**
 * 功能与角色管理
 * @author Administrator
 */
@With(value = {AjaxSecure.class})
public class FuncApp extends Controller{
	
	public static void index(String tabId){
		render(tabId);
	}
	/**
	 * 根据权限获取第一层
	 */
	public static void getChilds(String pcode){
		if(pcode==null){

			List<Func> func=Func.getChindsByPcode("F000000",session.get(Constant.userName));
			renderJSON(func);
		}else{
			Func f=Func.NTget(pcode);
			f.loginname=session.get(Constant.userName);
			TreeNode node =f.convertNode();
			MyTreeUtil.getModelTree(node,f,true,false);
			renderJSON(new TreeNode[]{node});
		}
	}

	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_func");
		Sqler cntsql =new Sqler("select count(1) from mb_func");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("funccode", queryKey);
			cntsql.and().left().like("funccode", queryKey);
			sql.or().like("funcname", queryKey).right();
			cntsql.or().like("funcname", queryKey).right();
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
		sql.orberByAsc("funccode ");
		List<Func> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new FuncRowMap());
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
	public static void saveFunc(Func func){
		if("1".equals(params.get("kact"))){
			if(func.NTexits(func.funccode)>0){
				renderText(JSONResult.failure(func.funccode+"该功能编码已经存在"));
			}
			func.createdate=new Date(System.currentTimeMillis());
			func.createuser=session.get(Constant.userName);
			if(func.NTcreat()){
				renderText(JSONResult.success(func.funccode+"功能创建成功!"));
			}else{
				renderText(JSONResult.failure(func.funccode+"功能创建失败!"));
			}
		}
		if("2".equals(params.get("kact"))){
			if(func.NTexits(func.funccode)==0){
				renderText(JSONResult.failure(func.funccode+"该功能编码不存在"));
			}
			if(func.NTupdate()){
				renderText(JSONResult.success(func.funccode+"功能修改成功!"));
			}else{
				renderText(JSONResult.failure(func.funccode+"功能修改失败!"));
			}
		}
	}
	
	public static void deleteFunc(String idcode){
		String funccode=idcode;
		if(PageUtil.blank(funccode)){
			renderText(JSONResult.failure("非法操作！"));
		}
		Func func =new Func();
		func.funccode=funccode;
		if(func.NTdelete()){
			renderText(JSONResult.success("删除功能成功!"));
		}else{
			renderText(JSONResult.failure("删除功能失败!"));
		}
	}
	
	public static void getFuncsByRole(String rolecode){
		
		Func f=Func.NTget("F000000");
		f.loginname=session.get(Constant.userName);
		TreeNode node =f.convertNode();
		RoleFunc rf=new RoleFunc();
		rf.rolecode=rolecode;
		final List<RoleFunc> rflist=rf.NTgetRF(rolecode);
		for(RoleFunc r:rflist){
			if(f.funccode.equals(r.funccode)){
				node.checked=true;
				break;
			}
		}
		MyTreeUtil.getModelTree2(node,f,new TreeHandler(){
			public void handle(MyTree tree, TreeNode node) {
				Func f=(Func)tree;
				boolean checked=false;
				for(RoleFunc r:rflist){
					if(f.funccode.equals(r.funccode)){
						checked=true;
						break;
					}
				}
				node.checked=checked;
				node.expanded=true;
			}},true);
		renderJSON(new TreeNode[]{node});
	}
	
}
