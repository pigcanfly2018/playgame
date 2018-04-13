package bsz.exch.test.db;

import java.util.List;

/**
 * jdbc操作dao
 */
public interface IJdbcDao {

    /**
     * 插入一条记录 自动处理主键
     *
     * @param entity
     * @return
     */
    public Long insert(Object entity);

    /**
     * 插入一条记录 自动处理主键
     *
     * @param criteria the criteria
     * @return long long
     */
    public Long insert(EntConfig entConfig);

    
    
    public <T> T queryById(Class<T> c,Long id);
}
