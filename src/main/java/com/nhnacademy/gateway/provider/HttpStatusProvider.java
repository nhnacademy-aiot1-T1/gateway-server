package com.nhnacademy.gateway.provider;

import org.springframework.http.HttpStatus;

/**
 * HTTP 상태 코드 제공 인터페이스.
 */
public interface HttpStatusProvider {
  HttpStatus getHttpStatus();
}