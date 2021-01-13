package nk.gk.wyl.sql.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.*;

/**
 * @Description: 工具类
 * @Author: zhangshuailing
 * @CreateDate: 2020/8/13 21:23
 * @UpdateUser: zhangshuailing
 * @UpdateDate: 2020/8/13 21:23
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class Util {
    /**
     * title 获取唯一流水号
     *
     * @return string
     */
    public static String getResourceId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * title 获取当前时间
     *
     * @return 时间
     */
    public static Date getDate() {
        return new Date();
    }

    /**
     * 获取字符串时间
     *
     * @return 返回字符串时间
     */
    public static String getStrDate() {
        return DateFormatUtils.format(getDate(), "yyyy-MM-dd HH:mm:ss");
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("11");
        list.add("22");
        list.add("33");
        System.out.println(StringUtils.join(list, ","));
    }

    /**
     * 将第二个map 数据整体嵌入到第一个map里面
     *
     * @param map
     * @param map_body
     * @return
     */
    public static void secondMapAddInFirstMap(Map<String, Object> map, Map<String, Object> map_body) {
        map.putAll(map_body);
    }
}
