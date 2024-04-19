package com.nhnacademy.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.gateway.handler.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Gateway 예외 처리 설정
 * GlobalExceptionHandler 를 Bean 으로 등록.
 */
@Configuration
@RequiredArgsConstructor
public class ExceptionConfig {

  private final ObjectMapper objectMapper;

  @Bean
  public ErrorWebExceptionHandler globalExceptionHandler() {
    return new GlobalExceptionHandler(objectMapper);
  }
}