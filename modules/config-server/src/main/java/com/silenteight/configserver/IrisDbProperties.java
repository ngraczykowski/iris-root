/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = IrisDbProperties.PREFIX)
@Data
@Validated
public class IrisDbProperties {

  static final String PREFIX = "iris.db";

  @NotBlank private String host;
  @NotNull private Integer port;
  @NotBlank private String name;
  private String schema;
  private String options;
  @NotBlank private String username;
  @NotBlank private String password;
}
