package com.example.ewallet.walletRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.ewallet.walletModel.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {

	Wallet findByPhoneNumber(String receiver);
	
	@Modifying
	@Query("update wallet w set w.balance = w.balance+ ?1 where w.phoneNumber = ?2")
	void updateWallet(Double amount, String phoneNumber);

}
