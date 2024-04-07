package com.test.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@SequenceGenerator(name = "sequence", sequenceName = "MY_CUSTOM_SEQ", allocationSize=1)
public class Client implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
	private Long id;

	@Column (nullable = false)
	private String name;

	@Column (nullable = false, name = "DATE_OF_BIRTH")
	private LocalDate birthdate;

	@Column (nullable = false)
	@Size(min = 8, max = 500, message = "Длина пароля вне диапазона [8 - 500]")
	private String password;

	@Override
	public String toString() {
		return "Client{" +
		"id=" + id +
		", name='" + name + '\'' +
		", birthdate=" + birthdate +
		'}';
	}
}