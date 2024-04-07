package com.test.project.service;


import com.test.project.entity.Account;

import java.util.List;

public interface AccountService {

	List<Account> getAccounts();

	void incrementBalance();
}