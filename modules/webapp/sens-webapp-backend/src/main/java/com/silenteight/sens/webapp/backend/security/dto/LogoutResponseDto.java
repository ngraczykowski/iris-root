package com.silenteight.sens.webapp.backend.security.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class LogoutResponseDto {

  @NonNull
  private final String redirectToUrl;
}
