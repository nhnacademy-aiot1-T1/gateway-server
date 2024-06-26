package com.nhnacademy.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 요청과 응답 로그를 남기기 위한 전역 필터.
 */
@Component
@Slf4j
public class LoggingFilter implements GlobalFilter, Ordered {


  /**
   * 요청이 들어왔을 때와 해당 요청과 요청에 대한 응답 로그를 남기는 메서드
   *
   * @param exchange ServerWebExchange
   * @param chain    GatewayFilterChain
   * @return Mono<Void>
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    ServerHttpResponse response = exchange.getResponse();

    log.info("Request ID - {} : request : {} - {}", request.getId(), request.getMethodValue(),
        request.getURI());
    return chain.filter(exchange).then(Mono.fromRunnable(
        () -> log.info("Request ID - {} : response : {}", request.getId(),
            response.getStatusCode())));
  }

  @Override
  public int getOrder() {
    return -1;
  }
}
