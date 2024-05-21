package com.example.ewallet.transactionService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.kafka.common.Uuid;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

import com.example.ewallet.transactionModel.Transaction;
import com.example.ewallet.transactionRepo.TransactionRepository;
import com.example.ewallet.utils.CommonConstants;
import com.example.ewallet.utilsEnums.TransactionStatus;
import com.example.ewallet.utilsEnums.WalletUpdateStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransactionService implements UserDetailsService{
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	TransactionRepository transactionRepository;

	public String initiateTransaction(String username, String receiver, String message, Double amount) throws JsonProcessingException {
		
		Transaction transaction = Transaction.builder().sender(username).receiver(receiver)
				.amount(amount).transactionId(Uuid.randomUuid().toString())
				.transactionStatus(TransactionStatus.PENDING).build();
		transactionRepository.save(transaction);
		
//		publish the event after initiating the Transaction
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("sender", username);
		jsonObject.put("receiver", receiver);
		jsonObject.put("amount", amount);
		jsonObject.put("transactionId", transaction.getTransactionId());
		
		kafkaTemplate.send(CommonConstants.TRANSACTION_CREATION_TOPIC, objectMapper.writeValueAsString(jsonObject));
		return transaction.getTransactionId();
	}
	
	@KafkaListener(topics = CommonConstants.WALLET_UPDATED_TOPIC, groupId = "transactionGroup")
	public void updateTransaction(String msg) throws ParseException, JsonProcessingException {
		JSONObject data = (JSONObject) new JSONParser().parse(msg);
		
		String sender = (String) data.get("sender");
		String receiver = (String) data.get("receiver");
		Double amount = (Double) data.get("amount");
		String transactionId = (String) data.get("transactionId");
		WalletUpdateStatus walletUpdateStatus = WalletUpdateStatus.valueOf((String) data.get("walletUpdateStatus"));
		
		JSONObject senderObj = getUserFromUserService(sender);
		String senderEmail = (String) senderObj.get("email");
		String receiverEmail;
		
		if (walletUpdateStatus == WalletUpdateStatus.SUCCESSFUL) {
			JSONObject receiverObj = getUserFromUserService(receiver);
			receiverEmail = (String) receiverObj.get("email");
			transactionRepository.updateTransaction(transactionId, TransactionStatus.SUCCESSFUL);
			
			String receiverMesg = "Hi, You have reveived Rs."+amount+" from "+sender+
					" in your wallet linked with phone number"+receiver;
			
			JSONObject receiverEmailObject = new JSONObject();
			receiverEmailObject.put("Email", receiverEmail);
			receiverEmailObject.put("msg", receiverMesg);
			kafkaTemplate.send(CommonConstants.TRANSACTION_COMPLETION_TOPIC, objectMapper.writeValueAsString(receiverEmailObject));
			
		}else {
			transactionRepository.updateTransaction(transactionId, TransactionStatus.FAILED);
		}
		
		String senderMesg = "Hi, Your Transaction with ID "+transactionId+" got"+walletUpdateStatus;
		
		JSONObject senderEmailObject = new JSONObject();
		senderEmailObject.put("Email", senderEmail);
		senderEmailObject.put("msg", senderMesg);
		
		kafkaTemplate.send(CommonConstants.TRANSACTION_COMPLETION_TOPIC, objectMapper.writeValueAsString(senderEmailObject));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		JSONObject requestedUser = getUserFromUserService(username);
		
		List<GrantedAuthority> authorities;
		List<LinkedHashMap<String, String>> requestedAuthorities = (List<LinkedHashMap<String, String>>) requestedUser.get("authorities");
		authorities = requestedAuthorities.stream().map(x -> x.get("authority")).map(x -> new SimpleGrantedAuthority(x))
				.collect(Collectors.toList());
		return new User((String)requestedUser.get("username"), (String)requestedUser.get("password"), authorities);
	}
	
	private JSONObject getUserFromUserService(String username) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBasicAuth("txn_service", "txn123");
		HttpEntity request = new HttpEntity(httpHeaders);
		
		return restTemplate.exchange("http:://localhost:6001/admin/user/"+username, HttpMethod.GET, 
				request, JSONObject.class).getBody();
		
	}

}
