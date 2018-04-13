package bsz.exch.test.db;

/**
 * SQL操作元
 * @author robin
 *
 */
public class SqlField {
	
	//bean字段
	public String name;
	
	//操作符
	public String op;
	
	//值
	public Object[] values;
	
	//类型
	public int type;
	
	//连接字段字符(查询中使用)
	public String link;
	

	//Query
    public final static int Query=1;
	//更新段
	public final static int UPDATE=2;
	//条件段
	public final static int WHERE=3;
	//排序段
	public final static int ORDER=4;
	//主键段
	public final static int PK=5;
	
	public final static int NONE=0;
	
	
	public SqlField(String name,String op,Object[]values,int type){
		this.name=name;
		this.op=op;
		this.values=values;
		this.type=type;
		
	}
	public SqlField link(String link){
		this.link=link;
		return this;
	}
	
}
