package com.gateway.router.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

	@Value("${secret.key}")
    private String secret;
	
	@Value("${issuer}")
    private String issuer;
	
	public String getTokenFrom(String bearerToken) {
        final String bearer = "Bearer ";
        if (bearerToken == null || !bearerToken.startsWith(bearer))
            throw new JWTVerificationException("Invalid Authorization Header");
        String token = bearerToken.substring(bearer.length());
        return token;
    }
	
	public String getSubjectFrom(String token) {
		Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT decodedJWT = verifier.verify(token);  // throws JWTVerificationException if not valid
		String subject = decodedJWT.getSubject();
		return subject;
    }
}
