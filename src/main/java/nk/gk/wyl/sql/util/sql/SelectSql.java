package nk.gk.wyl.sql.util.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @Description: 查询的sql 语句
 * @Author: zhangshuailing
 * @CreateDate: 2020/8/29 10:23
 * @UpdateUser: zhangshuailing
 * @UpdateDate: 2020/8/29 10:23
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class SelectSql {

    private static boolean is_logic=false;

    public static boolean isIs_logic() {
        return is_logic;
    }

    public static void setIs_logic(boolean is_logic) {
        SelectSql.is_logic = is_logic;
    }

    // logger 日志
    private static Logger logger = LoggerFactory.getLogger(SelectSql.class);

    /**
     * 根据数据编号获取单条数据sql语句
     *
     * @param table  表名
     * @param fields 查询显示的字段数组
     * @param id     数据编号
     * @return 返回sql 语句
     */
    public static String sql(String table, String[] fields, String id) {
        String sql = "select " + getSelectSqlFields(fields) + " from " + table
                + " " + getCommonWhereSql() + " and id = '" + id + "'";
        logger.info(sql);
        return sql;
    }

    /**
     * 统一查询的sql语句
     *
     * @param table        表名
     * @param fields       显示字段数组
     * @param exact_search 精确查找 key 字段名 value 字段值
     * @param search       模糊匹配 key 字段名 value 字段值
     * @param in_search    in子查询 key 字段名 value 字段值集合
     * @param order        排序 key 字段名 value  排序 （asc desc）
     * @return
     */
    public static String sql(String table, String[] fields, Map<String, String> exact_search,
                             Map<String, String> search,
                             Map<String, List<String>> in_search,
                             Map<String, String> order) {
        // sql 语句
        String sql = "select " + getSelectSqlFields(fields) + " from " + table
                + " " + getCommonWhereSql();
        // 精确查找
        if (exact_search != null && !exact_search.isEmpty()) {
            sql = sql + " and " + getWhereSqlExact(exact_search);
        }
        // 模糊匹配
        if (search != null && !search.isEmpty()) {
            sql = sql + " and " + getWhereSqlLike(search);
        }
        // in查询
        if (in_search != null && !in_search.isEmpty()) {
            sql = sql + " and " + getWhereSqlIn(in_search);
        }
        // order 排序
        sql = sql + " " + getOrderSql(order);
        return sql;
    }


    /**
     * where字句前缀
     *
     * @return
     */
    public static String getCommonWhereSql() {
        if(is_logic){
            return " where "+DelSql.getIs_deleted()+" = '0' ";
        }else{
            return " where 1=1 ";
        }

    }

    /**
     * in查询条件生成where查询条件（不含where ）
     *
     * @param in_search 查询条件 key 字段名 value  list<String>字段值集合
     * @return 返回where 查询语句
     */
    public static String getWhereSqlIn(Map<String, List<String>> in_search) {
        String whereSql = "";
        for (Map.Entry<String, List<String>> key : in_search.entrySet()) {
            if (key.getValue() == null || key.getValue().size() == 0) {
                continue;
            }
            if ("".equals(whereSql)) {
                whereSql = key.getKey() + " in " + getInStr(key.getValue());
            } else {
                whereSql = whereSql + " and " + key.getKey() + " in " + getInStr(key.getValue());
            }
        }
        return whereSql;
    }

    /**
     * in查询条件生成where查询条件（不含where ）
     *
     * @param field  字段
     * @param values 字段值集合
     * @return 返回where 查询语句
     */
    public static String getWhereSqlIn(String field, List<String> values) {
        String whereSql = field + " in " + getInStr(values);
        return whereSql;
    }

    /**
     * 精确查询条件生成where查询条件（不含where ）
     *
     * @param query 查询条件
     * @return 返回where 查询语句
     */
    public static String getWhereSqlExact(Map<String, String> query) {
        String whereSql = "";
        for (Map.Entry<String, String> key : query.entrySet()) {
            if (StringUtils.isEmpty(key.getValue())) {
                continue;
            }
            if ("".equals(whereSql)) {
                whereSql = key.getKey() + " = '" + key.getValue() + "'";
            } else {
                whereSql = whereSql + " and " + key.getKey() + " = '" + key.getValue() + "'";
            }
        }
        return whereSql;
    }


    /**
     * 模糊查询条件生成where查询条件（不含where ）
     *
     * @param query 查询条件
     * @return 返回where 查询语句
     */
    public static String getWhereSqlLike(Map<String, String> query) {
        String whereSql = "";
        for (Map.Entry<String, String> key : query.entrySet()) {
            if (StringUtils.isEmpty(key.getValue())) {
                continue;
            }
            if ("".equals(whereSql)) {
                whereSql = key.getKey() + " like '%" + key.getValue() + "%'";
            } else {
                whereSql = whereSql + " and " + key.getKey() + " like '%" + key.getValue() + "%'";
            }
        }
        return whereSql;
    }

    /**
     * 生成需要显示的字段字符串
     *
     * @param fields 字段集合
     * @return 返回字段字符串
     */
    public static String getSelectSqlFields(String[] fields) {
        if (fields == null || fields.length == 0) {
            return " * ";
        }
        String str = "";
        for (int i = 0; i < fields.length; i++) {
            if (i == 0) {
                str = fields[i];
            } else {
                str = str + "," + fields[i];
            }
        }
        return str;
    }

    /**
     * 将集合转字符串
     *
     * @param list 字符串集合
     * @return 返回 字符串 ('1','2','3')
     */
    public static String getInStr(List<String> list) {
        String values = "";
        if (list == null || list.size() == 0) {
            return "('')";
        }
        for (String value : list) {
            if ("".equals(values)) {
                values = "'" + value + "'";
            } else {
                values = values + "," + "'" + value + "'";
            }
        }
        return "(" + values + ")";
    }

    /**
     * 生成排序
     *
     * @param order 排序 key 字段名 value  排序
     * @return
     */
    public static String getOrderSql(Map<String, String> order) {
        String order_sql = "";
        if (order != null && !order.isEmpty()) {
            for (Map.Entry<String, String> key : order.entrySet()) {
                if ("".equals(order_sql)) {
                    order_sql = " " + key.getKey() + " " + key.getValue();
                } else {
                    order_sql = order_sql + " , " + key.getKey() + " " + key.getValue();
                }
            }
        }
        if ("".equals(order_sql)) {
            //order_sql = " order by create_time desc ";
        } else {
            order_sql = " order by " + order_sql;
        }
        return order_sql;
    }

    /**
     * 拼接sql语句
     *
     * @param sql   sql语句
     * @param query 查询条件
     * @return
     */
    public static String getQuerySql(String sql, Map<String, String> query) {
        if (StringUtils.isEmpty(sql)) {// 判断是否为空
            sql = SelectSql.getCommonWhereSql();
        }
        sql = sql + " and " + getWhereSqlExact(query);
        return sql;
    }

    /**
     * 根据指定显示的字段，id集合 拼接sql
     * @param table 表名
     * @param ids id 集合
     * @param show_field 显示字段
     * @return
     */
    public static String getQuerySql(String table,List<String> ids,String show_field){
        String sql = "select " + show_field + " from " + table + " " + getCommonWhereSql() + "and" + getWhereSqlIn("id",ids);
        return sql;
    }

}
