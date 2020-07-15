package com.silenteight.sens.auth.config;

import lombok.Value;

import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "keycloak")
@Value
@ConstructorBinding
class KeycloakAdapterConfigurationProperties implements
    KeycloakAdapterConfigFactory {

  @NotNull
  AdapterConfig adapter;

}
