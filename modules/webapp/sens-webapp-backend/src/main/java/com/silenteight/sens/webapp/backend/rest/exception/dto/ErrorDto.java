package com.silenteight.sens.webapp.backend.rest.exception.dto;

import lombok.Data;

import java.time.Instant;
import java.util.Map;
import javax.validation.constraints.NotNull;

@Data
public class ErrorDto {

  private final String key;
  private final Instant date;
  private final Class<? extends Exception> exception;
  private final Map<String, Object> extras;

  public ErrorDto(
      @NotNull Exception e,
      @NotNull String key,
      @NotNull Map<String, Object> extras) {

    this.exception = e.getClass();
    this.key = key;
    this.date = Instant.now();
    this.extras = extras;
  }
}
