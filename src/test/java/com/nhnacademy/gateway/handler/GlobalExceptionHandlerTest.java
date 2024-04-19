package com.nhnacademy.gateway.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.gateway.exception.UnAuthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  private MockServerWebExchange exchange;
  private GlobalExceptionHandler handler;


  @BeforeEach
  void setUp() {
    handler = new GlobalExceptionHandler(new ObjectMapper().registerModule(new JavaTimeModule()));
    exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/").build());
  }

  @Test
  @DisplayName("UnAuthorizedException 발생 시 상태코드 401 응답 테스트")
  void handleUnAuthorizedException() {
    Throwable throwable = new UnAuthorizedException("test");
    handler.handle(exchange, throwable).block();
    assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
  }

  @Test
  @DisplayName("기타 예외 발생 시 상태코드 500 응답 테스트")
  void handleOtherException() {
    Throwable throwable = new RuntimeException("test");
    handler.handle(exchange, throwable).block();
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getResponse().getStatusCode());
  }

}