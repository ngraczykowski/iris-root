package com.silenteight.sens.webapp.backend.security.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.beans.ConstructorProperties;

@Data
@EqualsAndHashCode(callSuper = true)
public class UnauthorizedResponseDto extends GenericErrorResponseDto {

  private final String loginUrl;

  @ConstructorProperties({"loginUrl", "error"})
  public UnauthorizedResponseDto(@NonNull String loginUrl, ErrorDto error) {
    super(error);
    this.loginUrl = loginUrl;
  }
}
