package com.example.ewallet.transactionModel;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.example.ewallet.utilsEnums.TransactionStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String transactionId;
	
	private String sender;
	
	private String receiver;
	
	private String message;
	
	
	private Double amount;
	
	@Enumerated(value = EnumType.STRING)
	private TransactionStatus transactionStatus;
	
	@CreationTimestamp
	private Date createdOn;
	
	@CreationTimestamp
	private Date updatedOn;

}
