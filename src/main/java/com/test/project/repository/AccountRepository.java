package com.test.project.repository;

import com.test.project.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

	@Query("SELECT a FROM Account a WHERE a.client.id = :clientId")
	List<Account> getAccountByClientId(Long clientId);
}
