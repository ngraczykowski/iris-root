package com.silenteight.sens.webapp.backend.security.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
public class PrincipalDto {

  @NonNull
  private final String userName;
  @NonNull
  private final List<String> authorities;

  private final String displayName;

  private final boolean superUser;
}
