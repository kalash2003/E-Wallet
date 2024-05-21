package com.example.ewallet.userRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ewallet.userModel.MyUser;

public interface UserRepository extends JpaRepository<MyUser, Integer> {
	
	MyUser findByPhonenumber(String phoneNumber);

}
