package com.test.project.service.impl;

import com.test.project.entity.Account;
import com.test.project.repository.AccountRepository;
import com.test.project.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;

	private final BigDecimal balanceRaiseLimit = BigDecimal.valueOf(2.07);
	private final BigDecimal coefficient = BigDecimal.valueOf(1.1);

	private List<BigDecimal> accountsToReach= new ArrayList<>();

	public AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
		initAccount();
	}

	private void initAccount(){
		List<Account> initAccounts = getAccounts();
		accountsToReach = initAccounts.stream()
		.map(account -> account.getBalance().multiply(balanceRaiseLimit))
		.collect(Collectors.toList());
		initAccounts.forEach(account->
		log.info("client ID - {}, initial  balance - {},  started at - {}", account.getClient().getId(), account.getBalance().setScale(2, RoundingMode.HALF_UP), formatEventDate()));
	}

	@Override
	public List<Account> getAccounts() {
		return accountRepository.findAll();
	};

	@Scheduled(fixedRateString = "${scheduler.interval}", initialDelayString =  "${scheduler.interval}")
	@Async
	@Override
	public void incrementBalance() {

		List<Account> accounts = getAccounts();

		accounts.forEach(account-> {
			BigDecimal newBalance = account.getBalance().multiply(coefficient);
			if (!(newBalance.compareTo(accountsToReach.get(accounts.indexOf(account))) > 0)){
				account.setBalance(newBalance);
				log.info("client ID - {}, current  balance - {},  changed at - {}", account.getClient().getId(), account.getBalance().setScale(2, RoundingMode.HALF_UP), formatEventDate());
			}
		});
	}

	private  String formatEventDate(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
		return formatter.format(LocalDateTime.now());
	}
}