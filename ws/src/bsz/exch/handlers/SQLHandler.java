package bsz.exch.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import bsz.exch.bean.FobbinInfo;
import bsz.exch.bean.Result;
import bsz.exch.bean.Task;
import bsz.exch.core.Before;
import bsz.exch.core.Handler;
import bsz.exch.core.InterFace;
import bsz.exch.core.JdbcResource;
import bsz.exch.core.MyPreparedStatementCreator;
import bsz.exch.core.SQLInfo;
import bsz.exch.core.SQLProCache;
import bsz.exch.core.Service;
import bsz.exch.core.Tx;
import bsz.exch.utils.LogHelper;


@Handler(name="SQL")
public class SQLHandler{
	
	private static Logger logger=Logger.getLogger(SQLHandler.class);
	
	private boolean isSelect(String sql){
		if(sql.toLowerCase().startsWith("select")){
			return true;
		}else{
			return false;
		}
	}

	private boolean isInsert(String sql){
		if(sql.toLowerCase().startsWith("insert")){
			return true;
		}else{
			return false;
		}
	}

	private boolean isUpdate(String sql){
		if(sql.toLowerCase().startsWith("update")||sql.toLowerCase().startsWith("delete")){
			return true;
		}else{
			return false;
		}
	}
	
	@Before
	public FobbinInfo before(Task task, InterFace inter){
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		if(template==null){
			return new FobbinInfo(true,"the datasource is null!");
		}
		return new FobbinInfo(false,"");
	}
	
	//屏蔽真实姓名，电话号码，密码，QQ，银行卡好，银行卡开户网点
	private String fitter(Object obj,String param){
		String objStr=obj.toString();
		StringBuffer sb=new StringBuffer("");
		if("real_name".equals(param)){
			if(objStr.length()>1){
				sb.append(objStr.substring(0, 1));
				for(int i=0;i<objStr.length()-1;i++){
					sb.append("*");
				}
			}else{
				sb.append(objStr);
			}
		   return sb.toString();
		}
		
		if("phone".equals(param)){
			if(objStr.length()>=10){
				sb.append(objStr.substring(0, 3));
				sb.append("****");
				sb.append(objStr.substring(objStr.length()-4, objStr.length()));
			}else{
				sb.append(objStr);
			}
			 return sb.toString();
		}
		
		if("password".equals(param)||"login_pwd".equals(param)){
			sb.append("******");
		}
		
		if("qq".equals(param)){
			if(objStr.length()>=6){
				sb.append(objStr.substring(0, 2));
				sb.append("****");
				sb.append(objStr.substring(objStr.length()-3, objStr.length()));
			  
			}else{
				sb.append(objStr);
			}
			 return sb.toString();
		}
		
//		if("account".equals(param)){
//			if(objStr.length()>=6){
//				sb.append(objStr.substring(0, 2));
//				sb.append("****");
//				sb.append(objStr.substring(objStr.length()-3, objStr.length()));
//			  
//			}else{
//				sb.append(objStr);
//			}
//			 return sb.toString();
//		}
//		if("bank_dot".equals(param)){
//			if(objStr.length()>=1){
//				sb.append(objStr.substring(0, 1));
//				sb.append("******");
//			}else{
//				sb.append(objStr);
//			}
//			 return sb.toString();
//		}
		return objStr;
	}
	

	@Service(name="query")
	public Result query(Task task, InterFace inter){
		
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		SQLProCache cache =SQLProCache.instance();
		SQLInfo info=cache.addSqlInfo(inter);
		List<String> sqlParams=info.getParams();
		List<String> vList=new ArrayList<String>();
		for(String param:sqlParams){
			String v=task.getParam(param);
			if(v==null){
				return Result.createError(task.getId(), task.getFunId(), "1009", "Requried param "+ param+" Not Found!") ;
			}
			vList.add(v);
		}
		if(isSelect(info.getSql())){
		    String sql=info.getSql();
		    //是否需要分页
		    List<String> list0= new ArrayList<String>();
		    for(String s:vList){
		    	list0.add(s);
		    }
			if(task.isActivePage()){
				sql=sql+" limit "+task.getLimit()+" offset "+task.getStart();
			}
			
			//记录日志
			logger.info(LogHelper.dbLog(inter.getDataSource(),sql,list0));
			
			//开始执行
			List<Map<String,Object>>list=(List<Map<String,Object>>)template.queryForList(sql,list0.toArray(new String[0]));
			Result result=Result.create(task.getId(), task.getFunId());
			List<String> replyList= inter.getReplyEn();
			result.addFields(inter.getReplyEn());
			for(Map<String,Object> map:list){
				List<String> t=new ArrayList<String>();
				for(String r:replyList){
					if(map.get(r)!=null){
					   t.add(fitter(map.get(r).toString(),r));
					}else{
						t.add(null);
					}
				}
				result.addRecord(t);
			}
			result.setLength(list.size());
			
			//是否需要统计总数
			if(task.isCount()){
				sql="select count(1) from ("+info.getSql()+") tmp";
				logger.info(LogHelper.dbLog(inter.getDataSource(),sql,list0));
				int count=template.queryForObject(sql, list0.toArray(new String[0]),Integer.class);
				result.setPage(task.getStart(), task.getLimit(), count);
			}
			result.setFlag("-1");
			result.setIsList(true);
			return result;
		}
		return Result.createError(task.getId(), task.getFunId(), "1009", "No Support Statemenet!");
	}
	
	
	@Service(name="update")
	@Tx
	public Result update(Task task, InterFace inter){
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		SQLProCache cache =SQLProCache.instance();
		SQLInfo info=cache.addSqlInfo(inter);
		List<String> sqlParams=info.getParams();
		List<String> vList=new ArrayList<String>();
		for(String param:sqlParams){
			String v=task.getParam(param);
			if(v==null){
				return Result.createError(task.getId(), task.getFunId(), "1009", "Requried param "+ param+" Not Found!") ;
			}
			vList.add(v);
		}
		//处理插入语句
		if(isInsert(info.getSql())){
			logger.info(LogHelper.dbLog(inter.getDataSource(),info.getSql(),vList));
		    KeyHolder keyHolder = new GeneratedKeyHolder();
		    int cnt=template.update(new MyPreparedStatementCreator(info.getSql(),vList.toArray(new String[0])), keyHolder);
			Result result=Result.create(task.getId(), task.getFunId());
			result.setFlag("-1");
			result.setIsList(false);
			result.setRes(keyHolder.getKey().longValue()+"");
			return result;
		}
		//处理更新语句
		if(isUpdate(info.getSql())){
			logger.info(LogHelper.dbLog(inter.getDataSource(),info.getSql(),vList));
			int cnt=template.update(info.getSql(),vList.toArray(new String[0]));
			Result result=Result.create(task.getId(), task.getFunId());
			result.setFlag("-1");
			result.setIsList(false);
			result.setRes(""+cnt);
			return result;
		}
		return Result.createError(task.getId(), task.getFunId(), "1009", "No Support Statemenet!");
		
	}
	

}
