package com.bank.account.dto;

import com.bank.account.model.Account;

public class AccountDTO {
	
	private String accountNo;

	private double balance;

	public AccountDTO(Account account) {
		super();
		this.accountNo = account.getAccountNo();
		this.balance = account.getBalance();
	}
	
	
	public String getAccountNo() {
		return accountNo;
	}
	
	public double getBalance() {
		return balance;
	}

}
