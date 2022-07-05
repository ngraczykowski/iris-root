/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver.dynamic.generator;

import lombok.RequiredArgsConstructor;

import com.silenteight.configserver.IrisProperties;
import com.silenteight.configserver.dynamic.IrisDynamicPropertiesGenerator;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(IrisProperties.class)
class Global implements IrisDynamicPropertiesGenerator {

  private static final Map<String, String> PROFILES =
      Map.ofEntries(entry("adjudication-engine", "rabbitdeclare"));

  private final IrisProperties irisProperties;

  @Override
  public Map<String, Object> generate(String application, String profile, String label) {
    return Map.ofEntries(
        entry("server.port", irisProperties.getServerPort()),
        entry("server.servlet.context-path", "/rest/" + application),
        entry("spring.webflux.base-path", "/rest/" + application),
        entry(
            "logging.config",
            irisProperties.getConfigServerUrl()
                + "/"
                + application
                + "/"
                + profile
                + "/"
                + Optional.ofNullable(label).orElse("master")
                + "/logback.xml"),
        entry("management.server.port", irisProperties.getManagementPort()),
        entry("management.server.base-path", "/"),
        entry("management.endpoints.web.base-path", "/management"),
        entry("grpc.server.port", irisProperties.getGrpcPort()),
        entry("serp.show-env", "true"),
        entry("spring.profiles.include", PROFILES.getOrDefault(application, "")));
  }
}
