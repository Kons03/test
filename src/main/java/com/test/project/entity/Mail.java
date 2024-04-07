package com.test.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name  = "email_data")
@Setter
@Getter
@NoArgsConstructor
public class Mail {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne  (fetch = FetchType.EAGER)
	@JoinColumn(name = "client_id")
	private Client client;

	@Email
	private String mail;
}