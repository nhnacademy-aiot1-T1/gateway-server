package com.nhnacademy.gateway.utils;

import java.time.Clock;

public class SystemClockHolder implements ClockHolder{
  @Override
  public long getEpochSecond() {
    return Clock.systemUTC().instant().getEpochSecond();
  }
}
