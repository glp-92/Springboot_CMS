package com.authserver2.api.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.authserver2.api.model.pojo.User;

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
	      return decodedJWT.getSubject();
    }
	
	public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        Instant expiration = generateExpirationTimeIn(20);  // expires in 10 min
        String token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expiration)
                .withIssuer(issuer)
                .withClaim("roles", user.getRole().name())
                .sign(algorithm);
        return token;
    }
	
	private Instant generateExpirationTimeIn(int minutes) {
        return LocalDateTime.now().plusMinutes(minutes).atZone(ZoneId.systemDefault()).toInstant();
    }
}
