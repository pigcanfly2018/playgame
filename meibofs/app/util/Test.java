package util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.io.FileUtils;

public class Test {
	

	String code;
	
	String pwd_old;
	
	String online_messenger;
	
	String game_key;
	
	String product_id;
	
	String last_updated_by;
	 
	String   last_update;
	
	String sex;
	
	String remarks;
	
	String real_name;
	
	String pwd_expiry_date;
	
    String pwd;
     
	String phone;
	
	String  parent_id;
	
	String login_times;
    
	String login_name;
	
	String last_login_ip;
	
	String last_login_date;
	
	String key;
	
	String ip_address;
	
	String flag;
	
	String expiry_date;
	
	String email;
	
	String effectivity_date;
    
	String customer_type;
    
	String customer_id;
    
    String currency;
    
    String credit;
    
    String created_date;
	
	String created_by;
	
	String birth_date;
	
    String before_login_ip;
    
    String before_login_date;
    
    String address;
    
    String backup6;
    
    String backup5;
    
    String backup4;
    
    String backup3;
    
    String backup2;
    
    String backup1;
    
    String reserve3;
    
    String reserve2;
    
    String reserve1;
   
    String integral; 
    
    String rn;
    
    public static String des(String test,String key){
    	
    	try{
    	PHPDESEncrypt dd2 = new PHPDESEncrypt("A02", key);
    	if(test==null||"null".equals(test)){
    		return "";
    	}
    	return dd2.decrypt(test);
    	}catch(Exception e){
    		
    	}
    	return "";
    	
    }
    
	public static void main(String[] args)throws Exception {
		
		System.out.println(99/100.0);
		
		/*
		try{
		    IPSeeker seeker = IPSeeker.getInstance();
	
			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(new Locale("cn", "ZH"));
			WritableWorkbook workbook = Workbook.createWorkbook(new File("e:/客户资料1.xls"));
			WritableSheet sheet = workbook.createSheet("客户信息表", 0);
	        sheet.addCell(new Label(0, 0,"注册时间"));
	        sheet.addCell(new Label(1, 0,"用户名"));
	        sheet.addCell(new Label(2, 0,"真实姓名"));
	        sheet.addCell(new Label(3, 0,"电话"));
	        sheet.addCell(new Label(4, 0,"邮箱"));
	        sheet.addCell(new Label(5, 0,"QQ"));
	        sheet.addCell(new Label(6, 0,"地址"));
	        sheet.addCell(new Label(7, 0,"余额"));
	        sheet.addCell(new Label(8, 0,"登陆次数"));
	        sheet.addCell(new Label(9, 0,"最后登陆"));
	        sheet.addCell(new Label(10, 0,"IP地址"));
	        
	        sheet.addCell(new Label(11, 0,"备注1"));
	        sheet.addCell(new Label(12, 0,"备注2"));
	        sheet.addCell(new Label(13, 0,"备注3"));
	        sheet.addCell(new Label(14, 0,"备注4"));
	        sheet.addCell(new Label(15, 0,"备注5"));
	        
			String txt=FileUtils.readFileToString(new File("f:/libo.log"));
			String[] lines=txt.split("\n");
			List<Test> testList=new ArrayList<Test>();
			
	        for(int i=0;i<lines.length;i++){
	        	String line=lines[i];
	        	String[] params= line.split(",");
				System.out.println(params.length);
				if(params.length!=46){
					continue;
				}
				Test test =new Test();
				//ADDRESS,BEFORE_LOGIN_DATE,BEFORE_LOGIN_IP,BIRTH_DATE,CREATED_BY,
				test.address=params[0];
				test.before_login_date=params[1];
				test.before_login_ip=params[2];
				test.birth_date=params[3];
				test.created_by=params[4];
				//CREATED_DATE,CREDIT,CURRENCY,CUSTOMER_ID,CUSTOMER_TYPE,
				test.created_date=params[5];
				test.credit=params[6];
				test.currency=params[7];
				test.customer_id=params[8];
				test.customer_type=params[9];
				
				//EFFECTIVITY_DATE,EMAIL,EXPIRY_DATE,FLAG,IP_ADDRESS,
				test.effectivity_date=params[10];
				test.email=params[11];
				test.expiry_date=params[12];
				test.flag=params[13];
				test.ip_address=params[14];
				
				//key,LAST_LOGIN_DATE,LAST_LOGIN_IP,LOGIN_NAME,LOGIN_TIMES,
				test.key=params[15];
				test.last_login_date=params[16];
				test.last_login_ip=params[17];
				test.login_name=params[18];
				test.login_times=params[19];
				
				//PARENT_ID,PHONE,PWD,PWD_EXPIRY_DATE,REAL_NAME,
				test.parent_id=params[20];
				test.phone=params[21];
				test.pwd=params[22];
				test.pwd_expiry_date=params[23];
				test.real_name=params[24];
				
				//REMARKS,SEX,LAST_UPDATE,LAST_UPDATED_BY,PRODUCT_ID,
				test.remarks=params[25];
				test.sex=params[26];
				test.last_update=params[27];
				test.last_updated_by=params[28];
				test.product_id=params[29];
				
				//GAME_KEY,ONLINE_MESSENGER,PWD_OLD,CODE,INTEGRAL,
				test.game_key=params[30];
				test.online_messenger=params[31];
				test.pwd_old=params[32];
				test.code=params[33];
				test.integral=params[34];
				
				//RESERVE1,RESERVE2,RESERVE3,BACKUP1,BACKUP2,
				test.reserve1=params[35];
				test.reserve2=params[36];
				test.reserve3=params[37];
				test.backup1=params[38];
				test.backup2=params[39];
				
				//BACKUP3,BACKUP4,BACKUP5,BACKUP6,RN,
				test.backup3=params[40];
				test.backup4=params[41];
				test.backup5=params[42];
				test.backup6=params[43];
				test.rn=params[44];
				if(test.login_name==null||test.login_name.equals("null")||
						test.login_times.equals("1")){
					continue;
				}
				testList.add(test);
	        }
	        
	        for(int i=0;i<testList.size();i++){
 
	        	  Test test =testList.get(i);
				  sheet.addCell(new Label(0, i+1,test.created_date));
	              sheet.addCell(new Label(1, i+1,test.login_name));
	              
	              sheet.addCell(new Label(2, i+1,des(test.real_name,"01")));

	              sheet.addCell(new Label(3, i+1,des(test.phone,"03")));
	  
	              sheet.addCell(new Label(4, i+1,des(test.email,"04")));
	          
	              sheet.addCell(new Label(5, i+1,des(test.online_messenger,"05")));
	              
	              sheet.addCell(new Label(6, i+1,des(test.address,"02")));
	              
	              sheet.addCell(new Label(7, i+1,test.credit));
	              sheet.addCell(new Label(8, i+1,test.login_times));
	              sheet.addCell(new Label(9, i+1,test.last_login_date));
	              sheet.addCell(new Label(10,i+1,seeker.getAddress(test.ip_address==null?"":test.ip_address)));
	              
	              sheet.addCell(new Label(11, i+1,test.backup1));
	              sheet.addCell(new Label(12, i+1,test.backup2));
	              sheet.addCell(new Label(13, i+1,test.backup3));
	              sheet.addCell(new Label(14, i+1,test.backup4));
	              sheet.addCell(new Label(15, i+1,test.backup5));
	        }

	        workbook.write();
		    workbook.close();
			}catch(Exception e){
				e.printStackTrace();
			}*/
		/**
		 * ADDRESS,BEFORE_LOGIN_DATE,BEFORE_LOGIN_IP,BIRTH_DATE,CREATED_BY,CREATED_DATE,
		 * CREDIT,CURRENCY,CUSTOMER_ID,CUSTOMER_TYPE,EFFECTIVITY_DATE,EMAIL,
		 * EXPIRY_DATE,FLAG,IP_ADDRESS,key,LAST_LOGIN_DATE,LAST_LOGIN_IP,LOGIN_NAME,
		 * LOGIN_TIMES,PARENT_ID,PHONE,PWD,PWD_EXPIRY_DATE,
		 * REAL_NAME,REMARKS,SEX,LAST_UPDATE,LAST_UPDATED_BY,
		 * PRODUCT_ID,GAME_KEY,ONLINE_MESSENGER,PWD_OLD,CODE,INTEGRAL,RESERVE1,RESERVE2,RESERVE3,
		 * BACKUP1,BACKUP2,BACKUP3,BACKUP4,BACKUP5,BACKUP6,RN,
           null,2011-04-06,221.210.254.205,null,MIGRATION_CUSTOMER,2011-03-23,0,CNY,1000063202,1,2011-11-26,null,3000-12-31,1,127.0.0.1,null,2011-04-21,218.7.127.190,cp50333,53,1000062764,0Ai8XkpVMFtDA+hJAb1dJTAJw==0fon,8499c339ac637201f7e3007132ffcd1f,2012-06-19,4XUnT74HonBqfU/I7gQb9eBRA==4GJY,null,M,null,null,A02,3uso70f7,null,a33c2a5ae6cb8c2ed3e5473a9cceeb8e,null,0,加密,null,null,null,null,程宏升,13359950333,null,null,101,
		  
		 *  01.A02.KEY=g66Eq5e3gts667Dr
			02.A02.KEY=fdF0MmewtLfxqATK
			03.A02.KEY=eTl3T9x8ScJ6IH79
			04.A02.KEY=yeAA5bqscl5gYWF1
			05.A02.KEY=hmkSmOHYaReunmro
			06.A02.KEY=rasRdjpsQl3G5p8V
			07.A02.KEY=tuuTkhnmlit697Ko
			08.A02.KEY=eIyZo36jKEj7TJC9
		 */
		
		/**
		 * 6iJoXIZJkgnzO3QHyybEVnTFg==6aTk
		   3qDtNWkaRFPbc488dzXhxlOrA==3OTI
		   7WR/Z7n11JD62/QHyybEVnTFg==7xK1
		 */
		

	}
	


}
