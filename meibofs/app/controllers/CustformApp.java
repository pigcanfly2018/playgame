package controllers;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import models.Custform;
import models.CustformRowMap;
import models.Customer;
import models.DictRender;
import play.mvc.Controller;
import play.mvc.With;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;
public class CustformApp extends Controller{
	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_custform");
		Sqler cntsql =new Sqler("select count(1) from mb_custform");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey);
			cntsql.and().left().like("login_name", queryKey);

			sql.or().like("phone", queryKey).right();
			cntsql.or().like("phone", queryKey).right();
		}
		if(!(sdate==null||"".equals(sdate))){
			Date date =DateUtil.stringToDate(sdate, "yyyy-MM-dd");
			sql.and().ebigger("create_date",date);
			cntsql.and().ebigger("create_date",date);
		}
		if(!(edate==null||"".equals(edate))){
			Date date =DateUtil.stringToDate(edate, "yyyy-MM-dd");
			sql.and().esmaller("create_date",date);
			cntsql.and().esmaller("create_date",date);
		}
		sql.orberByDesc("create_date");
		List<Custform> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new CustformRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	public static void saveCustform(Customer customer){
		Customer  c=Customer.NTgetCustomerByLoginName(customer.login_name);
		if(c==null){
			renderText(JSONResult.failure("该用户不存在!"));
		}
		
		if(customer.bank_name!=null&&customer.bank_name.contains("请选择")){
			customer.bank_name="";
		}
		if(customer.account_type!=null&&customer.account_type.contains("请选择")){
			customer.account_type="";
		}
		
		if(!StringUtils.trimToEmpty(c.phone) .equals("")){
			if(StringUtils.trimToEmpty(c.phone) .equals(StringUtils.trimToEmpty(customer.phone))){
				renderText(JSONResult.success("用户新手机号与原先一致,无须修改!"));
			}
		}
		
		if(!StringUtils.trimToEmpty(c.real_name) .equals("")){
			if(StringUtils.trimToEmpty(c.real_name) .equals(StringUtils.trimToEmpty(customer.real_name))){
				renderText(JSONResult.success("用户新姓名与原先一致,无须修改!"));
			}
		}
		
		
		if(!StringUtils.trimToEmpty(c.email) .equals("")){
			if(StringUtils.trimToEmpty(c.email) .equals(StringUtils.trimToEmpty(customer.email))){
				renderText(JSONResult.success("用户新邮箱与原先一致,无须修改!"));
			}
		}
		
		if(!StringUtils.trimToEmpty(c.qq) .equals("")){
			if(StringUtils.trimToEmpty(c.qq) .equals(StringUtils.trimToEmpty(customer.qq))){
				renderText(JSONResult.success("用户新qq与原先一致,无须修改!"));
			}
		}
		
		if(!StringUtils.trimToEmpty(c.bank_name) .equals("")){
			if(StringUtils.trimToEmpty(c.bank_name) .equals(StringUtils.trimToEmpty(customer.bank_name))){
				renderText(JSONResult.success("用户新银行名称与原先一致,无须修改!"));
			}
		}
		
		
		if(!StringUtils.trimToEmpty(c.account) .equals("")){
			if(StringUtils.trimToEmpty(c.account) .equals(StringUtils.trimToEmpty(customer.account))){
				renderText(JSONResult.success("用户新银行账户与原先一致,无须修改!"));
			}
		}
		
		
		if(!StringUtils.trimToEmpty(c.account_type) .equals("")){
			if(StringUtils.trimToEmpty(c.account_type) .equals(StringUtils.trimToEmpty(customer.account_type))){
				renderText(JSONResult.success("用户新银行账户类型与原先一致,无须修改!"));
			}
		}
		
		
		if(!StringUtils.trimToEmpty(c.bank_dot) .equals("")){
			if(StringUtils.trimToEmpty(c.bank_dot) .equals(StringUtils.trimToEmpty(customer.bank_dot))){
				renderText(JSONResult.success("用户新银行开户地址与原先一致,无须修改!"));
			}
		}
		
		
		
			
		
		Custform form =new Custform();
		form.login_name=c.login_name;
		form.cust_id=c.cust_id;	
		if(StringUtils.isNotBlank(customer.real_name)){
			form.real_name=customer.real_name;
		}else{
			form.real_name=c.real_name;
		}
		
		if(StringUtils.isNotBlank(customer.phone)){
			form.phone=customer.phone;
		}else{
			form.phone=c.phone;
		}
		
		if(StringUtils.isNotBlank(customer.email)){
			form.email=customer.email;
		}else{
			form.email=c.email;
		}
		
		if(StringUtils.isNotBlank(customer.qq)){
			form.qq=customer.qq;
		}else{
			form.qq=c.qq;
		}
		
		if(StringUtils.isNotBlank(customer.bank_name)){
			form.bank_name=customer.bank_name;
		}else{
			form.bank_name=c.bank_name;
		}
		
		if(StringUtils.isNotBlank(customer.account)){
			form.account=customer.account;
		}else{
			form.account=c.account;
		}
		
		if(StringUtils.isNotBlank(customer.account_type)){
			form.account_type=customer.account_type;
		}else{
			form.account_type=c.account_type;
		}
		
		if(StringUtils.isNotBlank(customer.bank_dot)){
			form.bank_dot=customer.bank_dot;
		}else{
			form.bank_dot=c.bank_dot;
		}
		
		
		//form.phone=customer.phone;
		//form.email=customer.email;
		//form.qq=customer.qq;
		//form.bank_name=customer.bank_name;
		//form.account=customer.account;
		//form.account_type=customer.account_type;
		//form.bank_dot=customer.bank_dot;
		form.reason=params.get("customer.reason");
		

		form.org_real_name=c.real_name;
		form.org_phone=c.phone;
		form.org_email=c.email;
		form.org_qq=c.qq;
		form.org_bank_name=c.bank_name;
		form.org_account=c.account;
		form.org_account_type=c.account_type;
		form.org_bank_dot=c.bank_dot;
		
		form.status=1;
		form.create_user=session.get(Constant.userName);
		if(form.NTcreat()>0){
			renderText(JSONResult.success("提交成功!"));
		}
		renderText(JSONResult.failure("提交失败!"));
	}
	
	public static void detail (Long id){
	    Custform form =Custform.NTfindById(id);
	    
	    if(form==null){
	    	renderText(JSONResult.failure("提案不存在!"));
	    }
	    boolean phone_flag = false;
	    boolean qq_flag = false;
	    boolean account_flag = false;
	    if(! StringUtils.trimToEmpty(form.phone) .equals(StringUtils.trimToEmpty(form.org_phone))){
	    	phone_flag = true;
		}
	    
	    if(! StringUtils.trimToEmpty(form.qq) .equals(StringUtils.trimToEmpty(form.org_qq))){
	    	qq_flag = true;
		}
	    
	    if(! StringUtils.trimToEmpty(form.account) .equals(StringUtils.trimToEmpty(form.org_account))){
	    	account_flag = true;
		}
	    
	    
	    if(form.org_phone != null && !form.org_phone.equals("")){
	    	form.org_phone = form.org_phone.substring(0, 3)+"****"+form.org_phone.substring(7);
		}
	    if(form.org_qq != null && !form.org_qq.equals("")){
	    	form.org_qq = form.org_qq.substring(0, 2)+"***"+form.org_qq.substring(form.org_qq.length()-2,form.org_qq.length());
		}
	    if(form.org_account != null && !form.org_account.equals("") && form.org_account.length() > 4 ){
	    	form.org_account = form.org_account.substring(0, 4)+"***"+form.org_account.substring(form.org_account.length()-4,form.org_account.length());
		}
	    
		DictRender rd =new DictRender();
	    render(form,rd,phone_flag,qq_flag,account_flag);
	}
	
	public static void cancle(Long id,String remarks){
		    Custform form =Custform.NTfindById(id);
		    if(form==null){
		        renderText(JSONResult.failure("提案不存在!"));
		    }
		    if(form.status!=1){
		    	renderText(JSONResult.failure("提案不容许废除!"));
		    }
		   if(Custform.NTupdateFlag(2, remarks,  session.get(Constant.userName), id)){
			   renderText(JSONResult.success("操作成功!")); 
		    }
		   renderText(JSONResult.failure("提交失败!"));
		
	}
	
	public static void audit(Long id,Integer flag,String remarks){
	    Custform form =Custform.NTfindById(id);
	    if(form==null){
	        renderText(JSONResult.failure("提案不存在!"));
	    }
	    //修改客户资料
	   int status=3;
	   if(flag==-2)status=2;
	   if(status==3){
		   boolean f= Customer.NTmodInfo(form.real_name, 
				  form.phone, form.email, form.qq,
				  form.bank_name, form.account, 
				  form.account_type, form.bank_dot, form.cust_id);
		   if(!f){
			   renderText(JSONResult.failure("提交失败!"));
		   }
		   
	   }
	   if(Custform.NTupdateFlag(status, remarks,  session.get(Constant.userName), id)){
		   renderText(JSONResult.success("操作成功!")); 
	    }
	    renderText(JSONResult.failure("提交失败!"));
	
       }
}
