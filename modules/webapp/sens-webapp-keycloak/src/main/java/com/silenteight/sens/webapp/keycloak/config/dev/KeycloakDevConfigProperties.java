package com.silenteight.sens.webapp.keycloak.config.dev;

import lombok.ToString;
import lombok.Value;

import com.silenteight.sens.webapp.keycloak.config.KeycloakAdapterConfigFactory;
import com.silenteight.sens.webapp.keycloak.config.KeycloakAdminClientFactory;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotEmpty;

import static java.util.Map.of;

@ConfigurationProperties(prefix = "keycloak", ignoreUnknownFields = false)
@Value
@ConstructorBinding
class KeycloakDevConfigProperties implements
    KeycloakAdapterConfigFactory, KeycloakAdminClientFactory {

  private static final String ADMIN_CLIENT_ID = "admin-cli";
  private static final String ADMIN_CLIENT_REALM = "master";

  @NotEmpty
  String authServerUrl;

  @NotEmpty
  String realmName;

  @NotEmpty
  String adminUsername;

  @NotEmpty
  String clientName;

  @NotEmpty
  @ToString.Exclude
  String clientSecret;

  @NotEmpty
  @ToString.Exclude
  String adminPassword;

  @NotEmpty
  String configPath;

  @Override
  public AdapterConfig getAdapterConfig() {
    var adapterConfig = new AdapterConfig();
    adapterConfig.setAuthServerUrl(authServerUrl);
    adapterConfig.setRealm(realmName);
    adapterConfig.setResource(getClientName());
    adapterConfig.setSslRequired("external");
    adapterConfig.setPublicClient(false);
    adapterConfig.setConfidentialPort(0);
    adapterConfig.setPrincipalAttribute("preferred_username");
    adapterConfig.setCredentials(of("secret", getClientSecret()));
    adapterConfig.setUseResourceRoleMappings(true);
    return adapterConfig;
  }

  @Override
  public Keycloak getAdminClient() {
    return KeycloakBuilder.builder()
        .username(getAdminUsername())
        .password(getAdminPassword())
        .clientId(ADMIN_CLIENT_ID)
        .realm(ADMIN_CLIENT_REALM)
        .serverUrl(getAuthServerUrl())
        .build();
  }
}
