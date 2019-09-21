package com.flowdaq.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories("com.flowdaq.app.repository")
public class FlowdaqDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowdaqDashboardApplication.class, args);
	}

}
