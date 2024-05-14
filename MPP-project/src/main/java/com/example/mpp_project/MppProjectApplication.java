package com.example.mpp_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example.mpp_project")
@EntityScan(basePackages = "com.example.mpp_project.Model")
@EnableJpaRepositories(basePackages = "com.example.mpp_project.Repository")
public class MppProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MppProjectApplication.class, args);
	}

}
