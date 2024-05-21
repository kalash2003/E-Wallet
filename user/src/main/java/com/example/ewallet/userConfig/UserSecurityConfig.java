package com.example.ewallet.userConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.FormLoginBeanDefinitionParser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.ewallet.userModel.UserConstants;

@Configuration
public class UserSecurityConfig {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService) throws Exception {
		
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailsService)
				.passwordEncoder(bCryptPasswordEncoder).and().build();
		
	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable()).httpBasic(Customizer.withDefaults()).authorizeHttpRequests( 
        		authorize -> authorize.requestMatchers(HttpMethod.GET,"/user").hasAuthority(UserConstants.USER_AUTHORITY)
                .requestMatchers("/admin/allusers").hasAuthority(UserConstants.ADMIN_AUTHORITY)
                .requestMatchers("/admin/user/{userId}").hasAnyAuthority(UserConstants.ADMIN_AUTHORITY)
                .requestMatchers("/user").permitAll())
                .formLogin(formLogin -> formLogin.loginPage("/home").permitAll());
		return http.build();
    }

}