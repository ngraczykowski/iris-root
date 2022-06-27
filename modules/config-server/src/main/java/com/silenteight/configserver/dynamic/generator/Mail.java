/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver.dynamic.generator;

import lombok.RequiredArgsConstructor;

import com.silenteight.configserver.IrisMailProperties;
import com.silenteight.configserver.dynamic.IrisDynamicPropertiesGenerator;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Map.entry;

@Component
@ConditionalOnProperty("iris.mail.enabled")
@RequiredArgsConstructor
@EnableConfigurationProperties(IrisMailProperties.class)
class Mail implements IrisDynamicPropertiesGenerator {

  private final IrisMailProperties irisMailProperties;

  @Override
  public Map<String, Object> generate(String application, String profile, String label) {
    return Map.ofEntries(
        entry("spring.mail.host", irisMailProperties.getHost()),
        entry("spring.mail.port", irisMailProperties.getPort()),
        entry("spring.mail.username", irisMailProperties.getUsername()),
        entry("spring.mail.password", irisMailProperties.getPassword()));
  }
}
