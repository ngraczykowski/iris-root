/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver.dynamic.generator;

import lombok.RequiredArgsConstructor;

import com.silenteight.configserver.IrisDbProperties;
import com.silenteight.configserver.dynamic.IrisDynamicPropertiesGenerator;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;

@Component
@ConditionalOnProperty("iris.db.enabled")
@RequiredArgsConstructor
@EnableConfigurationProperties(IrisDbProperties.class)
class Db implements IrisDynamicPropertiesGenerator {

  private final IrisDbProperties irisDbProperties;

  @Override
  public Map<String, Object> generate(String application, String profile, String label) {

    return Map.ofEntries(
        entry(
            "spring.datasource.url",
            "jdbc:postgresql://"
                + irisDbProperties.getHost()
                + ":"
                + irisDbProperties.getPort()
                + "/"
                + irisDbProperties.getName()
                + Optional.ofNullable(irisDbProperties.getOptions()).map(x -> "?" + x).orElse("")),
        entry("spring.datasource.username", irisDbProperties.getUsername()),
        entry("spring.datasource.password", irisDbProperties.getPassword()));
  }
}
