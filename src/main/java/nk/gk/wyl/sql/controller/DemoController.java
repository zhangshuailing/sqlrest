package nk.gk.wyl.sql.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
@RequestMapping("demo/{table}")
@Api(tags = "demo接口")
public class DemoController extends SqlRestController {
    /**
     * 分页语句
     *
     * @param table    表名
     * @param body json 参数
     * @return 返回map 对象
     */
    @PostMapping("page")
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
        return super.page(table,body);
    }

    /**
     * 分页语句
     *
     * @param table    表名
     * @param body json 参数
     * @return 返回map 对象
     */
    @PostMapping("page2")
    @ApiOperation(value = "2分页列表的接口")
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
    public Map<String, Object> page2(@PathVariable("table") String table,
                                    @RequestBody Map<String, Object> body) throws Exception {
        return super.page(table,body);
    }
}
