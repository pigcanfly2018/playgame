package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.jdbc.core.PreparedStatementCreator;

public class MyPreparedStatementCreator implements PreparedStatementCreator{
	
	public String sql;
	
	public Object[] objects;
	
	public MyPreparedStatementCreator(String sql,Object[] objects){
		this.sql=sql;
		this.objects=objects;
	}

	
	public PreparedStatement createPreparedStatement(Connection conn)
			throws SQLException {
		 PreparedStatement ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
         for(int i=0;i<objects.length;i++){
         	ps.setObject(i+1, objects[i]);
         }
         return ps;
	}

}
