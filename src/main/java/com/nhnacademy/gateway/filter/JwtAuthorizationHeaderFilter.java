package com.nhnacademy.gateway.filter;

import com.nhnacademy.gateway.key.JwtProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

@Slf4j
@Component
public class JwtAuthorizationHeaderFilter extends AbstractGatewayFilterFactory<JwtAuthorizationHeaderFilter.Config> {

    private JwtProperty jwtProperty;

    public JwtAuthorizationHeaderFilter(){
        super(Config.class);
    }

    public static class Config{
        private String key;

        public String getKey(){
            return this.key;
        }

        public void setKey(String key){
            this.key=key;
        }
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.debug("jwt-validation-filter");
            ServerHttpRequest request = exchange.getRequest();

            String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bearer 토큰이 필요합니다.");
            }

            String jwtToken = authorizationHeader.substring(7);

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(jwtProperty.getSecretKey().getBytes()))
                        .build()
                        .parseClaimsJws(jwtToken)
                        .getBody();


                exchange = exchange.mutate().request(builder -> builder.header("X-USER-ID", claims.getSubject())).build();

            }catch(ExpiredJwtException e){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access Token이 만료 되었습니다.");
            }
            catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
        };
    }
}