package com.bank.account.controller;

import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bank.account.model.Account;
import com.bank.account.model.OperationType;
import com.bank.account.model.Statement;
import com.bank.account.service.AccountService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class AccountControllerTests {

	private MockMvc mockMvc;

	@Mock
	private AccountService accountService;

	@InjectMocks
	private AccountController accountController;
	
	//test data
	private final static Statement TEST_DEPOSIT = new Statement(OperationType.DEPOSIT, new Date(), 1000, 1000);
	private final static Statement TEST_WITHDRAWAL = new Statement(OperationType.WITHDRAWAL, new Date(), 1000, 2000);


	@Before
	public void initialize(){
		//instantiate and register the controller
		mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
		//mock the service
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void get_account() throws Exception {
		Mockito.when(accountService.findByAccountNo("exists")).thenReturn(new Account("exists"));
		mockMvc.perform(get("/accounts/exists")).andExpect(status().isOk());
	}

	@Test
	public void get_account_not_found() throws Exception {
		Mockito.when(accountService.findByAccountNo("notExists")).thenReturn(null);
		mockMvc.perform(get("/accounts/notExists")).andExpect(status().isNotFound());
	}

	@Test
	public void create_account() throws Exception {
		Mockito.when(accountService.createAccount("new")).thenReturn(new Account("new"));
		mockMvc.perform(post("/accounts/new")).andExpect(status().isCreated());
	}

	@Test
	public void create_account_already_exists() throws Exception {
		Mockito.when(accountService.findByAccountNo("already_exists")).thenReturn(new Account("already_exists"));
		mockMvc.perform(post("/accounts/already_exists")).andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void list_operations() throws Exception {
		Account account = new Account("exists");
		account.getStatements().add(TEST_DEPOSIT);
		
		Mockito.when(accountService.findByAccountNo("exists")).thenReturn(account);
		Mockito.when(accountService.getStatements("exists")).thenReturn(account.getStatements());

		ResultActions actions = mockMvc.perform(get("/accounts/exists/operations"));
		actions.andExpect(jsonPath("$.[*].id").exists());
		actions.andExpect(jsonPath("$.[*].operationType").exists());
		actions.andExpect(jsonPath("$.[*].date").exists());
		actions.andExpect(jsonPath("$.[*].amount").exists());
		actions.andExpect(jsonPath("$.[*].balance").exists());
		actions.andExpect(status().isOk());
	}

	@Test
	public void list_operations_account_not_found() throws Exception {
		Mockito.when(accountService.getStatements("exists")).thenReturn(null);
		ResultActions actions = mockMvc.perform(get("/accounts/exists/operations"));
		actions.andExpect(status().isNotFound());
	}

	@Test
	public void get_operation() throws Exception {
		Mockito.when(accountService.getStatement("1")).thenReturn(TEST_DEPOSIT);
		ResultActions actions = mockMvc.perform(get("/accounts/exists/operations/1"));
		actions.andExpect(status().isOk());
	}

	@Test
	public void get_operation_not_found() throws Exception {
		Mockito.when(accountService.getStatement("1")).thenReturn(null);
		ResultActions actions = mockMvc.perform(get("/accounts/exists/operations/1"));
		actions.andExpect(status().isNotFound());
	}

	@Test
	public void make_deposit() throws Exception {
		Mockito.when(accountService.findByAccountNo("dAccount")).thenReturn(new Account("dAccount"));
		Mockito.when(accountService.deposit("dAccount", 1000)).thenReturn(TEST_DEPOSIT);
		ResultActions actions = mockMvc.perform(post("/accounts/dAccount/deposits?amount=1000"));
		actions.andExpect(status().isCreated());
	}

	@Test
	public void make_deposit_negative_amount() throws Exception {
		Mockito.when(accountService.findByAccountNo("dAccount")).thenReturn(new Account("dAccount"));
		ResultActions actions = mockMvc.perform(post("/accounts/dAccount/deposits?amount=-1000"));
		actions.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void make_deposit_account_not_found() throws Exception {
		Mockito.when(accountService.findByAccountNo("not_found")).thenReturn(null);
		ResultActions actions = mockMvc.perform(post("/accounts/not_found/deposits?amount=1000"));
		actions.andExpect(status().isNotFound());
	}

	@Test
	public void make_withdrawal() throws Exception {
		String accountName = "wAccount";
		Account account = new Account(accountName);
		account.setBalance(3000);
		Mockito.when(accountService.findByAccountNo(accountName)).thenReturn(account);
		Mockito.when(accountService.withdraw(accountName, 1000)).thenReturn(TEST_WITHDRAWAL);
		ResultActions actions = mockMvc.perform(post("/accounts/"+accountName+"/withdrawals?amount=1000"));
		actions.andExpect(status().isCreated());
		actions.andExpect(header().string("Location", 
				Matchers.containsString("/accounts/"+accountName+"/operations/")));
	}

	@Test
	public void make_withdrawal_negative_amount() throws Exception {
		String accountName = "wAccount";
		Account account = new Account(accountName);
		account.setBalance(3000);
		Mockito.when(accountService.findByAccountNo(accountName)).thenReturn(new Account(accountName));
		ResultActions actions = mockMvc.perform(post("/accounts/"+accountName+"/withdrawals?amount=-1000"));
		actions.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void make_withdrawal_greater_than_balance() throws Exception {
		String accountName = "wAccount";
		Account account = new Account(accountName);
		account.setBalance(3000);
		Mockito.when(accountService.findByAccountNo(accountName)).thenReturn(new Account(accountName));
		ResultActions actions = mockMvc.perform(post("/accounts/"+accountName+"/withdrawals?amount=5000"));
		actions.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void make_withdrawal_account_not_found() throws Exception {
		Mockito.when(accountService.findByAccountNo("not_found")).thenReturn(null);
		ResultActions actions = mockMvc.perform(post("/accounts/not_found/withdrawals?amount=1000"));
		actions.andExpect(status().isNotFound());
	}

}
