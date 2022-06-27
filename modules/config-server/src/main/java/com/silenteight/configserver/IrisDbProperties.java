/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = IrisDbProperties.PREFIX)
@Data
@Validated
public class IrisDbProperties {

  static final String PREFIX = "iris.db";

  private boolean enabled;
  @NotBlank private String host;
  private int port = 5432;
  @NotBlank private String name;
  private String options;
  @NotBlank private String username;
  @NotBlank private String password;
}
