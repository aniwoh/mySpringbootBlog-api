package org.aniwoh.myspringbootblogapi;

import cn.dev33.satoken.SaManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "org.aniwoh.myspringbootblogapi.Repository")
public class MySpringbootBlogApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySpringbootBlogApiApplication.class, args);
		System.out.println("启动成功，Sa-Token 配置如下：" + SaManager.getConfig());
	}

}
