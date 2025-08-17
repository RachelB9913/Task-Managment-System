package com.rachel.taskManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.rachel.taskManager.config.CognitoProperties;


@SpringBootApplication
@EnableConfigurationProperties(CognitoProperties.class)
public class TaskManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagerApplication.class, args);
	}

}
