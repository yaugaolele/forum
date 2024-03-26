package com.knowledgeplanet.forum.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Api(tags = "公共接口")
@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping("/index")
    public String index() {
        return "index.html";
    }

    @GetMapping("/index2")
    public String index2() {
        return "index.html";
    }
}
