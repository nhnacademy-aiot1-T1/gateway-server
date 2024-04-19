package com.nhnacademy.gateway.exception;

import com.nhnacademy.gateway.provider.HttpStatusProvider;
import org.springframework.http.HttpStatus;

/**
 * 인증되지 않은 사용자 예외.
 */
public class UnAuthorizedException extends RuntimeException implements HttpStatusProvider {

  public UnAuthorizedException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.UNAUTHORIZED;
  }
}
