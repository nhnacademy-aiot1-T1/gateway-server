package com.nhnacademy.gateway;

import com.nhnacademy.gateway.filter.JwtAuthorizationHeaderFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GatewayApplicationTests {
  
  @Test
  void contextLoads() {
    Assertions.assertDoesNotThrow(() -> {
    });
  }

}
