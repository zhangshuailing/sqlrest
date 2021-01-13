package nk.gk.wyl;

import nk.gk.wyl.sql.api.SqlService;
import nk.gk.wyl.sql.controller.SqlRestController;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class SqlRestApplicationTests {

    @Autowired
    private SqlService sqlService;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Test
    void contextLoads() {


        /*String id = "368be343f0c04d2da64f01b6bda50fd8";
        try {
            boolean bl = sqlService.deleteLogic("user",id,"zslzsl");
            System.out.println(bl);
        } catch (MyException e) {
            e.printStackTrace();
        }*/
        String[] usernames = {"张三", "李四", "王五",
                "李青", "张泽", "王宇", "雷鸣", "封装", "风筝"};
        Map<String, Object> save = new HashMap<>();
        for (int i = 0; i < usernames.length; i++) {
            save.put("content", usernames[i] + "-demo-" + i);
            try {
                sqlService.insert(sqlSessionTemplate,"user", save, "zsl");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*try {
            Map<String,String> query = new HashMap<>();
            query.put("username","特朗普");
            String[] fields = new String[]{"username","sex"};
            Map<String,String> search = new HashMap<>();
            search.put("username","张");
            List<Map<String,Object>> obj = sqlService.findList("user_info",null,search,null,null,fields);
        } catch (MyException e) {

            System.out.println("-------------------------------- 871679849");
            System.out.println(e.getMessage());
            System.out.println("--------------------------------");
        }*/
        /*String sql = "select  *  from user";
        try {
            List<Map<String,Object>> list = sqlService.findList(sql);
            System.out.println(list);
        } catch (MyException e) {
            e.printStackTrace();
        }*/
       /* try {
            Map<String,Object> query = new HashMap<>();
            String[] fields = {"username","sex"};
            Map<String,List<String>> search = new HashMap<>();
            List<String> list = new ArrayList<>();
            list.add("2");
            search.put("sex",list);
            query.put("in_search",search);
            query.put("pageNo","1");
            query.put("pageSize","2");
            Map<String,Object> obj = sqlService.page("user",query,fields);
            System.out.println(obj);
        } catch (MyException e) {

            System.out.println("-------------------------------- 871679849");
            System.out.println(e.getMessage());
            System.out.println("--------------------------------");
        }*/

        /*try {
            sqlService.delete("user","6e8490b2ea07415a852984bc5fc3257d","09901");
        } catch (MyException e) {
            e.printStackTrace();
        }*/
        /*Map<String,Object> save = new HashMap<>();
        save.put("username","特朗普00912121");
        try {
            save.put("id","6e8490b2ea07415a852984bc5fc3257d");
            Map<String,String> query = new HashMap<>();
            query.put("sex","1");
            query.put("dept_id","001");
            boolean id = sqlService.updateBatch("user",save,query,"09901");
            System.out.println(id);
        } catch (MyException e) {
            e.printStackTrace();
        }*/
    }
}
