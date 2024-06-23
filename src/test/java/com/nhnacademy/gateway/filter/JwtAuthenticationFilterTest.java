package com.nhnacademy.gateway.filter;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.gateway.exception.UnAuthorizedException;
import com.nhnacademy.gateway.utils.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

  @Mock
  private JwtUtil jwtUtil;
  @Mock
  private GatewayFilterChain filterChain;
  @InjectMocks
  private JwtAuthenticationFilter filter;
  private static final String BEARER_PREFIX = "Bearer ";
  private static final String USER_ID_HEADER = "X-USER-ID";
  private static final String VALID_TOKEN = "유효한 토큰";
  private static final String TEST_USER_ID = "user1";

  @Test
  @DisplayName("유효한 토큰을 가진 요청에 대해 헤더에 X-USER-ID를 추가하는 테스트")
  void jwtFilterWithValidToken() {

    when(jwtUtil.extractUserId(VALID_TOKEN)).thenReturn(TEST_USER_ID);

    MockServerHttpRequest request = MockServerHttpRequest.post("")
        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + VALID_TOKEN)
        .build();
    ServerWebExchange exchange = MockServerWebExchange.from(request);

    filter.apply(new JwtAuthenticationFilter.Config()).filter(exchange, filterChain);

    verify(filterChain, times(1)).filter(any(ServerWebExchange.class));

    String addedUserId = exchange.getRequest().getHeaders().getFirst(USER_ID_HEADER);
    assertThat(addedUserId).isEqualTo(TEST_USER_ID);
  }

  @Test
  @DisplayName("Authorization 헤더가 없는 요청에 대한 예외 발생 테스트")
  void jwtFilterWithNoAuthorizationHeader() {

    MockServerHttpRequest request = MockServerHttpRequest.post("").build();
    ServerWebExchange exchange = MockServerWebExchange.from(request);

    assertThatThrownBy(
        () -> filter.apply(new JwtAuthenticationFilter.Config()).filter(exchange, filterChain)
            .block())
        .isInstanceOf(UnAuthorizedException.class);
  }

  @Test
  @DisplayName("Bearer Prefix 없는 요청에 대한 예외 발생 테스트")
  void jwtFilterWithNoBearerPrefix() {

    MockServerHttpRequest request = MockServerHttpRequest.post("")
        .header(HttpHeaders.AUTHORIZATION, VALID_TOKEN)
        .build();
    ServerWebExchange exchange = MockServerWebExchange.from(request);

    assertThatThrownBy(
        () -> filter.apply(new JwtAuthenticationFilter.Config()).filter(exchange, filterChain)
            .block())
        .isInstanceOf(UnAuthorizedException.class);
  }

}