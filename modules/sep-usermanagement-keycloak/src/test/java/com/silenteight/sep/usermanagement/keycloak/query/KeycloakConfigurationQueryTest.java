package com.silenteight.sep.usermanagement.keycloak.query;

import com.silenteight.sep.usermanagement.api.configuration.dto.AuthConfigurationDto;
import com.silenteight.sep.usermanagement.keycloak.config.KeycloakConfigurationProperties;
import com.silenteight.sep.usermanagement.keycloak.query.dto.KeycloakAuthConfigurationDto;
import com.silenteight.sep.usermanagement.keycloak.query.dto.KeycloakDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.keycloak.representations.idm.RealmRepresentation;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakConfigurationQueryTest {

  private static final String AUTH_SERVER_URL = "https://auth.silenteight.com";
  private static final String REALM = "sens-webapp";
  private static final String FRONTEND_CLIENT_ID = "frontend";
  private static final int DEFAULT_SESSION_TIMEOUT = 1800;

  @Mock
  private RealmResource realmResource;

  private KeycloakConfigurationQuery underTest;

  @BeforeEach
  void setUp() {
    AdapterConfig adapterConfig = new AdapterConfig();
    adapterConfig.setAuthServerUrl(AUTH_SERVER_URL);
    adapterConfig.setRealm(REALM);

    KeycloakConfigurationProperties keycloakConfigurationProperties =
        new KeycloakConfigurationProperties(adapterConfig, FRONTEND_CLIENT_ID);

    underTest = new KeycloakConfigurationQuery(realmResource, keycloakConfigurationProperties);
  }

  @Test
  void givenSessionTimeout_returnSessionTimeout() {
    // given
    int sessionTimeout = 20;
    when(realmResource.toRepresentation()).thenReturn(realmRepresentation(sessionTimeout));

    // when
    int result = underTest.getSessionIdleTimeoutSeconds();

    // then
    assertThat(result).isEqualTo(sessionTimeout);
  }

  @Test
  void givenNoSessionTimeout_returnDefaultSessionTimeout() {
    // given
    when(realmResource.toRepresentation()).thenReturn(new RealmRepresentation());

    // when
    int result = underTest.getSessionIdleTimeoutSeconds();

    // then
    assertThat(result).isEqualTo(DEFAULT_SESSION_TIMEOUT);
  }

  @Test
  void exceptionThrown_returnDefaultSessionTimeout() {
    // given
    when(realmResource.toRepresentation()).thenThrow(new RuntimeException());

    // when
    int result = underTest.getSessionIdleTimeoutSeconds();

    // then
    assertThat(result).isEqualTo(DEFAULT_SESSION_TIMEOUT);
  }

  @Test
  void givenKeycloakProperties_returnAuthConfiguration() {
    // when
    AuthConfigurationDto authConfiguration = underTest.getAuthConfiguration();

    // then
    assertThat(authConfiguration).isInstanceOf(KeycloakAuthConfigurationDto.class);
    KeycloakAuthConfigurationDto keycloakAuthConfiguration =
        (KeycloakAuthConfigurationDto) authConfiguration;
    assertThat(keycloakAuthConfiguration.getKeycloak()).isEqualTo(
        KeycloakDto.builder()
            .url(AUTH_SERVER_URL)
            .realm(REALM)
            .clientId(FRONTEND_CLIENT_ID)
            .build());
  }

  private static RealmRepresentation realmRepresentation(int sessionTimeout) {
    RealmRepresentation realm = new RealmRepresentation();
    realm.setSsoSessionIdleTimeout(sessionTimeout);
    return realm;
  }
}
