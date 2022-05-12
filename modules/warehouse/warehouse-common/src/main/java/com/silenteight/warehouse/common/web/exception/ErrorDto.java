package com.silenteight.warehouse.common.web.exception;

import lombok.Data;

import java.time.Instant;
import java.util.Map;
import javax.validation.constraints.NotNull;

@Data
public class ErrorDto {

  private final String key;
  private final Instant date;
  private final Map<String, Object> extras;

  public ErrorDto(@NotNull String key, @NotNull Map<String, Object> extras) {
    this.key = key;
    this.date = Instant.now();
    this.extras = extras;
  }
}
