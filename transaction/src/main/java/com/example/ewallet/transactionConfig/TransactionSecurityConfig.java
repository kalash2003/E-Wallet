package com.example.ewallet.transactionConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.ewallet.transactionService.TransactionService;

@Configuration
public class TransactionSecurityConfig {
	
	@Autowired
	TransactionService transactionService;
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService) throws Exception {
		
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(transactionService)
				.passwordEncoder(bCryptPasswordEncoder).and().build();
		
	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable()).httpBasic(Customizer.withDefaults()).authorizeHttpRequests( 
        		authorize -> authorize.requestMatchers("/transact/**").hasAuthority("usr"))
                .formLogin(formLogin -> formLogin.loginPage("/home").permitAll());
		return http.build();
    }

}
