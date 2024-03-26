package com.knowledgeplanet.forum;

import com.knowledgeplanet.forum.dao.UserMapper;
import com.knowledgeplanet.forum.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
@Slf4j
class ForumApplicationTests {

	@Test
	public void testLog () {
		log.trace("trace...");
		log.debug("debug...");
		log.info("info...");
		log.warn("warn...");
		log.error("error...");
	}

	@Resource
	private DataSource dataSource;

	@Test
	public void testConnection () throws SQLException {
		System.out.println("dataSource = " + dataSource.getClass());
		Connection connection = dataSource.getConnection();
		System.out.println("connection = " + connection);
		connection.close();
	}

	@Resource
	private UserMapper userMapper;

	@Test
	public void testMybatis () {
		User user = userMapper.selectByPrimaryKey(12l);
		System.out.println(user.toString());
		System.out.println(user.getUsername());
	}

	@Test
	void contextLoads() {
		System.out.println("TEST: 基于Spring Boot实现前后端分离的论坛系统");
	}



}
