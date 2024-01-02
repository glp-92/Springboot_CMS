package com.authserver2.api.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.authserver2.api.exceptions.AuthenticationFailedException;
import com.authserver2.api.model.pojo.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
	
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public String authenticate(String username, String password) {
        try {
            // The authentication manager provides secure authentication and throws exception if it fails
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authenticate = authenticationManager.authenticate(authToken);
            User user = (User) authenticate.getPrincipal();
            String token = tokenService.generateToken(user);
            return token;
        } catch (AuthenticationException e) {
            throw new AuthenticationFailedException("Invalid User or Password");
        }
    }

}