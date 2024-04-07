package com.nhnacademy.gateway.filter;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.gateway.utils.ClockHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

@ExtendWith(MockitoExtension.class)
class LogoutFilterTest {
  @InjectMocks
  private LogoutFilter logoutFilter;
  @Mock
  private RedisTemplate<String, Object> redisTemplate;
  @Spy
  private ObjectMapper objectMapper;
  @Spy
  public ClockHolder clockHolder = new TestClockHolder();

  @Test
  @DisplayName("토큰에서 만료 시간을 가져오기")
  void getTokenExpirationTime() {
    String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNzE2MjM5MDIyLCJleHAiOjE3MTI0MTQ3ODJ9.uPQ8aHjfFh-gr1CpBiUsnpGhKBKfNNSHD_FYb3zZPtE";
    long exceptedTime = 1712410000;
    assertEquals(exceptedTime, logoutFilter.getTokenExpirationTime(token));
  }

  static class TestClockHolder implements ClockHolder {
    @Override
    public long getEpochSecond() {
      return 4782;
    }
  }
}