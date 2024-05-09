package com.nhnacademy.gateway.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.common.dto.CommonResponse;
import com.nhnacademy.gateway.provider.HttpStatusProvider;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Gateway 에서 발생하는 예외를 처리하는 핸들러
 */
@Slf4j
@Order(Integer.MIN_VALUE)
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

  private static final String DEFAULT_ERROR_MESSAGE = "Gateway Error";
  private final ObjectMapper objectMapper;

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
    ServerHttpResponse response = exchange.getResponse();
    HttpStatus httpStatus = determineHttpStatus(throwable);
    String error = DEFAULT_ERROR_MESSAGE;

    log.debug("handle exception : {}", throwable.getMessage());

    try {
      error = objectMapper.writerWithDefaultPrettyPrinter()
          .writeValueAsString(CommonResponse.fail(throwable.getMessage()));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
      httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    response.setStatusCode(httpStatus);

    byte[] bytes = error.getBytes(StandardCharsets.UTF_8);
    DataBuffer buffer = response.bufferFactory().wrap(bytes);

    return exchange.getResponse().writeWith(Mono.just(buffer));
  }


  /**
   * 필터 내에서 발생한 예외에 따라 HttpStatus 를 반환
   *
   * @param throwable 예외
   * @return HttpStatus
   */
  private HttpStatus determineHttpStatus(Throwable throwable) {
    if (throwable instanceof ResponseStatusException) {
      return ((ResponseStatusException) throwable).getStatus();
    }
    if (throwable instanceof HttpStatusProvider) {
      return ((HttpStatusProvider) throwable).getHttpStatus();
    }
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }

}