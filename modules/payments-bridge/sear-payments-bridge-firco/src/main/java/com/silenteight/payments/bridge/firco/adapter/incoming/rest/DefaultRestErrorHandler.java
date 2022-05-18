package com.silenteight.payments.bridge.firco.adapter.incoming.rest;

import com.silenteight.payments.bridge.firco.dto.output.AckDto;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.Nonnull;

@ControllerAdvice
public class DefaultRestErrorHandler extends ResponseEntityExceptionHandler {

  @Nonnull
  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status,
      WebRequest request) {

    if (status.is4xxClientError()) {
      return ResponseEntity.badRequest().body(AckDto.clientError(ex.getMessage()));
    }
    return ResponseEntity.ok(AckDto.serverError(ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<AckDto> handleError(Exception e) {
    return ResponseEntity.ok(AckDto.serverError(e.getMessage()));
  }
}
