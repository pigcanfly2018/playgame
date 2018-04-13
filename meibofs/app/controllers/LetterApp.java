package controllers;

import java.util.Date;
import java.util.List;

import models.CashGift;
import models.Customer;
import models.Letter;
import models.LetterRowMap;
import models.Notice;
import play.mvc.Controller;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.MyRandom;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class LetterApp extends Controller{
	
	
	public static void getList(int start, int limit, int page, String sdate,
			String edate, String sort, String queryKey) throws Exception {
		Sqler sql = new Sqler("select * from mb_letter");
		Sqler cntsql = new Sqler("select count(1) from mb_letter");
		
		if(!PageUtil.blank(queryKey)){
			sql.and().left().equals("login_name", queryKey).right();
			cntsql.and().left().equals("login_name", queryKey).right();
		}
		
		if (!(sdate == null || "".equals(sdate))) {
			Date date = DateUtil.stringToDate(sdate, "yyyy-MM-dd");
			sql.and().ebigger("create_date", date);
			cntsql.and().ebigger("create_date", date);
		}
		if (!(edate == null || "".equals(edate))) {
			Date date = DateUtil.stringToDate(edate, "yyyy-MM-dd");
			sql.and().esmaller("create_date", date);
			cntsql.and().esmaller("create_date", date);
		}
		sql.orberByDesc("create_date");
		List<Letter> roleList = Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(), start, limit),sql.getParams().toArray(new Object[0]),new LetterRowMap());
		int count = Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p = new ExtPage();
		p.data = JSONResult.conver(roleList, true);
		p.total = count;
		renderJSON(p);
	}
	
	public static void saveLetter(Letter letter ){
		
		String letter_user=params.get("letter.user");
		String[] users=letter_user.split("[,]{1,1}");
		int count=0;
		if(letter.letter_code == null || letter.letter_code.equals("")){
			//System.out.println(111111);
			if("1".equals(params.get("kact"))){
				for(String u:users){
						Customer a=Customer.getCustomer(u);
						if(a!=null){
							Letter.NTcreat(a.cust_id,a.login_name, letter.title, letter.content, session.get(Constant.userName), letter.is_public);
							count++;
						}
				}
			}
			if("2".equals(params.get("kact"))){
				if(Letter.NTmodLetter(letter.title, letter.content, letter.is_public, letter.letter_id)){
					count++;
				}
			}
		}else{
			//System.out.println(22222);
			String code=MyRandom.getRandom(8);
			List<Customer> list = Customer.getCustomerListBylevel(letter.letter_code);
			for(Customer cust:list){
				
				Letter.NTcreatWithCode(cust.cust_id,cust.login_name, letter.title, letter.content, session.get(Constant.userName), letter.is_public,code);
				count++;
			}
		}
		
		renderText(JSONResult.success("处理完成!共计处理"+count+"条站内信。"));
	}
	
	
	public static void deleteLetter(Long idcode){
		if(idcode==null){
			renderText(JSONResult.failure("站内信删除失败!无效的操作!"));
		}
		if(Letter.NTdelLetter(idcode)){
			renderText(JSONResult.success("站内信删除成功!"));
		}else{
			renderText(JSONResult.failure("站内信删除失败!"));
		}
		
		
	}

	public static void batchDelete(String letter_code){
		if(letter_code == null || letter_code.equals("")){
			renderText(JSONResult.failure("批次号不应为空"));
		}else{
			int count=Letter.NTdeleteLetterBycode(letter_code);
			if(count>0){
				renderText(JSONResult.success("删除成功。共删除"+count+"条记录。"));
			}else{
				renderText(JSONResult.failure("删除失败。没有找到符合条件的记录。"));
			}
		}
		
	}
	


}
