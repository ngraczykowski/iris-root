package com.silenteight.sep.auth.authentication.security.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class GenericErrorResponseDto {

  @NonNull
  private final ErrorDto error;
}
