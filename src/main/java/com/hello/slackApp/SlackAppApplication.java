package com.hello.slackApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ServletComponentScan(basePackages = "com.hello.slackApp")
@SpringBootApplication
@EnableScheduling
public class SlackAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlackAppApplication.class, args);
	}

}
