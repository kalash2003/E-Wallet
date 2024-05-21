package com.example.ewallet.userConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class CommonConfig {
	
	@Bean
	PasswordEncoder getPE() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	ObjectMapper getMapper() {
		return new ObjectMapper();
	}

}
