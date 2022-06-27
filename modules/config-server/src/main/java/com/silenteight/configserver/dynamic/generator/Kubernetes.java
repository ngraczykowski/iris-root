/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver.dynamic.generator;

import lombok.RequiredArgsConstructor;

import com.silenteight.configserver.IrisKubernetesProperties;
import com.silenteight.configserver.dynamic.IrisDynamicPropertiesGenerator;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Profile("kubernetes")
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(IrisKubernetesProperties.class)
class Kubernetes implements IrisDynamicPropertiesGenerator {

  private final IrisKubernetesProperties irisKubernetesProperties;

  private static final Set<String> COMPONENTS =
      Set.of(
          "datasource",
          "ae",
          "adjudication-engine",
          "adjudicationengine",
          "companynamesurroundingagent",
          "governance",
          "simulator",
          "webapp",
          "warehouse");
  private static final Map<String, String> NAME_FIXUP =
      Map.of(
          "companynamesurroundingagent",
          "company-name-surrounding-agent",
          "adjudicationengine",
          "adjudication-engine",
          "ae",
          "adjudication-engine",
          "datasource",
          "universal-data-source");

  @Override
  public Map<String, Object> generate(String application, String profile, String label) {
    return COMPONENTS.stream()
        .collect(
            Collectors.toMap(
                x -> "grpc.client." + x + ".address",
                x ->
                    "dns:///"
                        + irisKubernetesProperties.getHelmReleaseName()
                        + "-"
                        + NAME_FIXUP.getOrDefault(x, x)
                        + ":9090"));
  }
}
