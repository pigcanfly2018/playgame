package util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
/**
 * spring实例
 * @author jb.lance 2013-9-3
 */
public class Sp {
	private Sp(){	
		if(ctx==null){
		       ctx= new ClassPathXmlApplicationContext("core-context.xml");
		}
	}
	private static Sp sp =new Sp();
	private ApplicationContext ctx;
	public static Sp get(){
		return sp;
	}
	/**
	 * get bean
	 * @param id
	 * @return
	 */
	public  Object getBean(String id){
		return ctx.getBean(id);
	}
	/**
	 * get config value
	 * @param configId
	 * @param key
	 * @return
	 */
	public String getCv(String configId,String key){
		MyProperty property=(MyProperty)ctx.getBean(configId);
		if(property!=null){
		  return (String)property.getContextProperty(key);
		}
		return null;
	}
	/**
	 * get default jdbc
	 * @return
	 */
	public JdbcTemplate getDefaultJdbc(){
		return (JdbcTemplate)Sp.get().getBean("jbdb");
	}
	
	public static void main(String []args){
		//Md5PasswordEncoder passwordEncoder=(Md5PasswordEncoder)Sp.get().getBean("passwordEncoder");
		//System.out.println(passwordEncoder.encodePassword("61959687", null));
	}

}
