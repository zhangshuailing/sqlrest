package nk.gk.wyl.sql.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: sql 语句
 * @Author: zhangshuailing
 * @CreateDate: 2020/8/13 21:06
 * @UpdateUser: zhangshuailing
 * @UpdateDate: 2020/8/13 21:06
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class SqlUtil {
    // logger 日志
    private static Logger logger = LoggerFactory.getLogger(SqlUtil.class);

    /**
     * 查询时增加sql参数
     *
     * @param sql sql 语句
     * @return 返回Map
     */
    public static Map<String, Object> sqlMap(String sql) {
        Map<String, Object> sql_map = new HashMap<>();
        sql_map.put("sql", sql);
        logger.info("sql语句："+sql);
        return sql_map;
    }

    /**
     * 新增时增加创建人和创建时间
     *
     * @param save 新增的数据集合
     * @param uid  用户编号
     */
    public static void addMap(Map<String, Object> save, String uid) {
        save.put("create_by", uid);
        save.put("create_time", Util.getStrDate());
    }

    /**
     * 编辑时增加创建人和创建时间
     *
     * @param update 编辑的数据集合
     * @param uid    用户编号
     */
    public static void editMap(Map<String, Object> update, String uid) {
        update.put("update_by", uid);
        update.put("update_time", Util.getStrDate());
    }
}
