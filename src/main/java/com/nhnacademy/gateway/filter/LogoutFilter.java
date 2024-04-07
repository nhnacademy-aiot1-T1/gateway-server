package com.nhnacademy.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.gateway.utils.ClockHolder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class LogoutFilter extends AbstractGatewayFilterFactory<LogoutFilter.Config> {

  private final ObjectMapper objectMapper;
  private final RedisTemplate<String, Object> redisTemplate;
  private final ClockHolder clockHolder;

  public LogoutFilter(ObjectMapper objectMapper, RedisTemplate<String, Object> redisTemplate, ClockHolder clockHolder) {
    super(LogoutFilter.Config.class);
    this.objectMapper = objectMapper;
    this.redisTemplate = redisTemplate;
    this.clockHolder = clockHolder;

  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      String token = findAccessToken(exchange.getRequest());
      long expirationTime = getTokenExpirationTime(token);

      ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
      valueOperations.set(token, "", expirationTime, TimeUnit.SECONDS);

      return chain.filter(exchange);
    };
  }

  private String findAccessToken(ServerHttpRequest request) {
    return Objects.requireNonNull(request.getHeaders().get("Authorization")).get(0).substring(7);
  }

  public long getTokenExpirationTime(String token) {

    String[] parts = token.split("\\.");
    Base64.Decoder decoder = Base64.getDecoder();
    String payload = new String(decoder.decode(parts[1]), StandardCharsets.UTF_8);
    long currentTime = clockHolder.getEpochSecond();
    JsonNode jsonPayload;
    try {
      jsonPayload = objectMapper.readTree(payload);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to parse payload");
    }
    long expirationTime = jsonPayload.get("exp").asLong();
    return expirationTime - currentTime;
  }



  public static class Config {

  }
}
