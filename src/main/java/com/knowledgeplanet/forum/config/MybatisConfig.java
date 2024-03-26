package com.knowledgeplanet.forum.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;


// 配置类
@Configuration
// 指定Mybatis的扫描路径, mapper所在的包
@MapperScan("com.knowledgeplanet.forum.dao")
public class MybatisConfig {

}
