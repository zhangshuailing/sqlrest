package nk.gk.wyl.sql.util.sql;

import nk.gk.wyl.sql.util.CommonValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Description: 更新的sql 语句
 * @Author: zhangshuailing
 * @CreateDate: 2020/8/29 10:23
 * @UpdateUser: zhangshuailing
 * @UpdateDate: 2020/8/29 10:23
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class UpdateSql {

    // logger 日志
    private static Logger logger = LoggerFactory.getLogger(UpdateSql.class);

    /**
     * 根据数据编号（id）更新sql语句
     *
     * @param update 更新的参数
     * @param table  表名
     * @return 返回sql 语句
     * @throws Exception 抛出异常
     */
    public static String sql(Map<String, Object> update, String table, String id) throws Exception {
        CommonValidator.checkMap(update, "update");
        String update_sql = "update " + table + " set " + updateSql(update, id);
        return update_sql;
    }

    /**
     * 更新数据sql
     *
     * @param update
     * @return
     */
    public static String updateSql(Map<String, Object> update) {
        // 更新的参数数据
        String values_sql = "";
        if (update.containsKey("id")) {
            update.remove("id");
        }
        // 循环更新的参数
        for (Map.Entry<String, Object> key : update.entrySet()) {
            String field = key.getKey();
            Object obj = key.getValue();
            if (StringUtils.isEmpty(values_sql)) {// 判断是否是空
                if (obj instanceof String) {// 字符串时插入数据
                    values_sql = field + "= '" + obj.toString() + "'";
                } else {
                    values_sql = field + "= '" + obj + "'";
                }
            } else {
                if (obj instanceof String) {// 字符串时插入数据
                    values_sql = values_sql + "," + field + "= '" + obj.toString() + "'";
                } else {
                    values_sql = values_sql + "," + field + "= '" + obj + "'";
                }
            }
        }
        return values_sql;
    }

    /**
     * 更新的sql语句
     *
     * @param update 更新条件
     * @return 返回 sql 语句
     */
    private static String updateSql(Map<String, Object> update, String id) {
        return updateSql(update) + " where id ='" + id + "'";
    }

    /**
     * 根据查询条件获取更新sql语句
     *
     * @param update 更新条件
     * @param table  表名
     * @param query  查询条件
     * @return 返回的sql语句
     * @throws Exception 抛出异常
     */
    public static String sql(Map<String, Object> update, String table, Map<String, String> query) throws Exception {
        // 校验 update  query
        CommonValidator.checkMap(update, "update");
        CommonValidator.checkMapStr(query, "query");
        String update_sql = "update " + table + " set " + updateSql(update) + " " + SelectSql.getWhereSqlExact(query);
        return update_sql;
    }
}
