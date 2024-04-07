package com.test.project.web.controller;

import com.test.project.dto.AuthenticationRequest;
import com.test.project.dto.AuthenticationResponse;
import com.test.project.exception.ClientException;
import com.test.project.exception.ExceptionMessageCreator;
import com.test.project.service.TestUserDetailsService;
import com.test.project.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.test.project.util.Constants.INCORRECT_MAIL_OR_PASS;


@RequiredArgsConstructor
@RestController
@Tag(name="Аутентификация и формирование JWT")
public class AuthenticationController {

	private final AuthenticationManager authenticationManager;
	private final ExceptionMessageCreator messageCreator;
	private final JwtUtil jwtTokenUtil;
	private final TestUserDetailsService userDetailsService;

	@PostMapping("/authenticate")
	@Operation(summary = "Аутентификация и формирование JWT в теле отклика")
	@SecurityRequirement(name = "JWT")
	public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws ClientException {

		try {
			/*способ аутентификации определен в WebSecurityConfig.configure(AuthenticationManagerBuilder auth) */
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getMail(), authenticationRequest.getPassword())
				);
		}
		catch (BadCredentialsException e) {
			throw ClientException.of(messageCreator.createMessage(INCORRECT_MAIL_OR_PASS));
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getMail());

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}