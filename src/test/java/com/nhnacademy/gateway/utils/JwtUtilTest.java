package com.nhnacademy.gateway.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.nhnacademy.gateway.exception.UnAuthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JwtUtilTest {

  private JwtUtil jwtUtil;
  private String validToken;
  private String invalidToken;

  @BeforeEach
  public void setup() {
    invalidToken = "invalidToken";
    String testSecret = "dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3Q=";

    jwtUtil = new JwtUtil();
    ReflectionTestUtils.setField(jwtUtil, "secret", testSecret);

    Date now = new Date();

    validToken = Jwts.builder()
        .setHeaderParam("type","jwt")
        .claim("userId", "test")
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + 1000 * 60 * 60 * 24 * 7))
        .signWith(Keys.hmacShaKeyFor(testSecret.getBytes()), SignatureAlgorithm.HS256)
        .compact();
  }

  @Test
  @DisplayName("유효한 토큰이 주어지면 사용자 아이디를 추출하는 테스트")
  void extractUserIdWithValidToken() {
    String expectedUserId = "test";
    String actualUserId = jwtUtil.extractUserId(validToken);
    assertEquals(expectedUserId, actualUserId);
  }

  @Test
  @DisplayName("유효하지 않은 토큰에 대한 예외 발생 테스트")
  void extractUserIdWithInvalidToken() {
    assertThrows(UnAuthorizedException.class, () -> jwtUtil.extractUserId(invalidToken));
  }
}