package com.test.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	@JsonFormat(pattern = "dd.MM.yyyy")
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private LocalDate birthdate;

	@Size(min = 8, max = 500, message = "Длина пароля вне диапазона [8 - 500]")
	private String password;
}