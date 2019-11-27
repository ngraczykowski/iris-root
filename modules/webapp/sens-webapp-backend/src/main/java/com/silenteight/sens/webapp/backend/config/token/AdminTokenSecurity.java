package com.silenteight.sens.webapp.backend.config.token;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
class AdminTokenSecurity {

  private static final String ADMIN_USER_NAME = "admin";

  @NotBlank
  private final String adminToken;

  boolean isAdminToken(String token) {
    return StringUtils.equals(token, adminToken);
  }

  String getAdminUserName() {
    return ADMIN_USER_NAME;
  }
}
