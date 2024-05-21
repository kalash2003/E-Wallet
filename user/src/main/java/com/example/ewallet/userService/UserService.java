package com.example.ewallet.userService;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.ewallet.userModel.MyUser;
import com.example.ewallet.userModel.UserConstants;
import com.example.ewallet.userRepo.UserRepository;
import com.example.ewallet.userRqstDto.UserCreateRqst;
import com.example.ewallet.utils.CommonConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	ObjectMapper objectMapper;

	@Override
	public MyUser loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
		return userRepository.findByPhonenumber(phoneNumber);
	}
	
	public void create(UserCreateRqst userCreateRqst) throws JsonProcessingException {
		MyUser myUser = userCreateRqst.toUser();
		myUser.setAuthorities(UserConstants.USER_AUTHORITY);
		myUser = userRepository.save(myUser);
		
//		publish the event post user creation which will be listened by consumers
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(CommonConstants.USER_CREATION_TOPIC_USERID, myUser.getId());
		jsonObject.put(CommonConstants.USER_CREATION_TOPIC_PHONENUMBER, myUser.getPhoneNumber());
		jsonObject.put(CommonConstants.USER_CREATION_TOPIC_IDENTIFIERKEY, myUser.getUserIdentifier());
		jsonObject.put(CommonConstants.USER_CREATION_TOPIC_IDENTIFIERVALUE, myUser.getIdentifierValue());
		
		kafkaTemplate.send(CommonConstants.USER_CREATION_TOPIC, objectMapper.writeValueAsString(jsonObject));
	}
	
	public List<MyUser> getAllUsers() {
		return userRepository.findAll();
	}

}
