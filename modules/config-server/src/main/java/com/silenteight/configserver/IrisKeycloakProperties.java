/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = IrisKeycloakProperties.PREFIX)
@Data
@Validated
public class IrisKeycloakProperties {

  static final String PREFIX = "iris.keycloak";

  @NotBlank private String frontendClientId;
  @NotBlank private String backendClientId;
  @NotBlank private String backendClientSecret;
  @NotBlank private String authServerUrl;
  @NotBlank private String realm;
}
