package com.nhnacademy.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 요청과 응답 로그를 남기기 위한 전역 필터 입니다
 *
 * @author ckddms6530
 * @version 1.0
 */
@Component
@Slf4j
public class LoggingFilter implements GatewayFilter, Ordered {


  /**
   * 요청이 들어왔을 때와 해당 요청에 대한 응답 시 로그를 남기는 메서드입니다
   *
   * @param exchange ServerWebExchange
   * @param chain    GatewayFilterChain
   * @return Mono<Void>
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    ServerHttpResponse response = exchange.getResponse();

    log.info("request: {} - {} {}", request.getId(), request.getMethodValue(), request.getURI());
    return chain.filter(exchange).then(Mono.fromRunnable(
        () -> log.info("response: {} - {}", request.getId(), response.getStatusCode())));
  }

  @Override
  public int getOrder() {
    return -1;
  }
}
