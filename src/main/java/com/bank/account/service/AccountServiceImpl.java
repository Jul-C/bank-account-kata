package com.bank.account.service;

import java.util.Date;
import java.util.Set;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.account.model.Account;
import com.bank.account.model.OperationType;
import com.bank.account.model.Statement;
import com.bank.account.repository.AccountRepository;
import com.bank.account.repository.StatementRepository;
import com.bank.account.service.exception.InvalidAmountException;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	@Autowired 
	private AccountRepository accountRepository;

	@Autowired 
	private StatementRepository statementRepository;
	
	/** {@inheritDoc} */
	public Account findByAccountNo(String accountNo) {
		return accountRepository.findByAccountNo(accountNo);
	}

	/** {@inheritDoc} */
	public Statement deposit(String accountNo, double amount) {
		if (0 >= amount) {
			throw new InvalidAmountException();
		}
		Account account = findByAccountNo(accountNo);
		if (null == account) {
			return null;
		}
		Statement statement = performOperation(account, OperationType.DEPOSIT, amount);
		statementRepository.save(statement);
		accountRepository.save(account);
		System.out.println("statement.getId()" + statement.getId());
		return statement;
	}

	/** {@inheritDoc} */
	public Statement withdraw(String accountNo, double amount) {
		if (0 >= amount) {
			throw new InvalidAmountException();
		}
		Account account = findByAccountNo(accountNo);
		if (null == account) {
			return null;
		}
		if (account.getBalance() < amount) {
			throw new InvalidAmountException();
		}
		Statement statement = performOperation(account, OperationType.WITHDRAWAL, amount);
		statementRepository.save(statement);
		accountRepository.save(account);
		return statement;
	}

	/** {@inheritDoc} */
	public Set<Statement> getStatements(String accountNo) {
		Account account = findByAccountNo(accountNo);
		if (null == account) {
			return null;
		}
		return account.getStatements();
	}

	/** {@inheritDoc} */
	public Statement getStatement(String operationId) {
		return statementRepository.findOne(operationId);
	}

	/** {@inheritDoc} */
	public Account createAccount(String accountNo) {
		Account account = new Account(accountNo);
		return accountRepository.save(account);
	}

	private Statement performOperation(Account account, OperationType operationType, double amount) {
		double balance = account.getBalance();
		//apply the amount to the balance
		balance += amount * operationType.getMultiplcator();
		Statement statement = new Statement(operationType, new Date(), amount, balance);
		account.getStatements().add(statement);
		account.setBalance(balance);
		return statement;
	}

}
