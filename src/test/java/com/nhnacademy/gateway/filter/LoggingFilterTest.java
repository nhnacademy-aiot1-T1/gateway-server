package com.nhnacademy.gateway.filter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class LoggingFilterTest {

  @Mock
  private GatewayFilterChain filterChain;
  private LoggingFilter loggingFilter;

  @BeforeEach
  public void setup() {
    loggingFilter = new LoggingFilter();
  }

  @Test
  @DisplayName("Request, Response 로깅")
  void loggingFilterRequestAndResponse() {
    MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.GET, "test-url")
        .build();
    ServerWebExchange exchange = MockServerWebExchange.from(request);
    when(filterChain.filter(exchange)).thenReturn(Mono.empty());

    loggingFilter.filter(exchange, filterChain).block();

    verify(filterChain, times(1)).filter(exchange);
  }

}