package com.silenteight.sens.webapp.backend.security.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class GenericErrorResponseDto {

  @NonNull
  private final ErrorDto error;
}
