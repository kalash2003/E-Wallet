package com.example.ewallet.walletService;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.ewallet.utils.CommonConstants;
import com.example.ewallet.utilsEnums.UserIdentifier;
import com.example.ewallet.utilsEnums.WalletUpdateStatus;
import com.example.ewallet.walletModel.Wallet;
import com.example.ewallet.walletRepo.WalletRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WalletService {
	
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	WalletRepository walletRepository;
	
	private static Logger logger = LoggerFactory.getLogger(WalletService.class);
	
	@KafkaListener(topics = CommonConstants.USER_CREATION_TOPIC, groupId = "walletConsumerGroup")
	public void createWallet(String msg) throws ParseException {
		JSONObject data = (JSONObject) new JSONParser().parse(msg);
		
		Long userId = (Long) data.get(CommonConstants.USER_CREATION_TOPIC_USERID);
		String phoneNumber = (String) data.get(CommonConstants.USER_CREATION_TOPIC_PHONENUMBER);
		String identifierKey = (String) data.get(CommonConstants.USER_CREATION_TOPIC_IDENTIFIERKEY);
		String identifierValue = (String) data.get(CommonConstants.USER_CREATION_TOPIC_IDENTIFIERVALUE);
		
		Wallet wallet = Wallet.builder()
				.userId(userId).phoneNumber(phoneNumber).identifierValue(identifierValue)
				.userIdentifier(UserIdentifier.valueOf(identifierValue)).balance(10.0).build();
		walletRepository.save(wallet);
	}
	
	@KafkaListener(topics = CommonConstants.TRANSACTION_CREATION_TOPIC, groupId = "transactionGroup")
	public void updateWalletForTransaction(String msg) throws ParseException, JsonProcessingException {
		JSONObject data = (JSONObject) new JSONParser().parse(msg);
		
		String sender = (String) data.get("sender");
		String receiver = (String) data.get("receiver");
		Double amount = (Double) data.get("amount");
		String transactionId = (String) data.get("transactionId");
		
		logger.info("Validating seneder's wallet balance: sender - {}, receiver - {}, amount - {}, transactionID - {}", 
				sender, receiver, amount, transactionId);
		
		Wallet senderWallet = walletRepository.findByPhoneNumber(sender);
		Wallet receiverWallet = walletRepository.findByPhoneNumber(receiver);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("sender", sender);
		jsonObject.put("receiver", receiver);
		jsonObject.put("amount", amount);
		jsonObject.put("transactionId", transactionId);
		
		if(senderWallet == null || receiverWallet == null || senderWallet.getBalance() <= amount) {
			jsonObject.put("walletUpdateStatus", WalletUpdateStatus.FAILED);
		}else {
			walletRepository.updateWallet(0-amount, sender);
			walletRepository.updateWallet(amount, receiver);
			jsonObject.put("walletUpdateStatus", WalletUpdateStatus.SUCCESSFUL);
		}
		
		kafkaTemplate.send(CommonConstants.WALLET_UPDATED_TOPIC, objectMapper.writeValueAsString(jsonObject));
	}

}
