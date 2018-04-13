package controllers;

import java.util.List;

import models.Msg;
import models.UserMsg;
import play.mvc.Controller;
import play.mvc.With;
import util.Constant;
import util.DateUtil;
import util.JSONResult;

@With(value = {AjaxSecure.class })
public class MsgApp extends Controller{
	
	public static void getMyMsg(){
	   String user=session.get(Constant.userName);
	   List<Msg> msgs=Msg.NTquerMsgByUser(user);
	   UserMsg.NTdo(user);
	   String message="";
	   if(msgs.size()>0){
		   message="您共有"+msgs.size()+"条提醒信息<br/>"; 
		   for(int i=0;i<msgs.size();i++){
		     message=message+""+DateUtil.dateToString(msgs.get(i).create_date, "yyyy-MM-dd HH:mm")+" "+msgs.get(i).content+"<br/>";
		     if(i>3)break;
		   }
		   renderText(JSONResult.success(message));
	   }else{
		   renderText(JSONResult.failure(message));
	   }
	  
	}

}
