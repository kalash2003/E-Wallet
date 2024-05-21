package com.example.ewallet.userRqstDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.ewallet.userModel.MyUser;
import com.example.ewallet.utilsEnums.UserIdentifier;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserCreateRqst {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String phoneNumber;
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String password;
	
	private String dob;
	
	private String authorities;
	
	@NotBlank
	private UserIdentifier userIdentifier;
	
	@NotBlank
	private String identifierValue;
	
	public MyUser toUser() {
		return MyUser.builder().name(name).phoneNumber(phoneNumber).email(email)
				.password(passwordEncoder.encode(password)).dob(dob).authorities(authorities)
				.userIdentifier(userIdentifier).identifierValue(identifierValue).build();
		
	}

}
