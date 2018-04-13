package controllers;

import java.util.Date;
import java.util.List;

import models.MessageBoard;
import models.MessageBoardRowMap;
import play.mvc.Controller;
import play.mvc.With;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;


public class MessageBoardApp extends Controller{
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_message_board");
		Sqler cntsql =new Sqler("select count(1) from mb_message_board");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("question", queryKey).right();
			cntsql.and().left().like("question", queryKey).right();
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
		
		List<MessageBoard> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new MessageBoardRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count;
		renderJSON(p);
	}
	
   public static void saveMsg(MessageBoard msg){	
	        if(msg.reply==null) msg.reply="";
	        if(msg.question==null) msg.question="";
	        if(msg.cust_name==null) msg.cust_name="";
	   	    String show_date=params.get("msg.show_date");
	   	    msg.show_date=new Date(DateUtil.stringToDate(show_date, "yyyy-MM-dd HH:mm:ss").getTime()); 
	   	    if(msg.show_date==null){
	   	    	renderText(JSONResult.failure("显示时间不可为空!"));
	   	    }
	        if("".equals(msg.reply)){
	        	renderText(JSONResult.failure("回复最少为8字符!"));
	        }
	        if("".equals(msg.question)){
	        	renderText(JSONResult.failure("请输入有效留言!"));
	        }
	        if("".equals(msg.cust_name)){
	        	renderText(JSONResult.failure("请输入客户名字!"));
	        }
	        if(msg.reply.length()<8){
				renderText(JSONResult.failure("回复最少为8字符!"));
			}
	        if(msg.question.length()<6){
				renderText(JSONResult.failure("回复最少为6字符!"));
			}
	        
			if("1".equals(params.get("kact"))){
				msg.create_user=session.get(Constant.userName);
				msg.reply_user=session.get(Constant.userName);
				if(msg.NTcreat()){
					renderText(JSONResult.success("留言创建成功!"));
				}else{
					renderText(JSONResult.failure("留言创建失败!"));
				}
			}
			
			if("2".equals(params.get("kact"))){
				msg.reply_user=session.get(Constant.userName);
				if(msg.NTupdate()){
					renderText(JSONResult.success("留言修改成功!"));
				}else{
					renderText(JSONResult.failure("留言修改失败!"));
				}
			}
		
	}
   
   public static void deleteMsg(Long idcode){
	   if(idcode==null){
			renderText(JSONResult.failure("无效的操作!"));
		}
	   if(MessageBoard.NTdelete(idcode)){
		   renderText(JSONResult.success("删除留言成功!"));
	   }else{
		   renderText(JSONResult.failure("删除留言失败!"));
	   } 
   }

}
