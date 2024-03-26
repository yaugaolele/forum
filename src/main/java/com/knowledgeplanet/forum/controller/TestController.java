package com.knowledgeplanet.forum.controller;

import com.knowledgeplanet.forum.common.AppResult;
import com.knowledgeplanet.forum.exception.ApplicationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// 日志
@Slf4j
// API 描述信息
@Api(tags = "测试接口")
// 指定测试根路径映射地址前缀
@RequestMapping("/test")
// Controller注解，返回的对象用JSON形式表示
@RestController()
public class TestController {

    /**
     * 测试接口，打印hello PLUGIN
     * @return Hello, Spring Boot...
     */
    // API 方法描述
    @ApiOperation(value = "测试接口，打印hello")
    // 地址映射
    @GetMapping("/hello")
    // 具体方法实现
    public String hello() {
        return "Hello, Spring Boot...";
    }

    /**
     * 测试接口，返回 你好+传入参数
     * @param name 姓名
     * @return 你好 + name
     */
    // 注意：@ApiParam 方法参数的注解，用来描述参数
    @ApiOperation(value = "测试接口，返回 你好+传入参数")
    @GetMapping("/helloByName")
    public AppResult<String> helloByName(@ApiParam(value = "名字", required = true) String name) {
        return AppResult.success("你好：" + name);
    }

    @ApiOperation(value = "测试接口，跳转到index.html")
    @GetMapping("/testIndex")
    public String index() {
        return "index.html";
    }

    @ApiOperation(value = "测试接口，返回一个异常结果")
    @GetMapping("/exception")
    public String testException() throws Exception {
        throw new Exception("这是一个Exception");
    }

    @ApiOperation(value = "测试接口，返回一个自定义异常结果")
    @GetMapping("/appException")
    public String testApplicationException() {
        throw new ApplicationException("这是一个自定义的ApplicationException");
    }

    @ApiOperation(value = "测试日志")
    @GetMapping("/logging")
    public void testLogging(){
        log.trace("trace...");
        log.debug("debug...");
        log.info("info...");
        log.warn("warn...");
        log.error("error...");
    }

    @ApiOperation(value = "测试API管理工具")
    @GetMapping("/apiManager")
    public AppResult<String> testApiManager(){
        return AppResult.success("你好：ApiManager");
    }
}
