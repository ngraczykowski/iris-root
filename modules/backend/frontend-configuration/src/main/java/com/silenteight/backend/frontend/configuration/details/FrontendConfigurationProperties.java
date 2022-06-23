/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.backend.frontend.configuration.details;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Value
@ConfigurationProperties(prefix = "frontend")
public class FrontendConfigurationProperties {

  Map<String, String> configuration;

}
