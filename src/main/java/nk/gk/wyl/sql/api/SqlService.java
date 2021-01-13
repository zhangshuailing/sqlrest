package nk.gk.wyl.sql.api;


import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;
import java.util.Map;

/**
 * @Description: sql 语句的接口
 * @Author: zhangshuailing
 * @CreateDate: 2020/8/13 17:45
 * @UpdateUser: zhangshuailing
 * @UpdateDate: 2020/8/13 17:45
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public interface SqlService {
    /**
     * 插入单条数据
     *
     * @param table 表名
     * @param save  新增数据内容
     * @return String 数据编号
     * @throws Exception 异常
     */
    public String insert(SqlSessionTemplate sqlSessionTemplate, String table, Map<String, Object> save, String uid) throws Exception;

    /**
     * 指定编号（id）更新数据
     *
     * @param table  表名
     * @param update 更新数据内容
     * @param id     数据编号
     * @return String 数据编号
     * @throws Exception
     */
    public String update(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, Object> update, String id, String uid) throws Exception;


    /**
     * 新增或编辑，当 saveOrUpdate 存在id时更新，不存在时新增
     *
     * @param table        表名
     * @param saveOrUpdate 新增获取编辑的数据内容
     * @param check （必填，必须校验） 集合
     *              field 字段名称
     *              name 字段显示名称
     *              type 类型 string【字符串】 int【整型】  array【集合】
     * @return String 数据编号
     * @throws Exception
     */
    public String saveOrUpdate(SqlSessionTemplate sqlSessionTemplate,String table,
                               Map<String, Object> saveOrUpdate,
                               List<Map<String,Object>> check,
                               String uid) throws Exception;

    /**
     * 新增或编辑，当 saveOrUpdate 存在id时更新，不存在时新增
     *
     * @param table        表名
     * @param saveOrUpdate 新增获取编辑的数据内容
     * @return String 数据编号
     * @throws Exception
     */
    public String saveOrUpdate(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, Object> saveOrUpdate, String uid) throws Exception;

    /**
     * 根据查询条件更新数据
     *
     * @param table  表名
     * @param update 新增或编辑的数据内容
     * @param query  查询条件
     * @return 返回 true/false
     * @throws Exception
     */
    public boolean updateBatch(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, Object> update, Map<String, String> query, String uid) throws Exception;

    /**
     * 根据指定编号数据删除-物理删除
     *
     * @param table 表名
     * @param id    数据编号
     * @return 返回 true/false
     * @throws Exception
     */
    public boolean delete(SqlSessionTemplate sqlSessionTemplate,String table, String id, String uid) throws Exception;

    /**
     * 根据指定条件数据删除-物理删除
     *
     * @param table 表名
     * @param query 指定条件删除
     * @return 返回 true/false
     * @throws Exception
     */
    public boolean deleteBatch(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, String> query, String uid) throws Exception;


    /**
     * 根据指定编号数据删除-逻辑删除
     *
     * @param table 表名
     * @param id    数据编号
     * @return 返回 true/false
     * @throws Exception
     */
    public boolean deleteLogic(SqlSessionTemplate sqlSessionTemplate,String table, String id, String uid) throws Exception;

    /**
     * 根据指定编号数据集合删除-逻辑删除
     *
     * @param table  表名
     * @param field  字段名称
     * @param values 字段值集合
     * @param uid    用户编号
     * @return 返回 true / false
     * @throws Exception 异常信息
     */
    public boolean deleteLogicBatch(SqlSessionTemplate sqlSessionTemplate,String table, String field, List<String> values, String uid) throws Exception;

    /**
     * 根据指定条件数据删除-逻辑删除
     *
     * @param table 表名
     * @param query 指定条件删除
     * @return 返回 true/false
     * @throws Exception
     */
    public boolean deleteLogicBatch(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, String> query, String uid) throws Exception;

    /**
     * 根据指定编号查询数据
     *
     * @param table 表名
     * @param id    数据编号
     * @return Map<String   ,   Object> 返回map集合
     * @throws Exception
     */
    public Map<String, Object> get(SqlSessionTemplate sqlSessionTemplate,String table, String id, String[] fields) throws Exception;


    /**
     * 根据sql 获取对象
     *
     * @param t
     * @param sql
     * @return
     * @throws Exception
     */
    public <T> T get(SqlSessionTemplate sqlSessionTemplate,T t, String sql) throws Exception;

    /**
     * 根据查询条件获取列表（精确匹配）
     *
     * @param table 表名
     * @param query 精确查询条件
     * @return List<Map   <   String   ,   Object>> 返回集合
     * @throws Exception
     */
    public List<Map<String, Object>> findListExact(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, String> query, String[] fields) throws Exception;

    /**
     * 根据查询条件获取列表（in子句）
     *
     * @param table 表名
     * @param query in查询条件
     * @return List<Map   <   String   ,   Object>> 返回集合
     * @throws Exception
     */
    public List<Map<String, Object>> findListIn(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, List<String>> query, String[] fields) throws Exception;

    /**
     * 根据查询条件获取列表（模糊匹配）
     *
     * @param table 表名
     * @param query 模糊查询条件
     * @return List<Map   <   String   ,   Object>> 返回集合
     * @throws Exception
     */
    public List<Map<String, Object>> findListLike(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, String> query, String[] fields) throws Exception;

    /**
     * 根据查询条件获取列表
     *
     * @param table        表名
     * @param exact_search 精确查找
     * @param search       模糊匹配
     * @param in_search    in 子查询
     * @param order        排序
     * @return 返回数据集合
     * @throws Exception 异常信息
     */
    public List<Map<String, Object>> findList(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, String> exact_search, Map<String, String> search, Map<String, List<String>> in_search,
                                              Map<String, String> order, String[] fields) throws Exception;


    /**
     * 分页数据
     *
     * @param table        表名
     * @param pageNo       页码
     * @param pageSize     每页显示数据
     * @param exact_search 精确查询
     * @param search       模糊匹配
     * @param in_search    in子句查询
     * @param order        排序
     * @param fields       显示字段数组
     * @return 返回数据
     * @throws Exception 异常信息
     */
    public Map<String, Object> page(SqlSessionTemplate sqlSessionTemplate,String table,
                                    int pageNo,
                                    int pageSize,
                                    Map<String, String> exact_search,
                                    Map<String, String> search,
                                    Map<String, List<String>> in_search,
                                    Map<String, String> order,
                                    String[] fields) throws Exception;

    /**
     * 分页数据
     *
     * @param table  表名
     * @param map    组合参数
     * @param fields 显示字段数组
     * @return 返回数据
     * @throws Exception 异常信息
     */
    public Map<String, Object> page(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, Object> map, String[] fields) throws Exception;

    /**
     * 根据sql语句查询结果
     *
     * @param sql 查询的sql
     * @return 返回集合
     * @throws Exception 异常信息
     */
    public List<Map<String, Object>> findList(SqlSessionTemplate sqlSessionTemplate,String sql) throws Exception;

    /**
     * 新增或编辑
     *
     * @param map   参数
     * @param table 表名
     * @param uid   用户编号
     * @return 返回字符串
     * @throws Exception 抛出异常
     */
    public String saveOrUpdate(Map<String, Object> map,
                               SqlSessionTemplate sqlSessionTemplate,String table,
                               String uid) throws Exception;

    /**
     * 新增
     *
     * @param map   参数
     * @param table 表名
     * @param uid   用户编号
     * @return 返回字符串
     * @throws Exception 抛出异常
     */
    public String save(SqlSessionTemplate sqlSessionTemplate,String table,
                       Map<String, Object> map,
                       String id,
                       String uid) throws Exception;

    /**
     * 新增或编辑
     *
     * @param map   参数
     * @param check （必填，必须校验） 集合
     *              field 字段名称
     *              name 字段显示名称
     *              type 类型 string【字符串】 int【整型】  array【集合】
     * @param table 表名
     * @param uid   用户编号
     * @return 返回字符串
     * @throws Exception 抛出异常
     */
    public String saveOrUpdate(Map<String, Object> map,
                               List<Map<String,Object>> check,
                               SqlSessionTemplate sqlSessionTemplate,String table,
                               String uid) throws Exception;

    /**
     * 列表查询
     *
     * @param table 表名
     * @param map   参数
     * @return 返回集合
     * @throws Exception 抛出异常
     */
    public List<Map<String, Object>> findList(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, Object> map) throws Exception;

    /**
     * 分页列表
     *
     * @param table 表名
     * @param map   参数
     * @return 返回对象
     * @throws Exception 抛出异常
     */
    public Map<String, Object> page(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, Object> map) throws Exception;

    /**
     * 返回逻辑删除的状态
     * @param table 表名
     * @param id 数据编号
     * @param uid 用户编号
     * @throws Exception 异常信息
     */
    public void restoreDelete(SqlSessionTemplate sqlSessionTemplate,String table,String id,String uid) throws Exception;

    /**
     * 根据ids集合获取指定字段的集合
     * @param table 表名
     * @param ids id集合
     * @param show_field 显示的字段
     * @return 返回列表集合
     * @throws Exception 异常信息
     */
    public List<String> findList(SqlSessionTemplate sqlSessionTemplate,String table,List<String> ids,String show_field) throws Exception;

    /************************************统计************************************************/
    /**
     * 根据一个字段来统计数据
     * @param table 表名
     * @param field 字段值
     * @param order 排序
     * @return 返回统计信息
     * @throws Exception 异常信息
     */
    public List<Map<String, Object>> findGroupCount(SqlSessionTemplate sqlSessionTemplate,String table,String field,Map<String, String> order) throws Exception;

    /**
     * 根据多个字段来统计数据
     * @param table 表名
     * @param fields 字段值集合
     * @param order 排序
     * @return 返回统计信息
     * @throws Exception 异常信息
     */
    public List<Map<String, Object>> findGroupCount(SqlSessionTemplate sqlSessionTemplate,String table,String[] fields,Map<String,String> order) throws Exception;

    /**
     * 根据多个字段来统计数据
     * @param table 表名
     * @param fields 字段值集合
     * @param order 排序
     * @param size 前n条数据
     * @return 返回统计信息
     * @throws Exception 异常信息
     */
    public List<Map<String, Object>> findGroupCount(SqlSessionTemplate sqlSessionTemplate,String table,String[] fields,Map<String,String> order,int size) throws Exception;
    /************************************统计************************************************/
}
