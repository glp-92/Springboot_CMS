package com.gateway.router.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.gateway.router.service.TokenService;


@Component
public class JwtValidationFilter extends AbstractGatewayFilterFactory<JwtValidationFilter.Config> {

    private final TokenService tokenService;

    public JwtValidationFilter(TokenService tokenService) {
        super(Config.class);
        this.tokenService = tokenService;
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String authorizationHeader = request.getHeaders().getFirst("Authorization");
            if (!shouldFilter(request)) {
                try {
                    String token = tokenService.getTokenFrom(authorizationHeader);
                    String username = tokenService.getSubjectFrom(token);
                    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                            .header("USERNAME", username)
                            .build();
                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                } catch (JWTVerificationException ex) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                }
            }
            return chain.filter(exchange);
        });
    }

    public boolean shouldFilter(ServerHttpRequest request) {
        String uri = request.getURI().getPath();
        return uri.startsWith("/login") || uri.startsWith("/register");
    }
}