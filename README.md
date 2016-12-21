# bank-account-kata
REST API using Spring


Overview
==========

This project is a possible implementation for the Bank Account kata.
It's a REST API to meet the requirements (see below).


How it works
==========

To run the application : mvn spring-boot:run (or java -jar account-1.0.0.jar)
To run the tests : mvn test

The server will start on http://localhost:8080/
Use curl or postman to query the API (the documentation is available in the doc/ folder).
The application contains an embedded H2DB. The database is created at application startup, and removed on shutdown).


Solution Stack
==========
Spring 4.3.4
Spring-boot-starter 1.4.2 (web, data-jpa, test)
H2 1.4
Hibernate 5
Maven 3.0.4
Node 4.0
apidoc 0.16


Requirement
==========

Bank account kata
Think of your personal bank account experience When in doubt, go for the simplest solution
 
Requirements
Deposit and Withdrawal
Account statement (date, amount, balance)
Statement printing
 
User Stories
US 1:
In order to save money
As a bank client
I want to make a deposit in my account
 
US 2:
In order to retrieve some or all of my savings
As a bank client
I want to make a withdrawal from my account
 
US 3:
In order to check my operations
As a bank client
I want to see the history (operation, date, amount, balance)  of my operations


Next steps
==========
Deal with concurrency in account operations
Implement error message body
Create a client to consume the REST API
