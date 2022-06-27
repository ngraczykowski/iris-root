/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = IrisMailProperties.PREFIX)
@Data
@Validated
public class IrisMailProperties {

  static final String PREFIX = "iris.mail";

  private boolean enabled;
  @NotBlank private String host;
  private int port = 25;
  private String username;
  private String password;
}
