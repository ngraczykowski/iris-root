/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = IrisKubernetesProperties.PREFIX)
@Data
@Validated
public class IrisKubernetesProperties {

  static final String PREFIX = "iris.kubernetes";

  @NotBlank private String helmReleaseName;
}
