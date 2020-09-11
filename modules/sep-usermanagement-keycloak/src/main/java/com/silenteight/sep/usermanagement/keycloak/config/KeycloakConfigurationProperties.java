package com.silenteight.sep.usermanagement.keycloak.config;

import lombok.Value;

import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "keycloak")
@Value
@ConstructorBinding
class KeycloakConfigurationProperties {

  @NotNull
  AdapterConfig adapter;

}
