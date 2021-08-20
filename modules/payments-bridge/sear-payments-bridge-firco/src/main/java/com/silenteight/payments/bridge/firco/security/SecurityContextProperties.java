package com.silenteight.payments.bridge.firco.security;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "pb.firco.security.context")
@Data
@Validated
class SecurityContextProperties {

  private static final String DEFAULT_ROLES = "role";

  private String roles = DEFAULT_ROLES;
}
