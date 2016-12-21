package com.bank.account.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class represents a statement. 
 * 
 * @author Julien COURTOIS
 *
 */
@Entity
@Table(name="STATEMENT")
public class Statement implements Comparable<Statement> {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "STATEMENT_ID")
	private String id;

	@Column(name = "OPERATION_TYPE")
	private OperationType operationType;
	
	@Column(name = "OPERATION_DATE")
	private Date date;

	@Column(name = "AMOUNT")
	private double amount;

	@Column(name = "BALANCE")
	private double balance;
	
	/**
	 * Default no arg constructor for hibernate.
	 */
	protected Statement() {}
	
	public Statement(OperationType operationType, Date date, double amount,
			double balance) {
		super();
		this.operationType = operationType;
		this.date = date;
		this.amount = amount;
		this.balance = balance;
	}
	
	public String getId() {
		return id;
	}

	public OperationType getOperationType() {
		return operationType;
	}
	
	public Date getDate() {
		return date;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public double getBalance() {
		return balance;
	}

	public int compareTo(Statement other) {
		return this.getDate().compareTo(other.getDate());
	}
	
}
