package com.silenteight.sep.usermanagement.keycloak;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.BeforeAll;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

@Testcontainers(disabledWithoutDocker = true)
public abstract class BaseKeycloakIntegrationTest {

  private static final String KEYCLOAK_REALM_NAME = "master";
  private static final String KEYCLOAK_VERSION = "15.0.2";
  private static final String KEYCLOAK_REALM_CONFIG_JSON = "realm-sens-webapp-test.json";

  private static Keycloak keycloakAdminClient;

  @Container
  private static final KeycloakContainer KEYCLOAK_CONTAINER =
      new KeycloakContainer("jboss/keycloak:" + KEYCLOAK_VERSION)
          .withRealmImportFile(KEYCLOAK_REALM_CONFIG_JSON)
          .withAdminUsername("admin")
          .withAdminPassword("admin")
          .withStartupTimeout(Duration.ofMinutes(5)); // on M1 it runs in very slow emulation mode

  @BeforeAll
  private static void setup() {
    keycloakAdminClient = KeycloakBuilder.builder()
        .serverUrl(KEYCLOAK_CONTAINER.getAuthServerUrl())
        .realm(KEYCLOAK_REALM_NAME)
        .clientId("admin-cli")
        .username(KEYCLOAK_CONTAINER.getAdminUsername())
        .password(KEYCLOAK_CONTAINER.getAdminPassword())
        .build();
  }

  protected static RealmResource getRealm() {
    return keycloakAdminClient.realm(KEYCLOAK_REALM_NAME);
  }
}
