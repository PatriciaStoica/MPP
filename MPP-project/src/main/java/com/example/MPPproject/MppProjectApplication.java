package com.example.MPPproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.example.MPPproject.Model.*")
@EnableJpaRepositories(basePackages = "com.example.MPPproject.Repository")
public class MppProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MppProjectApplication.class, args);
	}

}
