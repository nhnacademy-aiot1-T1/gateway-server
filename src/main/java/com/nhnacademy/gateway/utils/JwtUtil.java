package com.nhnacademy.gateway.utils;

import com.nhnacademy.gateway.exception.UnAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * jwt 을 다루는 유틸리티 클래스.
 */
@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;
  private static final String USER_ID_KEY = "userId";

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes());
  }

  /**
   * jwt 토큰에서 사용자 아이디를 추출하는 메서드.
   *
   * @param token jwt 토큰
   * @return 사용자 아이디
   */
  public String extractUserId(String token) {
    return extractClaim(token, (claims -> String.valueOf(claims.get(USER_ID_KEY, Long.class))));
  }

  /**
   * jwt 토큰에서 클레임을 추출하는 메서드.
   *
   * @param token          jwt 토큰
   * @param claimsResolver 특정 클레임을 추출하는 함수
   * @return T
   */
  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * jwt 토큰에서 모든 클레임을 추출하는 메서드.
   *
   * @param token String
   * @return Claims
   */
  private Claims extractAllClaims(String token) {
    try {
      return Jwts
          .parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (JwtException e) {
      throw new UnAuthorizedException(e.getMessage());
    }
  }
}
