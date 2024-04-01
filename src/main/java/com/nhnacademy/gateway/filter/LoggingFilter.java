package com.nhnacademy.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

  public LoggingFilter() {
    super(Config.class);
  }

  public static class Config {
    boolean preLogger;
    boolean postLogger;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();
      ServerHttpResponse response = exchange.getResponse();
      if (config.preLogger) {
        log.info("pre filter: request id -> {}", request.getId());
      }
      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            if (config.postLogger) {
              log.info("response: {} - {}", request.getId(), response.getStatusCode());
            }
          }
      ));
    };
  }

}
