/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = IrisProperties.PREFIX)
@Data
@Validated
public class IrisProperties {

  static final String PREFIX = "iris";

  @NotBlank private String configServerUrl;
  private Integer serverPort = 8080;
  private Integer managementPort = 8081;
  private Integer grpcPort = 9090;
}
