package com.dss.spring_demo_order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class SpringDemoOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDemoOrderApplication.class, args);
	}

}
