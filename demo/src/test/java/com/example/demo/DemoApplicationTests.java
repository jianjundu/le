package com.example.demo;

import com.example.demo.dto.User;
import com.example.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan("com.example.demo.dao")
public class DemoApplicationTests {


	@Resource
	private UserService userService;


	@Test
	public void contextLoads() {
	}



	@Test
	public void add(){
		User user = new User();
		user.setAge(1);
		user.setName("张三");
		this.userService.add(user);
	}

}
