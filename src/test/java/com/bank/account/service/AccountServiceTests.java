package com.bank.account.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import com.bank.account.AccountBaseTest;
import com.bank.account.model.Account;
import com.bank.account.model.OperationType;
import com.bank.account.model.Statement;
import com.bank.account.repository.AccountRepository;
import com.bank.account.repository.StatementRepository;
import com.bank.account.service.AccountService;
import com.bank.account.service.AccountServiceImpl;
import com.bank.account.service.exception.InvalidAmountException;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
//@SpringBootTest
public class AccountServiceTests extends AccountBaseTest {

	private static final String TEST_ACCOUNT_NO = "1234";
	
	@Mock
	private AccountRepository accountRepository;
	
	@Mock
	private StatementRepository statementRepository;

	@InjectMocks
	private AccountService accountService = new AccountServiceImpl();

	@Before
	public void initialize(){
		//initialize mocks
		MockitoAnnotations.initMocks(this);
		
		//stub the accountRepository.findByAccountNo method 
		Mockito.when(accountRepository.findByAccountNo(TEST_ACCOUNT_NO)).thenReturn(new Account(TEST_ACCOUNT_NO));
	}

	@Test
	public void deposit_amount_increases_balance() {
		//Act
		accountService.deposit(TEST_ACCOUNT_NO, 1000);
		
		//Assert
		assertEquals("The balance is wrong", 1000, accountService.findByAccountNo(TEST_ACCOUNT_NO).getBalance(), DOUBLE_DELTA);
	}

	@Test
	public void deposit_amount_generates_a_statement() {
		//Arrange
		accountRepository.findByAccountNo(TEST_ACCOUNT_NO).setBalance(2000);
		
		//Act
		accountService.deposit(TEST_ACCOUNT_NO, 1000);

		//Assert
		assertEquals("The number of statements is wrong", 1, accountService.getStatements(TEST_ACCOUNT_NO).size());
		Statement statement = accountService.getStatements(TEST_ACCOUNT_NO).iterator().next();
		assertEquals("The Balance is wrong", 3000, statement.getBalance(), DOUBLE_DELTA);
		assertEquals("The Operation type is wrong", OperationType.DEPOSIT, statement.getOperationType());
		assertEquals("The Amount is wrong", 1000, statement.getAmount(), DOUBLE_DELTA);

	}

	@Test(expected = InvalidAmountException.class)
	public void deposit_negative_amount_throws_exception() {
		accountService.deposit(TEST_ACCOUNT_NO, -1000);
	}

	@Test
	public void withdraw_amount_deacreases_balance() {
		//Arrange
		accountRepository.findByAccountNo(TEST_ACCOUNT_NO).setBalance(5000);
		
		//Act
		accountService.withdraw(TEST_ACCOUNT_NO, 1000);
		
		//Assert
		assertEquals("The balance is wrong", 4000, accountService.findByAccountNo(TEST_ACCOUNT_NO).getBalance(), DOUBLE_DELTA);
	}

	@Test(expected = InvalidAmountException.class)
	public void withdraw_negative_amount_throws_exception() {
		accountService.withdraw(TEST_ACCOUNT_NO, -1000);
	}

	@Test(expected = InvalidAmountException.class)
	public void withdraw_more_than_balance_throws_exception() {
		//Arrange
		accountRepository.findByAccountNo(TEST_ACCOUNT_NO).setBalance(1000);
		
		//Act
		accountService.withdraw(TEST_ACCOUNT_NO, 2000);
	}

	@Test
	public void withdraw_amount_generates_a_statement() {
		//Arrange
		accountRepository.findByAccountNo(TEST_ACCOUNT_NO).setBalance(3000);
		
		//Act
		accountService.withdraw(TEST_ACCOUNT_NO, 1000);

		//Assert
		assertEquals("The number of statements is wrong", 1, accountService.getStatements(TEST_ACCOUNT_NO).size());
		Statement statement = accountService.getStatements(TEST_ACCOUNT_NO).iterator().next();
		assertEquals("The Balance is wrong", 2000, statement.getBalance(), DOUBLE_DELTA);
		assertEquals("The Operation type is wrong", OperationType.WITHDRAWAL, statement.getOperationType());
		assertEquals("The Amount is wrong", 1000, statement.getAmount(), DOUBLE_DELTA);

	}

}
