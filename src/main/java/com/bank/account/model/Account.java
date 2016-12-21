package com.bank.account.model;


import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.transaction.Transactional;

/**
 * This class represents an account. 
 * 
 * @author Julien COURTOIS
 *
 */
@Entity
@Transactional
@Table(name="ACCOUNT")
public class Account {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ACCOUNT_ID")
	private String id;

	@Column(name = "ACCOUNT_NO")
	private String accountNo;

	@Column(name = "BALANCE")
	private double balance;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="ACCOUNT_ID")
	Set<Statement> statements;
	
	/**
	 * Default no arg constructor for hibernate.
	 */
	protected Account() {
		this.statements = new TreeSet<Statement>();
	}

	public Account(String accountNo) {
		this.accountNo = accountNo;
		this.statements = new TreeSet<Statement>();
	}

	public String getId() {
		return id;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Set<Statement> getStatements() {
		return statements;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Account)) {
			return false;
		}
		Account account = (Account) obj;
		return account.accountNo.equals(accountNo)
				&& account.balance == balance;
	}

}
