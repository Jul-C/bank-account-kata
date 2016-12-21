package com.bank.account.controller;

import java.net.URI;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bank.account.dto.AccountDTO;
import com.bank.account.model.Account;
import com.bank.account.model.Statement;
import com.bank.account.service.AccountService;

@RestController
public class AccountController {

	@Autowired
	AccountService accountService;

	/**
	 * @apiVersion 1.0.0
	 * @apiGroup Account
	 * @apiDescription Request a single account
	 * 
	 * @api {get} /accounts/:accountNo GetAccount
	 * @apiParam {String} 	accountNo	Account number
	 * 
	 * @apiSuccess (200)	{String} accountNo	The account Number
	 * @apiSuccess (200)	{Number} balance	The account balance
	 * 
	 * @apiSuccessExample Success-Response:
	 *     HTTP/1.1 200 OK
	 *     {
	 *       "accountNo": "ABCD",
	 *       "balance": "1200.23"
	 *     }
	 *     
	 * @apiError 404 ResourceNotFound The <code>accountNo</code> of the Account was not found.
	 * 
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 404 Not Found
	 */
	@RequestMapping(value = "/accounts/{accountNo}", method = RequestMethod.GET)
	public ResponseEntity<AccountDTO> getAccount(@PathVariable("accountNo") String accountNo) {
		Account account = accountService.findByAccountNo(accountNo);
		if (null == account) {
			return new ResponseEntity<AccountDTO>(HttpStatus.NOT_FOUND);
		}
		AccountDTO accountDTO = new AccountDTO(account);
		return new ResponseEntity<AccountDTO>(accountDTO, HttpStatus.OK);
	}

	/**
	 * @apiVersion 1.0.0
	 * @apiGroup Account
	 * @apiDescription Create an account
	 * 
	 * @api {post} /accounts/:accountNo CreateAccount
	 * @apiParam {String} 	accountNo	Account number to create
	 * 
	 * @apiSuccess (201)	{String} accountNo	The account Number
	 * @apiSuccess (201)	{Number} balance	The account balance
	 * 
	 * @apiSuccessExample Success-Response:
	 *     HTTP/1.1 201 OK
	 *     {
	 *       "accountNo": "ABCD",
	 *       "balance": "0"
	 *     }
	 *     
	 * @apiError 422 UnprocessableEntity The <code>accountNo</code> already exists
	 * 
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 422 Unprocessable Entity
	 */
	@RequestMapping(value = "/accounts/{accountNo}", method = RequestMethod.POST)
	public ResponseEntity<AccountDTO> createAccount(@PathVariable("accountNo") String accountNo) {
		if (null != accountService.findByAccountNo(accountNo)) {
			return new ResponseEntity<AccountDTO>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		Account account = accountService.createAccount(accountNo);
		AccountDTO accountDTO = new AccountDTO(account);

		final URI location = ServletUriComponentsBuilder
				.fromCurrentServletMapping().path("/accounts/{accountNo}").build()
				.expand(account.getAccountNo()).toUri();

		return ResponseEntity.created(location).body(accountDTO);
	}
	
	/**
	 * @apiVersion 1.0.0
	 * @apiGroup Account
	 * @apiDescription Request the list of operations attached to an account 
	 * 
	 * @api {get} /accounts/:accountNo/operations ListAccountOperations
	 * @apiParam {String} 	accountNo	Account number
	 * 
	 * @apiSuccess {Object[]} operations       			List of the operations.
	 * @apiSuccess {String}   operations.id    			Operation identifier.
	 * @apiSuccess {String}   operations.operationType 	Operation type.
	 * @apiSuccess {Date}     operations.amount			Operation amount.
	 * @apiSuccess {Date}     operations.balance		Account resulting balance.
	 * 
	 * @apiError 404 ResourceNotFound The <code>accountNo</code> of the Account was not found.
	 * 
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 404 Not Found
	 */
	@RequestMapping(value = "/accounts/{accountNo}/operations", method = RequestMethod.GET)
	public ResponseEntity<Set<Statement>> listAccountOperations(@PathVariable("accountNo") String accountNo) {
		Set<Statement> statements = accountService.getStatements(accountNo);
		if (null == statements) {
			return new ResponseEntity<Set<Statement>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Set<Statement>>(statements, HttpStatus.OK);
	}

	/**
	 * @apiVersion 1.0.0
	 * @apiGroup Account
	 * @apiDescription Request detail information on a specific account operation
	 * 
	 * @api {get} /accounts/:accountNo/operations/:operationId GetAccountOperation
	 * @apiParam {String} 	accountNo	Account number
	 * @apiParam {String} 	operationId	Operation identifier
	 *     
	 * @apiError 404 ResourceNotFound <code>accountNo</code> or <code>operationId</code> not found.
	 * 
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 404 Not Found
	 */
	@RequestMapping(value = "/accounts/{accountNo}/operations/{operationId}", method = RequestMethod.GET)
	public ResponseEntity<Statement> getAccountOperation(@PathVariable("accountNo") String accountNo, @PathVariable("operationId") String operationId) {
		Statement statement = accountService.getStatement(operationId);
		if (null == statement) {
			return new ResponseEntity<Statement>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Statement>(statement, HttpStatus.OK);
	}

	/**
	 * @apiVersion 1.0.0
	 * @apiGroup Account
	 * @apiDescription Deposit money on an account.
	 * It will generate an account operation.
	 * 
	 * @api {post} /accounts/:accountNo/deposits MakeDeposit
	 * @apiParam {String} 	accountNo	Account number
	 * 
	 * @apiSuccess (201) {Header} location		(Response Header) The location of the created operation.  
	 * @apiSuccess (201) {String} id    		Operation identifier.
	 * @apiSuccess (201) {String} operationType Operation type.
	 * @apiSuccess (201) {Date}   amount		Operation amount.
	 * @apiSuccess (201) {Date}   balance		Account resulting balance.
	 * 
	 * @apiSuccessExample Success-Response:
	 *     HTTP/1.1 201 OK
	 *     {
	 *       "id": "1",
	 *       "operationType": "DEPOSIT",
	 *       "date": 1482319651288,
	 *       "amount": 400,
	 *       "balance": 400
	 *     }
	 *     
	 * @apiError 404 ResourceNotFound The <code>accountNo</code> of the Account was not found.
	 * 
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 404 Not Found
	 *     
	 * @apiError 422 UnprocessableEntity The <code>amount</code> is negative
	 * 
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 422 Unprocessable Entity
	 */
	@RequestMapping(value = "/accounts/{accountNo}/deposits", method = RequestMethod.POST)
	public ResponseEntity<Statement> createAccountDeposit(@PathVariable("accountNo") String accountNo, @RequestParam("amount") double amount) {
		Account account = accountService.findByAccountNo(accountNo);
		if (null == account) {
			return new ResponseEntity<Statement>(HttpStatus.NOT_FOUND);
		}
		if (0 > amount) {
			return new ResponseEntity<Statement>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		Statement statement = accountService.deposit(accountNo, amount);

		final URI location = ServletUriComponentsBuilder
				.fromCurrentServletMapping().path("/accounts/{accountNo}/operations/{operationId}").build()
				.expand(accountNo, statement.getId()).toUri();
		return ResponseEntity.created(location).body(statement);
	}

	/**
	 * @apiVersion 1.0.0
	 * @apiGroup Account
	 * @apiDescription Withdraw money on an account.
	 * It will generate an account operation.
	 * 
	 * @api {post} /accounts/:accountNo/withdrawals MakeWithdraw
	 * @apiParam {String} 	accountNo	Account number
	 * 
	 * @apiSuccess (201) {Header} location		(Response Header) The location of the created operation.  
	 * @apiSuccess (201) {String} id    		Operation identifier.
	 * @apiSuccess (201) {String} operationType Operation type.
	 * @apiSuccess (201) {Date}   amount		Operation amount.
	 * @apiSuccess (201) {Date}   balance		Account resulting balance.
	 * 
	 * @apiSuccessExample Success-Response:
	 *     HTTP/1.1 201 OK
	 *     {
	 *       "id": "1",
	 *       "operationType": "WITHDRAWAL",
	 *       "date": 1482319651288,
	 *       "amount": 400,
	 *       "balance": 400
	 *     }
	 *     
	 * @apiError 404 ResourceNotFound The <code>accountNo</code> of the Account was not found.
	 * 
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 404 Not Found
	 *     
	 * @apiError 422 UnprocessableEntity The <code>amount</code> is negative, or greater than the amount balance
	 * 
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 422 Unprocessable Entity
	 */
	@RequestMapping(value = "/accounts/{accountNo}/withdrawals", method = RequestMethod.POST)
	public ResponseEntity<Statement> createAccountWhitdraw(@PathVariable("accountNo") String accountNo, @RequestParam("amount") double amount) {
		Account account = accountService.findByAccountNo(accountNo);
		if (null == account) {
			return new ResponseEntity<Statement>(HttpStatus.NOT_FOUND);
		}
		if (0 > amount || amount > account.getBalance()) {
			return new ResponseEntity<Statement>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		Statement statement = accountService.withdraw(accountNo, amount);
		final URI location = ServletUriComponentsBuilder
				.fromCurrentServletMapping().path("/accounts/{accountNo}/operations/{operationId}").build()
				.expand(accountNo, statement.getId()).toUri();
		return ResponseEntity.created(location).body(statement);
	}

}
