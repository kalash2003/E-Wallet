package com.example.ewallet.transactionRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.ewallet.transactionModel.Transaction;
import com.example.ewallet.utilsEnums.TransactionStatus;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	@Modifying
	@Query("update Transaction t set t.transactionStatus = ?2 where transactionId = ?1")
	void updateTransaction(String transactionId, TransactionStatus transactionStatus);

}
