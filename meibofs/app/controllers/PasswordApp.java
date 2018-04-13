package controllers;

import models.User;
import play.mvc.Controller;
import play.mvc.With;
import util.Constant;
import util.JSONResult;
@With(value = {AjaxSecure.class})
public class PasswordApp extends Controller{
	
	public static void modify(String org_password,String new_password,String re_password){
		if(!new_password.equals(re_password)){
			renderText(JSONResult.failure("两次输入的密码不匹配!"));
		}
		String user=session.get(Constant.userName);
		if(!User.NTtestPwd(user, org_password)){
			renderText(JSONResult.failure("原密码不匹配!"));
		}
		 boolean has1=new_password.matches(".*\\d+.*");
		 boolean has2=new_password.matches(".*[A-Za-z]+.*");
		 boolean has3=new_password.matches(".*[^(0-9a-zA-Z)]+.*");
         if(!(has1&&has2&&has3)){
    	   renderText(JSONResult.failure("新密码必须包含数字和字母和特殊字符!")); 
         }
         if(new_password.length()<8){
 			renderText(JSONResult.failure("密码最少为8位字母和数字组成!"));
 		 }
         if(User.NTmodPwd(user, new_password)){
            renderText(JSONResult.success("密码修改成功!"));
         }else{
        	 renderText(JSONResult.failure("密码修改失败!"));
         }
        
		
		
	}
}
