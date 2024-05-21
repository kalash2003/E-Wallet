package com.example.ewallet.walletModel;

import com.example.ewallet.utilsEnums.UserIdentifier;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wallet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private Long userId;
	
	private String phoneNumber;
	
	private Double balance;
	
	@Enumerated(value = EnumType.STRING)
	private UserIdentifier userIdentifier;
	
	private String identifierValue;

}
