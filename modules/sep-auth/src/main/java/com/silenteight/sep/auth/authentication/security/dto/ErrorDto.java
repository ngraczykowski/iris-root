package com.silenteight.sep.auth.authentication.security.dto;

import lombok.Data;
import lombok.NonNull;

import java.beans.ConstructorProperties;

@Data
public class ErrorDto {

  private final String type;

  private final String message;

  public ErrorDto(@NonNull Exception cause) {
    this(cause.getClass().getName(), cause.getMessage());
  }

  @ConstructorProperties({"type", "message"})
  public ErrorDto(@NonNull String type, String message) {
    this.type = type;
    this.message = message;
  }
}
