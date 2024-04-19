package com.nhnacademy.gateway.filter;

import com.nhnacademy.gateway.exception.UnAuthorizedException;
import com.nhnacademy.gateway.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * Jwt 토큰을 추출하여 검증하고 헤더에 X-USER-ID를 추가하는 필터 클래스.
 */
@Component
public class JwtAuthenticationFilter extends
    AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

  private static final String BEARER_PREFIX = "Bearer ";
  private static final int BEARER_TOKEN_INDEX = 7;
  private static final String USER_ID_HEADER = "X-USER-ID";

  private final JwtUtil jwtUtil;

  public JwtAuthenticationFilter(JwtUtil jwtUtil) {
    super(Config.class);
    this.jwtUtil = jwtUtil;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();
      HttpHeaders headers = request.getHeaders();

      String bearerToken = extractAuthorizationHeader(headers);
      String token = extractToken(bearerToken);

      ServerHttpRequest mutatedRequest = request.mutate()
          .header(USER_ID_HEADER, jwtUtil.extractUserId(token)).build();

      ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
      return chain.filter(mutatedExchange);
    };
  }

  /**
   * Authorization 헤더를 추출하는 메서드
   *
   * @param headers HttpHeaders
   * @throws UnAuthorizedException Authorization 헤더가 없거나 값이 null 일 경우
   */
  private String extractAuthorizationHeader(HttpHeaders headers) {
    if (!headers.containsKey(HttpHeaders.AUTHORIZATION)
        || headers.getFirst(HttpHeaders.AUTHORIZATION) == null) {
      throw new UnAuthorizedException("No Authorization Header");
    }
    return headers.getFirst(HttpHeaders.AUTHORIZATION);
  }

  /**
   * Bearer 토큰에서 토큰을 추출하는 메서드
   *
   * @param bearerToken Authorization 헤더에서 추출한 토큰
   * @throws UnAuthorizedException Bearer Prefix 가 없을 경우
   */
  private String extractToken(String bearerToken) {
    if (!bearerToken.startsWith(BEARER_PREFIX)) {
      throw new UnAuthorizedException("No Bearer Token");
    }
    return bearerToken.substring(BEARER_TOKEN_INDEX);
  }

  /**
   * JwtAuthenticationFilter 설정 클래스
   */
  public static class Config {

  }

}
