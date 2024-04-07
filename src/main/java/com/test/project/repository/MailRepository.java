package com.test.project.repository;

import com.test.project.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailRepository extends JpaRepository<Mail, Long> {

	Optional<Mail> getFirstByMail (String mail);
}