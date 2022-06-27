/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = IrisSentryProperties.PREFIX)
@Data
@Validated
public class IrisSentryProperties {

  static final String PREFIX = "iris.sentry";

  private boolean enabled;

  private String environment;
  private String dsn;
  private Map<String, String> componentDsn = new HashMap<>();
}
