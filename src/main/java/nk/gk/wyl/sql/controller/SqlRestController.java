package nk.gk.wyl.sql.controller;

import io.swagger.annotations.*;
import nk.gk.wyl.sql.api.SqlService;
import nk.gk.wyl.sql.util.QueryUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("rest/v1/sql-rest")
@Api(tags = "通用相关接口")
public class SqlRestController {
    @Autowired
    private SqlService sqlService;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    /**
     * 添加或更新
     *
     * @param table 表名
     * @param body 请求body 参数
     * @return
     */
    @PostMapping("{table}/saveOrUpdate")
    @ApiOperation(value = "新增或编辑的接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "table", value = "数据库表名", required = true,dataType = "string",paramType = "path"),
            @ApiImplicitParam(name = "body", value = "Body参数【saveOrUpdate 必填的参数】<br>" +
                    "参数如下：<br>" +
                    "1、saveOrUpdate {}添加或修改参数" +
                    "2、uid 当前操作用户编号",required = true,defaultValue = "{}")
    })
    public String saveOrUpdate(@PathVariable("table") String table,
                               @RequestBody Map<String, Object> body) throws Exception {
        return sqlService.saveOrUpdate(body,sqlSessionTemplate, table, QueryUtil.getUid(body));
    }

    /**
     * 分页语句
     *
     * @param table    表名
     * @param body json 参数
     * @return 返回map 对象
     */
    @PostMapping("{table}/page")
    @ApiOperation(value = "分页列表的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "table", value = "数据库表名",dataType="string",defaultValue = "",paramType = "path",required = true),
            @ApiImplicitParam(name = "body", value = "body参数，参数如下：</br>" +
                    "1、pageNo 当前页面【int】</br>" +
                    "2、pageSize 每页默认显示条数【int】</br>" +
                    "3、fields 显示字段集合</br>" +
                    "4、order 排序{filed:int[1升序-1降序]}</br>" +
                    "5、exact_search 精确查找 {field[字段名]:value[字段值 字符串]}<br>" +
                    "6、search 模糊查找 {field[字段名]:value[字段值 字符串]} <br>" +
                    "7、in_search in字句 {field[字段名]:value[字段值 List<T>]}<br>" +
                    "8、rang_search 区间查询 {field[字段名]:value[字段值 {start:\"\",end:\"\",format:\"number 数字，time 时间 date 日期 year 年 month 月\"}]}<br>" +
                    "9、uid 当前登录账号信息 String<br>" +
                    "",required = true,defaultValue = "{}")
    })
    public Map<String, Object> page(@PathVariable("table") String table,
                                    @RequestBody Map<String, Object> body) throws Exception {
        return sqlService.page(sqlSessionTemplate,table, body);
    }

    /**
     * 列表
     *
     * @param table 表名
     * @param uid 当前登录账号
     * @return
     * @throws Exception
     */
    @GetMapping("{table}/list")
    @ApiOperation(value = "GET方式列表的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "table", value = "数据库表名",dataType="string",defaultValue = "",paramType = "path",required = true),
            @ApiImplicitParam(name = "uid", value = "当前登录编号", paramType = "query",dataType = "String"),
    })
    public List<Map<String, Object>> findList(@PathVariable("table") String table,
                                              @RequestParam(value = "uid",required = false,defaultValue = "") String uid) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("uid",uid);
        return sqlService.findList(sqlSessionTemplate,table, map);
    }

    /**
     * 列表
     *
     * @param table 表名
     * @param body 请求body参数
     * @return 返回集合数据
     * @throws Exception 异常信息
     */
    @PostMapping("{table}/list")
    @ApiOperation(value = "POST方式列表的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "table", value = "数据库表名",dataType="string",defaultValue = "",paramType = "path",required = true),
            @ApiImplicitParam(name = "body", value = "body参数，参数如下：</br>" +
                    "1、fields 显示字段集合</br>" +
                    "2、order 排序{filed:int[1升序-1降序]}</br>" +
                    "3、exact_search 精确查找 {field[字段名]:value[字段值 字符串]}<br>" +
                    "4、search 模糊查找 {field[字段名]:value[字段值 字符串]} <br>" +
                    "5、in_search in字句 {field[字段名]:value[字段值 List<T>]}<br>" +
                    "6、rang_search 区间查询 {field[字段名]:value[字段值 {start:\"\",end:\"\",format:\"number 数字，time 时间 date 日期 year 年 month 月\"}]}<br>" +
                    "7、uid 当前登录账号信息 String<br>" +
                    "",required = true,defaultValue = "{}")
    })
    public List<Map<String, Object>> findList(@PathVariable("table") String table,
                                              @RequestBody Map<String, Object> body) throws Exception {
        return sqlService.findList(sqlSessionTemplate,table, body);
    }

    /**
     * 获取单条数据
     *
     * @param table 表名
     * @param id  数据编号
     * @return
     * @throws Exception
     */
    @GetMapping("{table}/{id}")
    @ApiOperation(value = "根据id查询用户的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "table", value = "数据库表名",dataType="string",defaultValue = "",paramType = "path",required = true),
            @ApiImplicitParam(name = "id", value = "数据编号", dataType="string",defaultValue = "",paramType = "path",required = true)
    })
    public Map<String, Object> get(@PathVariable("table") String table,
                                   @PathVariable("id") String id) throws Exception {
        String[] fields = null;
        return sqlService.get(sqlSessionTemplate,table, id, fields);
    }

    /**
     * 单个删除
     *
     * @param table 表名
     * @param id    数据编号
     * @param uid   用户编号
     * @return 返回true false
     */
    @ApiOperation(value = "根据id删除数据")
    @DeleteMapping("{table}/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "table", value = "数据库表名",dataType="string",defaultValue = "",paramType = "path",required = true),
            @ApiImplicitParam(name = "id", value = "数据编号", dataType="string",defaultValue = "",paramType = "path",required = true),
            @ApiImplicitParam(name = "uid", value = "用户编号", dataType="string",defaultValue = "",paramType = "query",required = false)
    })
    public boolean delete(@PathVariable("table") String table,
                          @PathVariable("id") String id,
                          @RequestParam(value = "uid",required = false,defaultValue = "") String uid) throws Exception {
        return sqlService.deleteLogic(sqlSessionTemplate,table, id, uid);
    }

    /**
     * pl删除
     *
     * @param table 表名
     * @param body   参数
     * @return 返回true false
     */
    @PostMapping("{table}")
    @ApiOperation(value = "根据ids批量删除数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "table", value = "数据库表名",dataType="string",defaultValue = "",paramType = "path",required = true),
            @ApiImplicitParam(name = "body", value = "body参数，参数如下：<br>" +
                    "1、ids 数据编号集合<br>" +
                    "2、uid 用户编号",paramType = "body",required = true)
    })
    public boolean deleteBatch(@PathVariable("table") String table,
                               @RequestBody Map<String, Object> body) throws Exception {
        List<String> ids = QueryUtil.checkMapList(body, "ids");
        return sqlService.deleteLogicBatch(sqlSessionTemplate,table, "id", ids, QueryUtil.getUid(body));
    }

    /**
     * groupCount统计
     *
     * @param table 表名
     * @param uid   用户编号
     * @param field   字段名称
     * @param order   count排序 asc 升序 desc 降序
     * @return 统计集合
     */
    @GetMapping("{table}/group-count")
    @ApiOperation(value = "单个字段分组统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "table", value = "数据库表名",dataType="string",defaultValue = "",paramType = "path",required = true),
            @ApiImplicitParam(name = "uid", value = "用户编号", dataType="string",defaultValue = "",paramType = "query"),
            @ApiImplicitParam(name = "field", value = "字段名", dataType="string",defaultValue = "",paramType = "query",required = true),
            @ApiImplicitParam(name = "order", value = "count排序【升序:asc，降序:desc】", dataType="string",defaultValue = "desc",paramType = "query"),
            @ApiImplicitParam(name = "size", value = "前n条数据", dataType="int",defaultValue = "-1",paramType = "query")
    })
    public List<Map<String,Object>> groupCount(@PathVariable("table") String table,
                                               @RequestParam(value = "uid",required = false,defaultValue = "") String uid,
                                               @RequestParam(value = "field",required = true) String field,
                                               @RequestParam(value = "order",defaultValue = "desc") String order,
                                               @RequestParam(value = "size",required = false,defaultValue = "-1") int size) throws Exception {
        String[] fields = new String[]{field};
        Map<String,String> order_map = new HashMap<>();
        order_map.put("count",order);
        return sqlService.findGroupCount(sqlSessionTemplate,table,fields,order_map,size);
    }

    /**
     * 统计
     * @param table 表名
     * @param body 参数
     * @return 返回统计结果
     * @throws Exception 异常信息
     */
    @ApiOperation(value = "单个或者多字段分组统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "table", value = "数据库表名",dataType="string",defaultValue = "",paramType = "path",required = true),
            @ApiImplicitParam(name = "body", value = "body参数，参数如下<br>" +
                    "1、keys 统计字段集合<br>" +
                    "2、order 排序字段。asc/desc}<br>" +
                    "3、uid 用户编号",required = true),
            @ApiImplicitParam(name = "size", value = "前n条数据", dataType="int",defaultValue = "-1",paramType = "query")
    })
    @PostMapping("{table}/group-count")
    public List<Map<String,Object>> groupCount(@PathVariable("table") String table,
                                @RequestParam(value = "size",required = false,defaultValue = "-1") int size,
                                @RequestBody Map<String,Object> body) throws Exception{

        String[] fields = QueryUtil.checkArray(body,"fields");
        Map<String,String> order_map = QueryUtil.getMap(body,"order");
        return sqlService.findGroupCount(sqlSessionTemplate,table,fields,order_map,size);
    }
}
