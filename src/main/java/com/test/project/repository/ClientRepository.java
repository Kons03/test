package com.test.project.repository;

import com.test.project.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ClientRepository extends JpaRepository<Client, Long> {

	Page<Client> findByNameContainingIgnoreCaseOrderByName(Pageable pageable, String name);

	Page<Client> findByBirthdateAfterOrderByBirthdate(Pageable pageable, LocalDate birthdate);
}