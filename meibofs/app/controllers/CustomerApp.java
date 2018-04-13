package controllers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.internet.MimeMessage;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import models.Agent;
import models.Customer;
import models.CustomerRowMap;
import models.CustomerStatus;
import models.DictRender;
import models.User;
import models.UserLog;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import play.mvc.Controller;
import play.mvc.With;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.IPSeeker;
import util.JSONResult;
import util.MD5;
import util.PageUtil;
import util.Sp;
import util.Sqler;
import bsz.exch.service.Plat;

import com.ws.service.PlatService;

@With(value = {AjaxSecure.class})
public class CustomerApp extends Controller{
	
	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey,String status)throws Exception{
		Sqler sql =new Sqler("select * from mb_customer");
		Sqler cntsql =new Sqler("select count(1) from mb_customer");
		if(!PageUtil.blank(queryKey)){
			DictRender dict =new DictRender();
			String cust_level=dict.dictName2("cust_level", queryKey);
			System.out.println(cust_level);
			if(!queryKey.equals(cust_level)){
				sql.and().left().equals("cust_level", Integer.valueOf(cust_level)).right();
				cntsql.and().left().equals("cust_level", Integer.valueOf(cust_level)).right();
				if(User.NTcountByRole(session.get(Constant.userName), "F020105")<=0){
					sql.and().ebigger("create_date",DateUtil.getLast24Hour());
					cntsql.and().ebigger("create_date",DateUtil.getLast24Hour());
				}
			}else{
				sql.and().left().like("login_name", queryKey);
				cntsql.and().left().like("login_name", queryKey);
				
				sql.or().like("real_name", queryKey);
				cntsql.or().like("real_name", queryKey);
				
				sql.or().like("qq", queryKey);
				cntsql.or().like("qq", queryKey);
				
				sql.or().like("reg_ip", queryKey);
				cntsql.or().like("reg_ip", queryKey);
				
				sql.or().like("login_ip", queryKey);
				cntsql.or().like("login_ip", queryKey);
				
				sql.or().like("account", queryKey);
				cntsql.or().like("account", queryKey);
				
				sql.or().like("phone", queryKey).right();
				cntsql.or().like("phone", queryKey).right();
			}
		}else if(!PageUtil.blank(status)){
			sql.and().left().like("alipay_name", status).right();
			cntsql.and().left().like("alipay_name", status).right();
		}else{
			sql.and().ebigger("create_date",DateUtil.getLast24Hour());
			cntsql.and().ebigger("create_date",DateUtil.getLast24Hour());
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
		IPSeeker seeker = IPSeeker.getInstance();
		List<Customer> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new CustomerRowMap());
		for(Customer c:roleList){
			c.ip_addr=seeker.getAddress(c.reg_ip);
			if(c.phone != null && !c.phone.equals("")){
				c.phone = c.phone.substring(0, 3)+"****"+c.phone.substring(7);
			}
			
			if(c.qq != null && !c.qq.equals("") && c.qq.length()>4){
				c.qq = c.qq.substring(0, 2)+"***"+c.qq.substring(c.qq.length()-2,c.qq.length());
			}
			if(c.parent_id != null && c.parent_id !=0){
				Agent agent = Agent.NTget(c.parent_id);
				c.parent_name = agent.login_name;
			}
		}
		
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	/**
	 * 客户等级:黑名单,等级零，等级一，等级二，等级三，VIP
	 * @param customer
	 */
	public static void saveCustomer(Customer customer){
		if("1".equals(params.get("kact"))){
			if(customer.NTexist(customer.login_name)){
				renderText(JSONResult.failure(customer.login_name+"用户名已经存在!"));
			}
			if(customer.real_name==null||customer.real_name.trim().equals("")){
				renderText(JSONResult.failure("请填写真实姓名!"));
			}
			customer.create_user=session.get(Constant.userName);
			customer.create_date=new Date(System.currentTimeMillis());
			customer.reg_ip=request.remoteAddress;
			customer.flag=true;
			customer.cust_level=0;
			customer.s_email=false;
			customer.login_pwd=MD5.md5(customer.login_pwd);
			if(customer.sb_game!=null&&customer.sb_game.length()>3){
				customer.sb_flag=true;
			}
			customer.sb_actived=true;
			String ip=IpApp.getIpAddr();
			customer.login_times=0;
			customer.ag_game="daw"+customer.login_name;
			customer.ag_pwd="b123b123";
			customer.ag_flag=false;
			customer.ag_actived=true;
			customer.credit=new BigDecimal(0);
			
			customer.bbin_game="daw"+customer.login_name;
			customer.bbin_pwd="b123b123";
			customer.bbin_flag=false;
			customer.bbin_actived=true;
			
			customer.sb_game="daw"+customer.login_name;
			customer.sb_pwd="b123b123";
			customer.sb_flag=false;
			customer.sb_actived=true;
			
			customer.pt_game=("daw"+customer.login_name).toUpperCase();
			customer.pt_pwd="c123c123";
			customer.pt_flag=false;
			customer.pt_actived=true;
			
			customer.kg_game="daw"+customer.login_name;
			customer.kg_pwd="b123b123";
			customer.kg_flag=false;
			customer.kg_actived=true;
			
			customer.pp_game="daw"+customer.login_name;
			customer.pp_pwd="b123b123";
			customer.pp_flag=false;
			customer.pp_actived=true;
			
			customer.mg_game="daw"+customer.login_name;
			customer.mg_pwd="a123a123";
			customer.mg_flag=false;
			customer.mg_actived=true;
			customer.NTcreat();
			renderText(JSONResult.success("开户成功!"));
		}
		
	}
	
    /**
     * 激活客户
     * @param idcode
     */
	public static void actived(Long idcode){
		Customer cust =Customer.NTgetCustomer(idcode);
		if(cust.flag){
			renderText(JSONResult.failure("激活失败！该客户已经被激活。"));
		}
		boolean flag=false;
		String email="";
		if(cust.email!=null&&cust.email.length()>5){
			email=cust.email;
		}else{
			if(!"000000".equals(cust.qq)){
			   email=cust.qq+"@qq.com";
			}
		}
		if(email!=null&&email.length()>5){
			try{
			    JavaMailSender mail=(JavaMailSender)Sp.get().getBean("mailSender");
				MimeMessage mime = mail.createMimeMessage();  
			    MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");  
			    helper.setFrom("188shenbo@gmail.com");  
			    helper.setTo(cust.email);  
			    helper.setSubject("申博娱乐城(www.188shenbo.com)----会员注册激活信");  
			    StringBuffer text=new StringBuffer();
			    text.append("<html><body>");
			    text.append("<p>尊敬的"+cust.real_name+"会员：<p>");
			   // text.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;您好，恭喜您成功注册成为申博娱乐城会员，您的游戏账号为:"+cust.login_name+"，初始密码为："+cust.init_pass+" 。请你登陆游戏修改密码。申博娱乐城官方网站为 www.188shenbo.com <p>");
			    text.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;我们的24小时服务电话 400-6666-838，客服QQ：97626808， 有任何问题欢迎您随时联系我们。<p>");
			    text.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;祝您生活愉快！  <p>");
			    text.append("<p style='text-align:right'>申博娱乐城客服部上<p>");
			    text.append("</body></html>");
			    helper.setText(text.toString(), true);   
			    mail.send(mime);  
			    flag=true;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(Customer.NTactive(cust.cust_id, flag, session.get(Constant.userName))){
			if(!flag){
			     renderText(JSONResult.success("激活成功，但邮件发送失败！"));
			}else{
				 renderText(JSONResult.success("激活成功！"));
			}
			
		}else{
			 renderText(JSONResult.failure("激活失败！"));
		}
	}
	
	public static void saveCustomer2(Customer customer){
		Customer cust=Customer.NTgetCustomer(customer.cust_id);
		if(cust==null){
			renderText(JSONResult.failure("不存在该客户!"));
		}
		if(customer.sb_game!=null&&customer.sb_game.length()>3){
			customer.sb_flag=true;
		}
		
		Customer.NTmodInfo2(customer.email, customer.qq, 
				customer.remarks, customer.bank_name, 
				customer.account, customer.account_type,
				customer.bank_dot,customer.sb_game,
				customer.sb_pwd,customer.sb_flag,
				customer.cust_id);
		UserLog log = new UserLog();
		log.op_user = session.get(Constant.userName);
		log.log_msg ="完善用户"+cust.login_name+",将用户备注修改为"+customer.remarks;
		if(cust.remarks != null && !cust.remarks.equals("")){
			log.log_msg = log.log_msg + ",用户原先备注为"+cust.remarks;
		}
		log.NTcreat();
		renderText(JSONResult.success("修改成功!"));
	}
	
	
	
	public static void validatorUser(String login_name){
		if(Customer.NTexist(login_name)){
			renderText(JSONResult.failure("用户名:"+login_name+"已经存在!"));
		}
		renderText(JSONResult.success("成功!"));
		
	}
	public static void validatorRealName(String real_name){
		if(Customer.NTexitsRealName(real_name)){
			renderText(JSONResult.failure("真实名:"+real_name+"已经存在!"));
		}
		renderText(JSONResult.success("成功!"));
		
	}
	public static void validatorQQ(String qq){
		if("000000".equals(qq)){
			renderText(JSONResult.success("成功!"));
		}
		if(Customer.NTexitsQQ(qq)){
			renderText(JSONResult.failure("QQ:"+qq+"已经存在!"));
		}
		renderText(JSONResult.success("成功!"));
	}
	
	public static void validatorPhone(String phone){
		if(Customer.NTexitsPhone(phone)){
			renderText(JSONResult.failure("电话:"+phone+"已经存在!"));
		}
		renderText(JSONResult.success("成功!"));
	}
	public static void detail(Long cust_id){
		Customer customer=Customer.NTgetCustomer(cust_id);
		if(customer==null){
			renderText("不存在该客户资料!");
		}
		
		if(User.NTcountByRole(session.get(Constant.userName), "F020107")<=0){
			
			if(customer.phone != null && !customer.phone.equals("")){
				customer.phone = customer.phone.substring(0, 3)+"****"+customer.phone.substring(7);
			}
			if(customer.qq != null && !customer.qq.equals("")){
				customer.qq = customer.qq.substring(0, 2)+"***"+customer.qq.substring(customer.qq.length()-2,customer.qq.length());
			}
			
			if(customer.account != null && !customer.account.equals("")){
				customer.account = customer.account.substring(0, 3)+"***"+customer.account.substring(customer.account.length()-3,customer.account.length());
			}
			
		}
		
		DictRender rd=new DictRender();
//		if(customer.credit==null)customer.credit=new BigDecimal(0);
//		BigDecimal aginCredit=GameService.getAginBalance(customer,ip);
//		BigDecimal bbinCredit=GameService.getBbinBalance(customer,ip);
//		BigDecimal ptCredit=GameService.getPtBalance(customer,ip);
//		BigDecimal kgCredit=GameService.getKgBalance(customer, ip);
//		BigDecimal mgCredit=GameService.getMgBalance(customer, ip);
//		
//		BigDecimal totalCredit=customer.credit.add(aginCredit).add(bbinCredit).add(ptCredit).add(kgCredit).add(mgCredit);
		CustomerStatus cs  = CustomerStatus.NTgetCustomerById(cust_id);
		render(customer,rd,cs);
		//render(customer,rd,aginCredit,bbinCredit,totalCredit,ptCredit,kgCredit,mgCredit);
		
	}
	
	public static void transfertolocalfrombbin(Long cust_id){
		Customer customer=Customer.NTgetCustomer(cust_id);
		if(customer==null){
			renderText("不存在该客户资料!");
		}
		
		String order_no =""+System.currentTimeMillis();
		PlatService.transferOut(Plat.BBIN,customer.login_name,"127.0.0.1","系统转出(BBIN清理)","",session.get(Constant.userName),order_no,null);
		DictRender rd=new DictRender();

		CustomerStatus cs  = CustomerStatus.NTgetCustomerById(cust_id);
		render(customer,rd,cs);
		
		
	}
	
	public static void creditdetail(Long cust_id){
		Customer customer=Customer.NTgetCustomer(cust_id);
		if(customer==null){
			renderText("不存在该客户资料!");
		}
		
		String ip=IpApp.getIpAddr();
		if(customer.credit==null)customer.credit=new BigDecimal(0);
		
		BigDecimal aginCredit=PlatService.balance(Plat.AG, customer.login_name, ip);
		BigDecimal bbinCredit=PlatService.balance(Plat.BBIN, customer.login_name, ip);
		BigDecimal ptCredit=PlatService.balance(Plat.PT, customer.login_name, ip);
		BigDecimal kgCredit=PlatService.balance(Plat.KG, customer.login_name, ip);
		BigDecimal mgCredit=PlatService.balance(Plat.MG, customer.login_name, ip);
		BigDecimal sbCredit=PlatService.balance(Plat.VS, customer.login_name, ip);
		BigDecimal ppCredit=PlatService.balance(Plat.PP, customer.login_name, ip);
		
		BigDecimal totalCredit=customer.credit.add(aginCredit).add(bbinCredit).add(ptCredit).add(kgCredit).add(mgCredit).add(sbCredit).add(ppCredit);
		
		render(customer,aginCredit,bbinCredit,totalCredit,ptCredit,kgCredit,mgCredit,sbCredit,ppCredit);
		
	}
	
	public static void modLevel(Long cust_id,Boolean flag,Boolean bbin_actived,
			Boolean ag_actived,Boolean mg_actived,Boolean pt_actived,Boolean sb_actived,Boolean pp_actived,Boolean mod_pwd,String remarks,Integer cust_level,Boolean pt_flag,Boolean ag_flag,Boolean bbin_flag,Boolean mg_flag,Boolean sb_flag,Boolean pp_flag,Boolean online_pay,Boolean is_block,Boolean alipay_flag,Boolean bbinmobile_flag,Boolean promo_flag,Boolean bank_pay){
		String login_pwd=null;
		if(mod_pwd){
			 login_pwd=MD5.md5("a123b789");
		}
		if(Customer.NTmodCustStatus(cust_id,cust_level,flag,pt_actived,bbin_actived, ag_actived,mg_actived,sb_actived,pp_actived,pt_flag,bbin_flag,ag_flag,mg_flag,sb_flag,pp_flag,login_pwd,remarks,online_pay,is_block,alipay_flag,bbinmobile_flag,promo_flag,bank_pay)){
			
			UserLog log = new UserLog();
			log.op_user = session.get(Constant.userName);
			log.log_msg ="变更用户ID"+cust_id+"状态,等级变为:"+cust_level+",flag变为:"+flag+",promo_flag变为:"+promo_flag;
			if(mod_pwd){
				log.log_msg = log.log_msg+",用户重置密码";
			}
			log.NTcreat();
			
			if(mod_pwd){
				renderText(JSONResult.success("变更客户资料成功!客户的密码初始化为:a123b789"));
			}
			renderText(JSONResult.success("变更客户资料成功!"));
		}
		renderText(JSONResult.failure("变更客户资料失败!"));
	}
	
	public static void modPlatformPassword(Long cust_id,String bbinmobile_pwd,String pt_pwd,String mg_pwd){
		//System.out.println("cust_id"+cust_id+"   bbinmobile_pwd"+bbinmobile_pwd+"     pt_pwd"+pt_pwd+"     mg_pwd"+mg_pwd);
		Customer customer=Customer.NTgetCustomer(cust_id);
		String ip=IpApp.getIpAddr();
		if(bbinmobile_pwd != null && !bbinmobile_pwd.trim().equals("")){
			if(bbinmobile_pwd.length()!=8){
				renderText(JSONResult.failure("BBIN密码由8位数字或者字母组合组成!"));
			}else{
				PlatService.pwd(Plat.BBIN, customer.login_name, ip, bbinmobile_pwd);
			}
		}
		if(pt_pwd != null && !pt_pwd.trim().equals("")){
			if(pt_pwd.length()!=8){
				renderText(JSONResult.failure("PT密码由8位数字或者字母组合组成!"));
			}else{
				PlatService.pwd(Plat.PT, customer.login_name, ip, pt_pwd);
			}
		}
		if(mg_pwd != null && !mg_pwd.trim().equals("")){
			if(mg_pwd.length()!=8){
				renderText(JSONResult.failure("BBIN密码由8位数字或者字母组合组成!"));
			}else{
				PlatService.pwd(Plat.MG, customer.login_name, ip, mg_pwd);
			}
		}
		
		renderText(JSONResult.success("变更客户端密码成功!"));
	}
	
	
	public static void exportExcel(String sdate,String edate,String queryKey){
		Sqler sql =new Sqler("select * from mb_customer");
		Sqler cntsql =new Sqler("select count(1) from mb_customer");
//		if(!PageUtil.blank(queryKey)){
//			sql.and().left().like("login_name", queryKey);
//			cntsql.and().left().like("login_name", queryKey);
//			sql.or().like("real_name", queryKey);
//			cntsql.or().like("real_name", queryKey);
//			sql.or().like("qq", queryKey);
//			cntsql.or().like("qq", queryKey);
//			sql.or().like("ref_code", queryKey);
//			cntsql.or().like("ref_code", queryKey);
//			sql.or().like("tel", queryKey).right();
//			cntsql.or().like("tel", queryKey).right();
//		}
//		if(!(sdate==null||"".equals(sdate))){
//			Date date =DateUtil.stringToDate(sdate, "yyyy-MM-dd");
//			sql.and().ebigger("create_date",date);
//			cntsql.and().ebigger("create_date",date);
//		}
//		if(!(edate==null||"".equals(edate))){
//			Date date =DateUtil.stringToDate(edate, "yyyy-MM-dd");
//			sql.and().esmaller("create_date",date);
//			cntsql.and().esmaller("create_date",date);
//		}
		
		sql.and().ebigger("create_date",DateUtil.getLastWeek());
		cntsql.and().ebigger("create_date",DateUtil.getLastWeek());
		
		sql.orberByDesc("create_date");
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		if(count>50000){
			renderText("导出记录超过5万条，系统仅支持导出小于5万条记录,请缩短范围再导出。");
		}
		DictRender rd=new DictRender();
		try{
		List<Customer> custList=Sp.get().getDefaultJdbc().query(sql.getSql(),sql.getParams().toArray(new Object[0]),new CustomerRowMap());
		WorkbookSettings ws = new WorkbookSettings();
		ws.setLocale(new Locale("cn", "ZH"));
		WritableWorkbook workbook = Workbook.createWorkbook(response.out);
		WritableSheet sheet = workbook.createSheet("客户信息表格", 0);
		response.setHeader("content-disposition", "attachment; filename=customer_"+sdate+"_"+edate+".xls");
		response.setHeader("Content-Type", "application/excel");
        sheet.addCell(new Label(0, 0,"注册时间"));
        sheet.addCell(new Label(1, 0,"用户名"));
        sheet.addCell(new Label(2, 0,"真实姓名"));
        sheet.addCell(new Label(3, 0,"电话"));
        sheet.addCell(new Label(4, 0,"Email"));
        sheet.addCell(new Label(5, 0,"QQ"));
        sheet.addCell(new Label(6, 0,"客户等级"));
        sheet.addCell(new Label(7, 0,"客户状态"));
        sheet.addCell(new Label(8, 0,"积分"));
        sheet.addCell(new Label(9, 0,"最后登录时间"));
        sheet.addCell(new Label(10, 0,"注册IP"));
        sheet.addCell(new Label(11, 0,"注册地址"));
        sheet.addCell(new Label(12, 0,"登录ip"));
        sheet.addCell(new Label(13, 0,"登录地址"));
        sheet.addCell(new Label(14, 0,"代理用户名"));
//        sheet.addCell(new Label(15, 0,"推荐人"));
//        sheet.addCell(new Label(16, 0,"银行名称"));
//        sheet.addCell(new Label(17, 0,"账户号码"));
//        sheet.addCell(new Label(18, 0,"账户类别"));
//        sheet.addCell(new Label(19, 0,"开户网点"));
//        sheet.addCell(new Label(20, 0,"发送邮件"));
//        sheet.addCell(new Label(21, 0,"发送日期"));
//        sheet.addCell(new Label(22, 0,"创建人"));
    	IPSeeker seeker = IPSeeker.getInstance();
        for(int i=0;i<custList.size();i++){
        	Customer r=custList.get(i);
            sheet.addCell(new Label(0, i+1,DateUtil.dateToString(r.create_date, "yyyy-MM-dd HH:mm:ss")));
	        sheet.addCell(new Label(1, i+1,r.login_name));
	        sheet.addCell(new Label(2, i+1,r.real_name));
	        sheet.addCell(new Label(3, i+1,r.phone));
	        sheet.addCell(new Label(4, i+1,r.email));
	        sheet.addCell(new Label(5, i+1,r.qq));
	        sheet.addCell(new Label(6, i+1,rd.dictName("cust_level", r.cust_level)));
	        sheet.addCell(new Label(7, i+1,r.flag?"正常":"冻结"));
	        sheet.addCell(new Label(8, i+1,r.score.toString()));
	        sheet.addCell(new Label(9, i+1,DateUtil.dateToString(r.login_date, "yyyy-MM-dd HH:mm:ss")));
	        sheet.addCell(new Label(10, i+1,r.reg_ip));
	        sheet.addCell(new Label(11,i+1,seeker.getAddress(r.reg_ip==null?"":r.reg_ip)));
	    	sheet.addCell(new Label(12,i+1,r.login_ip));
	        sheet.addCell(new Label(13,i+1,seeker.getAddress(r.login_ip==null?"":r.login_ip)));
	        if(r.parent_id != null){
	        	Agent agent = Agent.NTgetAgent(r.parent_id);
	        	if(agent != null){
	        		sheet.addCell(new Label(14,i+1,agent.login_name));
	        	}else{
	        		sheet.addCell(new Label(14,i+1,""));
	        	}
	        }else{
	        	sheet.addCell(new Label(14,i+1,""));
	        }
	        
	   //   sheet.addCell(new Label(15,i+1,r.ref_code));
	       // sheet.addCell(new Label(16,i+1,r.bank_name));
	       // sheet.addCell(new Label(17,i+1,r.account));
	        //sheet.addCell(new Label(18,i+1,r.account_type));
	       // sheet.addCell(new Label(19,i+1,r.bank_dot));
	    //  sheet.addCell(new Label(19,i+1,r.s_email?"已发送":"未发送"));
	    //  sheet.addCell(new Label(21,i+1,DateUtil.dateToString(r.send_date, "yyyy-MM-dd HH:mm:ss")));
	        //sheet.addCell(new Label(22,i+1,r.create_user));
        }
        workbook.write();
	    workbook.close();
	    
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
