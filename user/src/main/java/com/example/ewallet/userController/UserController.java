package com.example.ewallet.userController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ewallet.userModel.MyUser;
import com.example.ewallet.userRqstDto.UserCreateRqst;
import com.example.ewallet.userService.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("/user")
	public void createUser(@RequestBody UserCreateRqst userCreateRqst) throws JsonProcessingException {
		userService.create(userCreateRqst);
	}
	
	@GetMapping("/user")
	public MyUser getUserDetails() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MyUser myUser = (MyUser) authentication.getPrincipal();
		
		return userService.loadUserByUsername(myUser.getPhoneNumber());
	}
	
	@GetMapping("/admin/allusers")
	public List<MyUser> getAllUsers(){
		return userService.getAllUsers();
	}
	
	@GetMapping("/admin/user/{userId}")
	public MyUser getUserDetails(@PathVariable("userId") String userId) {
		return userService.loadUserByUsername(userId);
	}

}
