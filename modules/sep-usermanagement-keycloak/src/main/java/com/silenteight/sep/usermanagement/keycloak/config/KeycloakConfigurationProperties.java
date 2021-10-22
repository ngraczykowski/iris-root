package com.silenteight.sep.usermanagement.keycloak.config;

import lombok.Value;

import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;
import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "keycloak")
@Value
@ConstructorBinding
public class KeycloakConfigurationProperties {

  @NotNull
  AdapterConfig adapter;

  @NotNull
  String frontendClientId;

  @NotNull
  List<String> hideUsersWithRoles;

  public String getAuthServerUrl() {
    return adapter.getAuthServerUrl();
  }

  public String getRealm() {
    return adapter.getRealm();
  }
}
