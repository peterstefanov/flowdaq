package com.flowdaq.app.config;

import org.joda.time.DateTimeZone;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.flowdaq.app.service.date.DateService;
import com.flowdaq.app.service.date.DateServiceImpl;

@Configuration
public class DateServiceConfig {

	@Bean
	DateService dateService() {
		return new DateServiceImpl(defaultTimeZone());
	}

	@Bean
	DateTimeZone defaultTimeZone() {
		return DateTimeZone.UTC;
	}
}
