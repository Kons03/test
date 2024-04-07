package com.test.project.service;

import com.test.project.entity.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TestUserDetailsService implements UserDetailsService {

	private final ClientService clientService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

    	Client client = clientService.getClientByMail(s);
        return new User(s, client.getPassword(), new ArrayList<>());
    }
}