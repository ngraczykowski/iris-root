/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver.dynamic.generator;

import lombok.RequiredArgsConstructor;

import com.silenteight.configserver.IrisKeycloakProperties;
import com.silenteight.configserver.dynamic.IrisDynamicPropertiesGenerator;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Map.entry;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(IrisKeycloakProperties.class)
class Keycloak implements IrisDynamicPropertiesGenerator {

  private final IrisKeycloakProperties irisKeycloakProperties;

  @Override
  public Map<String, Object> generate(String application, String profile, String label) {

    var authServerUrl = irisKeycloakProperties.getAuthServerUrl();
    var realm = irisKeycloakProperties.getRealm();

    return Map.ofEntries(
        entry("keycloak.frontend-client-id", irisKeycloakProperties.getFrontendClientId()),
        entry("keycloak.adapter.auth-server-url", authServerUrl),
        entry("keycloak.adapter.realm", realm),
        entry("keycloak.adapter.resource", irisKeycloakProperties.getBackendClientId()),
        entry(
            "spring.security.oauth2.resourceserver.jwt.issuer-uri",
            authServerUrl + "/realms/" + realm),
        entry(
            "spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
            authServerUrl + "/realms/" + realm + "/protocol/openid-connect/certs"),
        entry(
            "keycloak.adapter.credentials.secret",
            irisKeycloakProperties.getBackendClientSecret()));
  }
}
