package com.silenteight.sens.webapp.backend.config.token;

import lombok.Data;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
class TokenProperties {

  @NotBlank
  private String admin;
}
