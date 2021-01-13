package nk.gk.wyl.sql.util.sql;

import nk.gk.wyl.sql.util.CommonValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 插入的sql 语句
 * @Author: zhangshuailing
 * @CreateDate: 2020/8/29 10:23
 * @UpdateUser: zhangshuailing
 * @UpdateDate: 2020/8/29 10:23
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class InsertSql {

    // logger 日志
    private static Logger logger = LoggerFactory.getLogger(InsertSql.class);

    /**
     * 插入的sql 语句
     *
     * @param save  需要保存的数据
     * @param table 表名
     * @return 返回sql 语句
     * @throws Exception 抛出异常
     */
    public static String sql(Map<String, Object> save, String table) throws Exception {
        // 校验是否为空或null
        CommonValidator.checkMap(save, "save");
        // 插入的sql 字段，字段值 Map
        Map<String, String> fields_values = getFieldsValuesSqlExact(save);
        // 执行的sql语句
        String insert_sql = "insert into " + table + " (" + fields_values.get("fields_sql") + ") values(" + fields_values.get("values_sql") + ")";
        logger.info("sql：" + insert_sql);
        return insert_sql;
    }

    /**
     * 拼接生成精确（=）查询的sql语句
     *
     * @param map 查询条件
     * @return 返回 map  key fields_sql 是字段sql  values_sql 是值sql
     */
    public static Map<String, String> getFieldsValuesSqlExact(Map<String, Object> map) {
        // 响应结果
        Map<String, String> result = new HashMap<>();
        // 字段 sql  例如 id,name,type,sex
        String fields_sql = "";
        // 值sql
        String values_sql = "";
        // 循环插入的参数
        for (Map.Entry<String, Object> key : map.entrySet()) {
            String field = key.getKey();
            Object obj = key.getValue();
            if (StringUtils.isEmpty(fields_sql)) {// 判断是否是空
                fields_sql = field;
                if (obj instanceof String) {// 字符串时插入数据
                    values_sql = "'" + obj.toString() + "'";
                } else {
                    values_sql = "'" + obj + "'";
                }
            } else {
                fields_sql = fields_sql + "," + field;
                if (obj instanceof String) {// 字符串时插入数据
                    values_sql = values_sql + ",'" + obj.toString() + "'";
                } else {
                    values_sql = values_sql + ",'" + obj + "'";
                }
            }
        }
        result.put("fields_sql", fields_sql);
        result.put("values_sql", values_sql);
        return result;
    }
}
