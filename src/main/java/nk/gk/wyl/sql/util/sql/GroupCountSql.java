package nk.gk.wyl.sql.util.sql;

import java.util.Map;

/**
* @Description:    统计的sql
* @Author:         zhangshuailing
* @CreateDate:     2020/10/15 0:10
* @UpdateUser:     zhangshuailing
* @UpdateDate:     2020/10/15 0:10
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class GroupCountSql {

    public static String sql(String table, String[] fields, Map<String,String> order, int size){
        String key = "";
        for (String str : fields){
            if("".equals(key)){
                key = str;
            }else{
                key = key + "," + str;
            }
        }
        String sql = "select " + key +",count(1) count from "+table+" where "+DelSql.getIs_deleted()+" !='1' group  by " + key ;
        if(order == null || order.isEmpty()){
            sql = sql + " order by count desc";
        }else{
            String order_str = "";
            for (Map.Entry<String,String> ky:order.entrySet()){
                if("".equals(order_str)){
                    order_str = ky.getKey() +" " + ky.getValue();
                }else{
                    order_str = order_str + ", " + ky.getKey() +" " + ky.getValue();
                }
            }
            sql += " order by " + order_str;
        }
        if(size !=-1){
            sql += " limit 0,"+size;
        }
        return sql;
    }
}
