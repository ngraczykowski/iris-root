/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver.dynamic.generator;

import lombok.RequiredArgsConstructor;

import com.silenteight.configserver.IrisSentryProperties;
import com.silenteight.configserver.dynamic.IrisDynamicPropertiesGenerator;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Map.entry;

@Component
@ConditionalOnProperty("iris.sentry.enabled")
@RequiredArgsConstructor
@EnableConfigurationProperties(IrisSentryProperties.class)
class Sentry implements IrisDynamicPropertiesGenerator {

  private final IrisSentryProperties irisSentryProperties;

  @Override
  public Map<String, Object> generate(String application, String profile, String label) {
    return Map.ofEntries(
        entry(
            "sentry.dsn",
            irisSentryProperties
                .getComponentDsn()
                .getOrDefault(application, irisSentryProperties.getDsn())),
        entry("sentry.environment", irisSentryProperties.getEnvironment()));
  }
}
