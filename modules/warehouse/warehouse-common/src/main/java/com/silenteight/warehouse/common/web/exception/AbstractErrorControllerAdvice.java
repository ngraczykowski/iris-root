package com.silenteight.warehouse.common.web.exception;

import lombok.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import javax.validation.constraints.NotNull;

import static java.util.Collections.emptyMap;

public abstract class AbstractErrorControllerAdvice {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  protected ResponseEntity<ErrorDto> handle(
      @NonNull Exception e, @NonNull String key, @NonNull HttpStatus status) {

    return handle(e, key, status, emptyMap());
  }

  protected ResponseEntity<ErrorDto> handle(
      @NonNull Exception e,
      @NonNull String key,
      @NonNull HttpStatus status,
      @NotNull Map<String, Object> extras) {

    log(e);
    ErrorDto error = new ErrorDto(key, extras);

    return new ResponseEntity<>(error, status);
  }

  private void log(Exception e) {
    logger.error("Error occurred", e);
  }
}
