package com.bank.account.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bank.account.model.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

	Account findByAccountNo(String accountNo);

}