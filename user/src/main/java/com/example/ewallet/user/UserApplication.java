package com.example.ewallet.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.ewallet.userModel.MyUser;
import com.example.ewallet.userModel.UserConstants;
import com.example.ewallet.userRepo.UserRepository;
import com.example.ewallet.utilsEnums.UserIdentifier;

@SpringBootApplication
public class UserApplication implements CommandLineRunner{
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserRepository userRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		MyUser txnServiceUser = MyUser.builder().phoneNumber("txn_service")
				.password(passwordEncoder.encode("txn123"))
				.authorities(UserConstants.SERVICE_AUTHORITY)
				.email("txn@gmail.com").userIdentifier(UserIdentifier.SERVICE_ID)
				.identifierValue("txn123").build();
		userRepository.save(txnServiceUser);
	}

}
