package com.silenteight.sens.webapp.keycloak.config;

import lombok.Value;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import java.util.Map;
import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;

@Value
public class MigratorProperties {

  @NotEmpty
  String clientId = "admin-cli";

  @NotEmpty
  String realm = "master";

  @NotEmpty
  String migrations;

  @NotEmpty
  String migrationsNameSeparator = "-";

  @NotEmpty
  String user;

  @NotEmpty
  String password;

  @NotEmpty
  String authServerUrl;

  // WARN(bgulowaty): Spring maps nested lists here to dictionary, instead of actual list.
  // this has impact on configuration templates.
  @Nullable
  Map<String, Object> templateProperties;

  public Keycloak toAdminClient() {
    return KeycloakBuilder.builder()
        .username(getUser())
        .password(getPassword())
        .clientId(getClientId())
        .realm(getRealm())
        .serverUrl(getAuthServerUrl())
        .build();
  }
}
