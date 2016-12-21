package com.bank.account.service;

import java.util.Set;

import com.bank.account.model.Account;
import com.bank.account.model.Statement;
import com.bank.account.service.exception.InvalidAmountException;

public interface AccountService {

	/**
	 * Retrieves the account identified by accountNo.
	 * 
	 * @param accountNo the accountNo
	 * @return the account, may be null if the account was not found
	 */
	Account findByAccountNo(String accountNo);
	
	/**
	 * Performs the deposit of amount on the account identified by accountNo.
	 * <p>
	 * A deposit {@link Statement} is added to the account and the balance is modified.
	 * 
	 * @param accountNo the accountNo
	 * @param amount the amount to deposit
	 * @return the generated statement, may be null if the account was not found
	 * 
	 * @throws InvalidAmountException if amount is not positive 
	 */
	Statement deposit(String accountNo, double depositAmount);
	
	/**
	 * Performs the withdraw of amount on the account identified by accountNo.
	 * <p>
	 * A withdrawal {@link Statement} is added to the account and the balance is modified.
	 * 
	 * @param accountNo the accountNo
	 * @param amount the amount to withdraw
	 * @return the generated statement, may be null if the account was not found
	 * 
	 * @throws InvalidAmountException if amount is not positive 
	 * or if amount is greater than the balance 
	 */
	Statement withdraw(String accountNo, double withdrawalAmount);

	/**
	 * Returns a set of the statements attached to the account identified by accountNo.
	 * 
	 * @param accountNo the accountNo
	 * @return the set of statements, may be null if the account was not found
	 */
	Set<Statement> getStatements(String accountNo);

	/**
	 * Creates an account.
	 * 
	 * @param accountNo the accountNo
	 * @return the created account
	 */
	Account createAccount(String accountNo);

	/**
	 * Returns the statement identified by operationId.
	 * 
	 * @param operationId the operationId
	 * @return the statement, may be null if not found
	 */
	Statement getStatement(String operationId);

}
