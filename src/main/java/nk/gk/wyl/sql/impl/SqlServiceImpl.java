package nk.gk.wyl.sql.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import nk.gk.wyl.sql.api.SqlService;
import nk.gk.wyl.sql.mapper.SqlMapper;
import nk.gk.wyl.sql.util.CommonValidator;
import nk.gk.wyl.sql.util.QueryUtil;
import nk.gk.wyl.sql.util.SqlUtil;
import nk.gk.wyl.sql.util.Util;
import nk.gk.wyl.sql.util.sql.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 接口实现类
 * @Author: zhangshuailing
 * @CreateDate: 2020/8/13 17:48
 * @UpdateUser: zhangshuailing
 * @UpdateDate: 2020/8/13 17:48
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Service
@Component
public class SqlServiceImpl implements SqlService {
    // mapper类地址
    private String sqlMapping;

    @Autowired
    public void setMapper(SqlMapper t) {
        sqlMapping = t.getClass().getInterfaces()[0].getName() + ".";
    }

    /**
     * 插入单条数据
     *
     * @param table 表名
     * @param save  参数
     * @return String 数据编号
     * @throws Exception 异常
     */
    @Override
    public String insert(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, Object> save, String uid) throws Exception {
        // 生成流水号
        String id = Util.getResourceId();
        save.put("id", id);
        // 添加时增加用户名和创建时间
        SqlUtil.addMap(save, uid);
        Map<String, Object> sql_map = SqlUtil.sqlMap(InsertSql.sql(save, table));
        int result = 0;
        try {
            result = sqlSessionTemplate.insert(sqlMapping + "insertSql", sql_map);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        if (result > 0) {
            return id;
        } else {
            return "";
        }
    }

    /**
     * 指定编号（id）更新数据
     *
     * @param table  表名
     * @param update 更新数据内容
     * @param id     数据编号
     * @return String 数据编号
     * @throws Exception
     */
    @Override
    public String update(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, Object> update, String id, String uid) throws Exception {
        // 编辑时增加用户名和编辑时间
        SqlUtil.editMap(update, uid);
        Map<String, Object> sql_map = SqlUtil.sqlMap(UpdateSql.sql(update, table, id));
        boolean bl = update(sqlSessionTemplate,sql_map);
        if (bl) {
            return id;
        }
        return "";
    }

    /**
     * 新增或编辑，当 saveOrUpdate 存在id时更新，不存在时新增
     *
     * @param table        表名
     * @param saveOrUpdate 新增获取编辑的数据内容
     * @param check （必填，必须校验） 集合
     *              field 字段名称
     *              name 字段显示名称
     *              type 类型 string【字符串】 int【整型】  array【集合】
     *              length 长度 min 最小值或者最小长度 max 最大值或者最多长度
     * @return String 数据编号
     * @throws Exception
     */
    @Override
    public String saveOrUpdate(SqlSessionTemplate sqlSessionTemplate,String table,
                               Map<String, Object> saveOrUpdate,
                               List<Map<String,Object>> check,
                               String uid) throws Exception {
        // 校验是否为空或null
        CommonValidator.checkMap(saveOrUpdate, "saveOrUpdate");
        if(check!=null && !check.isEmpty()){
            // 循环校验
            for (Map map1:check){
                String field = CommonValidator.getStrValue(map1,"field");
                if("".equals(field)){
                    continue;
                }
                String value = "";
                if(saveOrUpdate.containsKey(field)){
                    value = saveOrUpdate.get(field).toString();
                }else{
                    continue;
                }
                String name = CommonValidator.getStrValue(map1,"name");
                if("".equals(value)){
                    throw new Exception(name+"【"+field+"】值不能为空！");
                }
                String type = CommonValidator.getStrValue(map1,"type");
                Map<String,Integer> mapInteger = null;
                try{
                    mapInteger = CommonValidator.getMapInteger(map1,"length");
                }catch (Exception e){
                    throw new Exception("参数 check 中 length 数据类型异常！");
                }
                if(mapInteger!=null && !mapInteger.isEmpty()){
                    // 最小值或者最小长度
                    int min = mapInteger.get("min");
                    // 最大值或者最多长度
                    int max = mapInteger.get("max");
                    if("string".equals(type)){// 字符串
                        int length = value.length();
                        if(!(min <= length && length <= max)){
                            throw new Exception(name+"【"+field+"】值长度需在【"+min+"】和【"+max+"】之间！");
                        }
                    }else if("int".equals(type)){
                        if(!CommonValidator.checkNumber(value)){
                            throw new Exception(name+"【"+field+"】值类型应为【"+type+"】！");
                        }
                        int value_ = Integer.parseInt(value);
                        if(!(min<=value_ && value_<=max)){
                            throw new Exception(name+"【"+field+"】值大小需在【"+min+"】和【"+max+"】之间！");
                        }
                    }
                }
            }
        }
        String id = "";
        if (StringUtils.isEmpty(saveOrUpdate.get("id"))) {// 为空 新增
            id = insert(sqlSessionTemplate,table, saveOrUpdate, uid);
        } else {// 不为空的时候添加
            id = saveOrUpdate.get("id").toString();
            id = update(sqlSessionTemplate,table, saveOrUpdate, id, uid);
        }
        return id;
    }

    /**
     * 新增或编辑，当 saveOrUpdate 存在id时更新，不存在时新增
     *
     * @param table        表名
     * @param saveOrUpdate 新增获取编辑的数据内容
     * @param uid
     * @return String 数据编号
     * @throws Exception
     */
    @Override
    public String saveOrUpdate(SqlSessionTemplate sqlSessionTemplate,String table,
                               Map<String, Object> saveOrUpdate,
                               String uid) throws Exception {
        // 校验是否为空或null
        CommonValidator.checkMap(saveOrUpdate, "saveOrUpdate");
        String id = "";
        if (StringUtils.isEmpty(saveOrUpdate.get("id"))) {// 为空 新增
            id = insert(sqlSessionTemplate,table, saveOrUpdate, uid);
        } else {// 不为空的时候添加
            id = saveOrUpdate.get("id").toString();
            id = update(sqlSessionTemplate,table, saveOrUpdate, id, uid);
        }
        return id;
    }

    /**
     * 根据查询条件更新数据
     *
     * @param table  表名
     * @param update 新增或编辑的数据内容
     * @param query  查询条件
     * @return 返回 true/false
     * @throws Exception
     */
    @Override
    public boolean updateBatch(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, Object> update, Map<String, String> query, String uid) throws Exception {
        SqlUtil.editMap(update, uid);
        Map<String, Object> sql_map = SqlUtil.sqlMap(UpdateSql.sql(update, table, query));
        return update(sqlSessionTemplate,sql_map);
    }

    /**
     * 根据指定编号数据删除-物理删除
     *
     * @param table 表名
     * @param id    数据编号
     * @return 返回 true/false
     * @throws Exception
     */
    @Override
    public boolean delete(SqlSessionTemplate sqlSessionTemplate,String table, String id, String uid) throws Exception {
        Map<String, Object> sql_map = SqlUtil.sqlMap(DelSql.sql(table, id));
        return delete(sqlSessionTemplate,sql_map);
    }

    /**
     * 根据指定条件数据删除-物理删除
     *
     * @param table 表名
     * @param query 指定条件删除
     * @return 返回 true/false
     * @throws Exception
     */
    @Override
    public boolean deleteBatch(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, String> query, String uid) throws Exception {
        Map<String, Object> sql_map = SqlUtil.sqlMap(DelSql.sql(table, query));
        return delete(sqlSessionTemplate,sql_map);
    }

    /**
     * 根据指定编号数据删除-逻辑删除 更新is_deleted='1'
     *
     * @param table 表名
     * @param id    数据编号
     * @return 返回 true/false
     * @throws Exception
     */
    @Override
    public boolean deleteLogic(SqlSessionTemplate sqlSessionTemplate,String table, String id, String uid) throws Exception {
        Map<String, Object> sql_map = SqlUtil.sqlMap(DelSql.sqlUpdateDel(table, id, uid));
        return update(sqlSessionTemplate,sql_map);
    }

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
    @Override
    public boolean deleteLogicBatch(SqlSessionTemplate sqlSessionTemplate,String table, String field, List<String> values, String uid) throws Exception {
        if (values.size() > 1000) {
            throw new Exception("in 查询时数量最多1000");
        }
        Map<String, Object> sql_map = SqlUtil.sqlMap(DelSql.sqlUpdateDelIn(table, values, field, uid));
        return update(sqlSessionTemplate,sql_map);
    }


    /**
     * 根据指定条件数据删除-逻辑删除
     *
     * @param table 表名
     * @param query 指定条件删除
     * @return 返回 true/false
     * @throws Exception
     */
    @Override
    public boolean deleteLogicBatch(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, String> query, String uid) throws Exception {
        Map<String, Object> sql_map = SqlUtil.sqlMap(DelSql.sqlUpdateDel(table, query, uid));
        return update(sqlSessionTemplate,sql_map);
    }

    /**
     * 根据指定编号查询数据
     *
     * @param table 表名
     * @param id    数据编号
     * @return Map<String   ,   Object> 返回map集合
     * @throws Exception
     */
    @Override
    public Map<String, Object> get(SqlSessionTemplate sqlSessionTemplate,String table, String id, String[] fields) throws Exception {
        // sql 语句
        String sql = SelectSql.sql(table, fields, id);
        Map<String, Object> sql_map = SqlUtil.sqlMap(sql);
        Map<String, Object> result = null;
        try {
            result = sqlSessionTemplate.selectOne(sqlMapping + "selectSql", sql_map);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return result == null ? new HashMap<>() : result;
    }

    /**
     * 根据sql 获取对象
     *
     * @param t
     * @param sql
     * @return
     * @throws Exception
     */
    @Override
    public <T> T get(SqlSessionTemplate sqlSessionTemplate,T t, String sql) throws Exception {
        if (StringUtils.isEmpty(sql)) {
            throw new Exception("参数 sql 不能为空！");
        }
        Map<String, Object> sql_map = new HashMap<>();
        sql_map.put("sql", sql);
        List<T> list = sqlSessionTemplate.selectList(sqlMapping + "selectSql", sql_map);
        if (list == null || list.size() == 0) {
            return t;
        }
        return (T) list.get(0);
    }

    /**
     * 根据查询条件获取列表（精确匹配）
     *
     * @param table  表名
     * @param query  精确查询条件
     * @param fields
     * @return List<Map   <   String   ,   Object>> 返回集合
     * @throws Exception
     */
    @Override
    public List<Map<String, Object>> findListExact(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, String> query, String[] fields) throws Exception {
        // 校验是否为空或null
        CommonValidator.checkMapStr(query, "query");
        return findList(sqlSessionTemplate,table, query, null, null, null, fields);
    }

    /**
     * 根据查询条件获取列表（in子句）
     *
     * @param table  表名
     * @param query  in查询条件
     * @param fields
     * @return List<Map   <   String   ,   Object>> 返回集合
     * @throws Exception
     */
    @Override
    public List<Map<String, Object>> findListIn(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, List<String>> query, String[] fields) throws Exception {
        return findList(sqlSessionTemplate,table, null, null, query, null, fields);
    }

    /**
     * 根据查询条件获取列表（模糊匹配）
     *
     * @param table  表名
     * @param query  模糊查询条件
     * @param fields
     * @return List<Map   <   String   ,   Object>> 返回集合
     * @throws Exception
     */
    @Override
    public List<Map<String, Object>> findListLike(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, String> query, String[] fields) throws Exception {
        // 校验是否为空或null
        CommonValidator.checkMapStr(query, "query");
        return findList(sqlSessionTemplate,table, null, query, null, null, fields);
    }

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
    @Override
    public List<Map<String, Object>> findList(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, String> exact_search, Map<String, String> search,
                                              Map<String, List<String>> in_search, Map<String, String> order, String[] fields) throws Exception {
        // 获取sql语句
        String sql = SelectSql.sql(table, fields, exact_search, search, in_search, order);
        Map<String, Object> sql_map = SqlUtil.sqlMap(sql);
        return findList(sqlSessionTemplate,sql_map);
    }

    /**
     * 执行删除sql
     *
     * @param sql_map sql 相关的对象集合 key  sql
     * @return
     */
    private boolean delete(SqlSessionTemplate sqlSessionTemplate,Map<String, Object> sql_map) throws Exception {
        int result = 0;
        try {
            result = sqlSessionTemplate.delete(sqlMapping + "deleteSql", sql_map);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 执行更新sql
     *
     * @param sql_map
     * @return
     */
    private boolean update(SqlSessionTemplate sqlSessionTemplate,Map<String, Object> sql_map) throws Exception {
        int result = 0;
        try {
            result = sqlSessionTemplate.update(sqlMapping + "updateSql", sql_map);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 执行列表查询sql
     *
     * @param sql_map sql 查询的map对象
     * @return 返回结果数据
     * @throws Exception 异常信息
     */
    private List<Map<String, Object>> findList(SqlSessionTemplate sqlSessionTemplate,Map<String, Object> sql_map) throws Exception {
        List<Map<String, Object>> list = null;
        try {
            list = sqlSessionTemplate.selectList(sqlMapping + "selectSql", sql_map);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return list == null ? new ArrayList<>() : list;
    }

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
     * @param fields       显示字段数据
     * @return 返回数据
     * @throws Exception 异常信息
     */
    @Override
    public Map<String, Object> page(SqlSessionTemplate sqlSessionTemplate,String table,
                                    int pageNo,
                                    int pageSize,
                                    Map<String, String> exact_search,
                                    Map<String, String> search,
                                    Map<String, List<String>> in_search,
                                    Map<String, String> order,
                                    String[] fields) throws Exception {
        // 返回数据
        Map<String, Object> result = new HashMap<>();
        // 分页数据
        Page page = PageHelper.startPage(pageNo, pageSize, true);
        // 获取数据
        List<Map<String, Object>> list = findList(sqlSessionTemplate,table, exact_search, search, in_search, order, fields);
        // 总数据
        long all_num = 0;
        // 获取 pageInfo
        PageInfo info = new PageInfo<>(page.getResult());
        // 总数
        all_num = info.getTotal();
        result.put("data", list);

        Map<String,Integer> pager = new HashMap<>();
        pager.put("total", new Long(all_num).intValue());
        pager.put("start", (pageNo - 1) * pageSize);
        pager.put("limit", pageSize);
        result.put("pager",pager);
        return result;
    }

    /**
     * 分页数据
     *
     * @param table  表名
     * @param map    组合参数
     * @param fields 显示字段数组
     * @return 返回数据
     * @throws Exception 异常信息
     */
    @Override
    public Map<String, Object> page(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, Object> map, String[] fields) throws Exception {
        int pageSize = 0;
        try {
            pageSize = Integer.parseInt(map.get("pageSize") == null || "".equals(map.get("pageSize").toString()) ? "10" : map.get("pageSize").toString());
        } catch (Exception e) {
            throw new Exception("pageSize 参数类型错误");
        }
        int pageNo = 10;
        try {
            pageNo = Integer.parseInt(map.get("pageNo") == null || "".equals(map.get("pageNo").toString()) ? "1" : map.get("pageNo").toString());
        } catch (Exception e) {
            throw new Exception("pageSize 参数类型错误");
        }
        Map<String, String> exact_search = QueryUtil.getMap(map, "exact_search");
        Map<String, String> search = QueryUtil.getMap(map, "search");
        Map<String, List<String>> in_search = QueryUtil.getList(map, "in_search");
        Map<String, String> order = QueryUtil.getMap(map, "order");
        Map<String, Object> result = page(sqlSessionTemplate,table, pageNo, pageSize, exact_search, search, in_search, order, fields);
        return result;
    }

    /**
     * 根据sql语句查询结果
     *
     * @param sql 查询的sql
     * @return 返回集合
     * @throws Exception 异常信息
     */
    @Override
    public List<Map<String, Object>> findList(SqlSessionTemplate sqlSessionTemplate,String sql) throws Exception {
        Map<String, Object> sql_map = SqlUtil.sqlMap(sql);
        return findList(sqlSessionTemplate,sql_map);
    }

    /**
     * 新增或编辑
     *
     * @param map   参数
     * @param table 表名
     * @param uid   用户编号
     * @return 返回字符串
     * @throws Exception 抛出异常
     */
    @Override
    public String saveOrUpdate(Map<String, Object> map, SqlSessionTemplate sqlSessionTemplate,String table, String uid) throws Exception {
        Map<String, Object> saveOrUpdate = null;
        try {
            saveOrUpdate = QueryUtil.checkMap(map, "saveOrUpdate");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return saveOrUpdate(sqlSessionTemplate,table, saveOrUpdate, uid);
    }

    /**
     * 新增
     *
     * @param sqlSessionTemplate
     * @param table              表名
     * @param map                参数
     * @param id
     * @param uid                用户编号
     * @return 返回字符串
     * @throws Exception 抛出异常
     */
    @Override
    public String save(SqlSessionTemplate sqlSessionTemplate,
                       String table,
                       Map<String, Object> map,
                       String id,
                       String uid) throws Exception {
        // 生成流水号
        // 添加时增加用户名和创建时间
        SqlUtil.addMap(map, uid);
        map.put("id",id);
        Map<String, Object> sql_map = SqlUtil.sqlMap(InsertSql.sql(map, table));
        int result = 0;
        try {
            result = sqlSessionTemplate.insert(sqlMapping + "insertSql", sql_map);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        if (result > 0) {
            return id;
        } else {
            return "";
        }
    }

    /**
     * 新增或编辑
     *
     * @param table 表名
     * @param map   参数
     * @param check （必填，必须校验） 集合
     *              field 字段名称
     *              name 字段显示名称
     *              type 类型 string【字符串】 int【整型】  array【集合】
     * @param uid   用户编号
     * @return 返回字符串
     * @throws Exception 抛出异常
     */
    @Override
    public String saveOrUpdate(Map<String, Object> map,
                               List<Map<String,Object>> check,
                               SqlSessionTemplate sqlSessionTemplate,String table,
                               String uid) throws Exception {
        Map<String, Object> saveOrUpdate = null;
        try {
            saveOrUpdate = QueryUtil.checkMap(map, "saveOrUpdate");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return saveOrUpdate(sqlSessionTemplate,table, saveOrUpdate,check, uid);
    }

    /**
     * 列表查询
     *
     * @param table 表名
     * @param map   参数
     * @return 返回集合
     * @throws Exception 抛出异常
     */
    @Override
    public List<Map<String, Object>> findList(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, Object> map) throws Exception {
        Map<String, String> exact_search = QueryUtil.getMap(map, "exact_search");
        Map<String, String> search = QueryUtil.getMap(map, "search");
        Map<String, List<String>> in_search = QueryUtil.getList(map, "in_search");
        Map<String, String> order = QueryUtil.getMap(map, "order");
        String[] fields = QueryUtil.getMapArray(map, "fields");
        return findList(sqlSessionTemplate,table, exact_search, search, in_search, order, fields);
    }

    /**
     * 分页列表
     *
     * @param table 表名
     * @param map   参数
     * @return 返回对象
     * @throws Exception 抛出异常
     */
    @Override
    public Map<String, Object> page(SqlSessionTemplate sqlSessionTemplate,String table, Map<String, Object> map) throws Exception {
        String[] fields = QueryUtil.getMapArray(map, "fields");
        return page(sqlSessionTemplate,table, map, fields);
    }

    /**
     * 返回逻辑删除的状态
     *
     * @param table 表名
     * @param id    数据编号
     * @throws Exception 异常信息
     */
    @Override
    public void restoreDelete(SqlSessionTemplate sqlSessionTemplate,String table, String id,String uid) throws Exception {
        Map<String,Object> update = new HashMap<>();
        update.put("is_deleted","0");
        update(sqlSessionTemplate,table,update,id,uid);
    }

    /**
     * 根据ids集合获取指定字段的集合
     *
     * @param table      表名
     * @param ids        id集合
     * @param show_field 显示的字段
     * @return 返回列表集合
     * @throws Exception 异常信息
     */
    @Override
    public List<String> findList(SqlSessionTemplate sqlSessionTemplate,String table, List<String> ids, String show_field) throws Exception {
        // 获取sql语句
        String sql = SelectSql.getQuerySql(table,ids,show_field);
        Map<String, Object> sql_map = SqlUtil.sqlMap(sql);
        return sqlSessionTemplate.selectList(sqlMapping + "selectSql", sql_map);
    }

    /**
     * 根据一个字段来统计数据
     *
     * @param table 表名
     * @param field 字段值
     * @param order 排序
     * @return 返回统计信息
     * @throws Exception 异常信息
     */
    @Override
    public List<Map<String, Object>> findGroupCount(SqlSessionTemplate sqlSessionTemplate,String table, String field, Map<String, String> order) throws Exception {
        String[] fields =  new String[]{field};
        return findGroupCount(sqlSessionTemplate,table,fields,order);
    }


    /**
     * 根据多个字段来统计数据
     *
     * @param table  表名
     * @param fields 字段值集合
     * @param order  排序
     * @return 返回统计信息
     * @throws Exception 异常信息
     */
    @Override
    public List<Map<String, Object>> findGroupCount(SqlSessionTemplate sqlSessionTemplate,String table, String[] fields, Map<String, String> order) throws Exception {
        return findGroupCount(sqlSessionTemplate,table,fields,order,-1);
    }

    /**
     * 根据多个字段来统计数据
     *
     * @param table  表名
     * @param fields 字段值集合
     * @param order  排序
     * @param size   前n条数据
     * @return 返回统计信息
     * @throws Exception 异常信息
     */
    @Override
    public List<Map<String, Object>> findGroupCount(SqlSessionTemplate sqlSessionTemplate,String table, String[] fields, Map<String, String> order, int size) throws Exception {
        String sql = GroupCountSql.sql(table,fields,order,size);
        Map<String, Object> sql_map = SqlUtil.sqlMap(sql);
        return findList(sqlSessionTemplate,sql_map);
    }
}
