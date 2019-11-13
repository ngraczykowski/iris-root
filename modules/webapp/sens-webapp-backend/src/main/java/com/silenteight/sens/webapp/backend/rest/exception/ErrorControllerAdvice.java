package com.silenteight.sens.webapp.backend.rest.exception;

import lombok.NonNull;

import com.silenteight.sens.webapp.backend.rest.exception.dto.ErrorDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import javax.validation.constraints.NotNull;

import static java.util.Collections.emptyMap;

public abstract class ErrorControllerAdvice {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  protected ResponseEntity<ErrorDto> handle(
      @NonNull Exception e,
      @NonNull HttpStatus status) {
    return handle(e, e.getClass().getSimpleName(), status);
  }

  protected ResponseEntity<ErrorDto> handle(
      @NonNull Exception e,
      @NonNull String key,
      @NonNull HttpStatus status) {
    return handle(e, key, status, emptyMap());
  }

  protected ResponseEntity<ErrorDto> handle(
      @NonNull Exception e,
      @NonNull String key,
      @NonNull HttpStatus status,
      @NotNull Map<String, Object> extras) {
    log(e);

    ErrorDto error = new ErrorDto(e, key, extras);

    return new ResponseEntity<>(error, status);
  }

  private void log(Exception e) {
    logger.error("Error occurred", e);
  }
}
