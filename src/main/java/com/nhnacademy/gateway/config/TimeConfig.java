package com.nhnacademy.gateway.config;

import com.nhnacademy.gateway.utils.ClockHolder;
import com.nhnacademy.gateway.utils.SystemClockHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfig {
  @Bean
  public ClockHolder clockHolder() {
    return new SystemClockHolder();
  }
}
