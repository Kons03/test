package com.test.project.repository;

import com.test.project.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

	Optional<Phone> getFirstByPhone (String phone);
}