/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = IrisRabbitProperties.PREFIX)
@Data
@Validated
public class IrisRabbitProperties {

  static final String PREFIX = "iris.rabbit";

  @NotBlank private String host;
  @NotNull private Integer port;
  @NotBlank private String virtualHost = "/"; // TODO default?
  private String options;
  @NotBlank private String username;
  @NotBlank private String password;
}
