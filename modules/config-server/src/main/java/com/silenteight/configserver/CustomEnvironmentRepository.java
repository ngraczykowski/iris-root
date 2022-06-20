/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver;

import lombok.RequiredArgsConstructor;

import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.core.Ordered;

import java.util.Collections;
import java.util.Map;

/**
 * This is only an example implementation and will be expanded later on. Currently it returns empty
 * set of properties
 */
@RequiredArgsConstructor
public class CustomEnvironmentRepository implements EnvironmentRepository, Ordered {
  @Override
  public Environment findOne(String application, String profile, String label) {
    Environment environment = new Environment(application, profile);

    final Map<String, String> properties = Collections.emptyMap();
    environment.add(new PropertySource("mapPropertySource", properties));
    return environment;
  }

  @Override
  public int getOrder() {
    return LOWEST_PRECEDENCE;
  }
}
