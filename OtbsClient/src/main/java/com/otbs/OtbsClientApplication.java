package com.otbs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class OtbsClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(OtbsClientApplication.class, args);
	}

}
