package com.silenteight.simulator.common.web.security;

import lombok.Data;
import lombok.NonNull;

@Data
public class GenericErrorResponseDto {

  @NonNull
  private final ErrorDto error;
}
