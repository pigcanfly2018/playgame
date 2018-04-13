package bsz.exch.test.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;



public class JdbcDao implements IJdbcDao {

	private static final Logger logger = LoggerFactory.getLogger(JdbcDao.class);
	   
    protected JdbcOperations jdbcTemplate;
   
    public void setJdbcTemplate(JdbcOperations jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public static NameDb nameHandler=new NameDb();
    
	
	@Override
	public Long insert(Object entity) {
		 final SqlBag sbag =SqlBuild.insert(EntConfig.from(entity));
		  logger.info("excute sql:["+sbag.sql+"] params:["+sbag.params+"]");
		  KeyHolder keyHolder = new GeneratedKeyHolder();
	      int result=jdbcTemplate.update(new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	                PreparedStatement ps = con.prepareStatement(sbag.sql,
	                    new String[] {sbag.pk});
	                int index = 0;
	                for (Object param :sbag.params) {
	                    index++;
	                    ps.setObject(index, param);
	                }
	                return ps;
	            }
	        }, keyHolder);
	       logger.info("excute sql:["+sbag.sql+"] params:["+sbag.params+"] result:"+result);
	       return keyHolder.getKey().longValue();
	}

	@Override
	public Long insert(EntConfig entConfig) {
	
		return null;
	}

	@Override
	public <T> T queryById(Class <T>c,Long id) {
		EntConfig entConfig =EntConfig.create(c);
		entConfig=entConfig.whereId(id);
		SqlBag sbag =SqlBuild.query(entConfig);
		System.out.println(sbag.params.length);
		List<T> list = jdbcTemplate.query(sbag.sql,sbag.params,new MyRowMapper<T>(c));
		if(list.size()==0){
			return null;
		}
		return list.get(0);
	}

	

}
