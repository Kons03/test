package com.test.project.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.test.project.dto.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

	@ExceptionHandler(ClientException.class)
	public ResponseEntity<ExceptionResponseDto> handleClientException(ClientException e) {
		return ResponseEntity
		.status(HttpStatus.NOT_FOUND)
		.body(new ExceptionResponseDto(e.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		return ResponseEntity
		.status(HttpStatus. NOT_ACCEPTABLE)
		.body(new ExceptionResponseDto(e.getMessage()));
	}

	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<ExceptionResponseDto> handleInvalidFormatException(InvalidFormatException e) {
		return ResponseEntity
		.status(HttpStatus. NOT_ACCEPTABLE)
		.body(new ExceptionResponseDto(e.getMessage()));
	}
}