package com.bank.account.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bank.account.model.Statement;

@Repository
public interface StatementRepository extends CrudRepository<Statement, String> {

}