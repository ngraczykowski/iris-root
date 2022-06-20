
/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb;

import org.springframework.boot.actuate.health.DefaultHealthContributorRegistry;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.actuate.health.HealthEndpointGroups;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is a workaround for missing consul health contributor.
 * See: https://java.tutorialink.com/consul-health-indicator-not-showing-in-springboot-actuator-health-endpoint/
 * https://github.com/spring-cloud/spring-cloud-consul/issues/671
 * Verify if that is still needed after updating spring-boot.
 */
@Configuration
public class MissingConsulHealthContributorFixConfiguration {

  @Bean
  HealthContributorRegistry healthContributorRegistry(
      ApplicationContext applicationContext, HealthEndpointGroups groups) {
    Map<String, HealthContributor> healthContributors =
        new LinkedHashMap<>(applicationContext.getBeansOfType(HealthContributor.class));
    ApplicationContext parent = applicationContext.getParent();
    while (parent != null) {
      healthContributors.putAll(parent.getBeansOfType(HealthContributor.class));
      parent = parent.getParent();
    }
    return new DefaultHealthContributorRegistry(healthContributors);
  }
}
