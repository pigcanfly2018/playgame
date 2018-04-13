package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import play.mvc.Controller;
import play.mvc.With;
import play.mvc.Http.Header;
import util.Constant;
import util.Meta;
import util.Sp;

import com.mchange.v2.c3p0.ComboPooledDataSource;


@With(value = {AjaxSecure.class})
public class Application extends Controller {
	
    public static void index() {
    	String username=session.get(Constant.userName);
        render(username);
    }
    
    public static void gm(String t)throws Exception{
    	 String sql="select * from "+t;
    	 ComboPooledDataSource ds=(ComboPooledDataSource)Sp.get().getBean("dataSource");
    	 Connection  c= ds.getConnection();
    	 PreparedStatement ps= c.prepareStatement(sql);
    	 ResultSet rs=ps.executeQuery();
    	 ResultSetMetaData rsmd=rs.getMetaData();
    	 int count=rsmd.getColumnCount();
    	 List<Meta>  metaList=new ArrayList<Meta>();
    	 for(int i=0;i<count;i++){
    		 Meta meta =new Meta();
    		 meta.name=rsmd.getColumnName(i+1).toLowerCase();
    		 String type=rsmd.getColumnTypeName(i+1);
    		 System.out.println(type);
    		 if("BIGINT".equals(type)){
    			 meta.type="Long";
    		 }else if("DATETIME".equals(type)){
    			 meta.type="Date";
    		 }else if("INT".equals(type)){
    			 meta.type="Integer";
    		 }else if("TINYINT".equals(type)){
    			 meta.type="Boolean";
    		 }else if("DECIMAL".equals(type)){
    			 meta.type="BigDecimal";
    		 }else{
    			 meta.type="String";
    		 }
    		 metaList.add(meta);
    	 }
    	render(metaList,t);
    }
    
    
}