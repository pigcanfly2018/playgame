package bsz.exch.core;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import bsz.exch.bean.Errors;
import bsz.exch.bean.FobbinInfo;
import bsz.exch.bean.LogInfo;
import bsz.exch.bean.Result;
import bsz.exch.bean.Task;
import bsz.exch.cache.ConfigCache;
import bsz.exch.cache.RoleCache;
 
 @WebService(endpointInterface="bsz.exch.core.SoapService")
 public class SoapServiceImpl  implements SoapService{
	 
  public static final ThreadLocal<LogInfo>  LOCAL_LOG  = new ThreadLocal<LogInfo>();
	 
  @Resource
  WebServiceContext context;
	 
   private static final Logger logger = LoggerFactory.getLogger(SoapServiceImpl.class);
   
   
   public String getClientIp() {
	   WrappedMessageContext  ctx = (WrappedMessageContext) context.getMessageContext();
	   HttpServletRequest request = (HttpServletRequest)
	   ctx.get(AbstractHTTPDestination.HTTP_REQUEST);
	   String ip=request.getRemoteAddr();
	   return ip; 
   }
    
   
   private Result doPerform(String paramString){

	   Task task=Task.from(paramString);
		if(task==null){
			return Result.createError("", "", Errors.Resolve_1005,"Requst Xml Resolve Exception!");
		}
		InterFace inter = ConfigCache.instance().getInterface(task.getFunId());
		if(inter==null){
			return Result.createError(task.getId(),task.getFunId(),Errors.ServiceError_1003,"Can't find the interface id is "+task.getFunId());
		}
		//----校验权限开始----
		String role=RoleCache.instance().getRole(task.getUser(), task.getPwd()); 	
		if(role==null)
			return Result.createError(task.getId(),task.getFunId(), Errors.NoAuthorization_1004, "No Authorization");
		
		if(!inter.haveRole(role)){
			return Result.createError(task.getId(),task.getFunId(), Errors.NoAuthorization_1004, "No Authorization");
		}
		//----校验权限结束----
		
		HandlerInfo handlerInfo = HandlerFactory.factory(inter.getType());
		if(handlerInfo==null){
			return Result.createError(task.getId(),task.getFunId(),Errors.ServiceError_1003,"Can't find the Handler!");
		}
		Method m=handlerInfo.getService(inter.getService());
		if(m==null){
			return Result.createError(task.getId(),task.getFunId(),Errors.NullResult_1006,"Nothing to do!");
		}else{
			try {
				//处理前置方法
				Method mb=handlerInfo.getBefore();
				if(mb!=null){
					FobbinInfo b=(FobbinInfo)mb.invoke(handlerInfo.getHandler(), task,inter);
					if(b.fobbin)
						return Result.createError(task.getId(), task.getFunId(), Errors.ValidateParams_1001, b.getMsg());
				}
				//处理前置验证
				Params params=m.getDeclaredAnnotation(Params.class);
				if(params!=null&&params.validateField()!=null){
					String[] fields =params.validateField();
					for(String field:fields){
						if(task.ifNullOrBlankParam(field)){
							return Result.createError(task.getId(), task.getFunId(), Errors.ValidateParams_1001, field+" must requested!");
						}
					}
				}
				//处理service
				Result result =null;
				//判断是否需要事物处理
				Tx tx=m.getAnnotation(Tx.class);
				if(tx!=null){
					BasicDataSource ds=(BasicDataSource)WsCxtHelper.getBean(inter.getDataSource());
					if(ds==null){
						return Result.createError(task.getId(), task.getFunId(), Errors.ValidateParams_1001, "Can't find the datasource!");
					}
					DefaultTransactionDefinition tf = new DefaultTransactionDefinition();  
					PlatformTransactionManager tm = new DataSourceTransactionManager(ds);
				    TransactionStatus ts = tm.getTransaction(tf);
				    try {  
				    	result = (Result)m.invoke(handlerInfo.getHandler(), task,inter);
			            tm.commit(ts);  
			        } catch (Exception e) {  
			            tm.rollback(ts);  
			            e.printStackTrace();
			            return Result.createError(task.getId(), task.getFunId(), Errors.ServiceError_1003, "Service Error:"+e.getMessage());
			        }  	
				}else{
					result = (Result)m.invoke(handlerInfo.getHandler(), task,inter);
					if(result==null){
						return Result.createError(task.getId(),task.getFunId(),Errors.NullResult_1006,"the Result is Null!");
					}
				}
				
				//处理后置方法
				Method ma=handlerInfo.getAfter();
				if(ma!=null){
					ma.invoke(handlerInfo.getHandler(),task,inter);
				}
				return result;
			} catch (Exception e) {
				LOCAL_LOG.get().addLog("|Exception :{"+e.getMessage()+"}");
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw, true));
				String str = sw.toString();
				logger.info(str);  
				return Result.createError(task.getId(),task.getFunId(),Errors.NullResult_1006,"try invoke the service "+inter.getService()+" occur error!");
			} 
		}
   }
	@Override
	public String perform(String paramString) {
		LOCAL_LOG.set(new LogInfo());
		LOCAL_LOG.get().addLog("|Soa Request:{"+paramString+"}");
		Result result=doPerform(paramString);
		LOCAL_LOG.get().addLog("|Soa Response["+result.success()+"]:"+result.toXml());
		logger.info(LOCAL_LOG.get().getLog());
		String resultString =result.toXml();
		return resultString;
	}


	
	
}
