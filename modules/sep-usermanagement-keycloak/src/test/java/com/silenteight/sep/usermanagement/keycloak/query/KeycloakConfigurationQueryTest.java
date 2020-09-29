package com.silenteight.sep.usermanagement.keycloak.query;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.RealmRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakConfigurationQueryTest {

  private static final int DEFAULT_SESSION_TIMEOUT = 1800;

  @InjectMocks
  private KeycloakConfigurationQuery underTest;

  @Mock
  private RealmResource realmResource;

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

  private static RealmRepresentation realmRepresentation(int sessionTimeout) {
    RealmRepresentation realm = new RealmRepresentation();
    realm.setSsoSessionIdleTimeout(sessionTimeout);
    return realm;
  }
}
