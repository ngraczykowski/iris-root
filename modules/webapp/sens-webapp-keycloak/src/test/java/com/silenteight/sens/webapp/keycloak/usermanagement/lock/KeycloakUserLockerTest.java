package com.silenteight.sens.webapp.keycloak.usermanagement.lock;

import com.silenteight.sens.webapp.common.testing.time.MockTimeSource;
import com.silenteight.sens.webapp.keycloak.usermanagement.retrieval.KeycloakUserRetriever;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.keycloak.usermanagement.lock.UserLockerConstants.DELETED_AT_ATTRIBUTE_NAME;
import static java.time.Instant.parse;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakUserLockerTest {

  private static final String DELETED_AT_TIME = "2011-12-03T10:15:30Z";

  @Mock
  private KeycloakUserRetriever keycloakUserRetriever;

  private KeycloakUserLocker underTest;

  @Captor
  ArgumentCaptor<UserRepresentation> userRepresentationCaptor;

  @BeforeEach
  void setUp() {
    underTest = new KeycloakUserLocker(
        keycloakUserRetriever, new MockTimeSource(parse(DELETED_AT_TIME)));
  }

  @Test
  void givenUsername_lockUser() {
    // given
    String username = "jsmith";
    UserResource userResource = mock(UserResource.class);
    UserRepresentation userRepresentation = asUserRepresentation(username);
    when(keycloakUserRetriever.retrieve(username)).thenReturn(userResource);
    when(userResource.toRepresentation()).thenReturn(userRepresentation);

    // when
    underTest.lock(username);

    // then
    verify(keycloakUserRetriever).retrieve(username);
    verify(userResource).update(userRepresentationCaptor.capture());
    UserRepresentation captured = userRepresentationCaptor.getValue();
    assertThat(captured.getUsername()).isEqualTo(username);
    assertThat(captured.isEnabled()).isFalse();
    assertThat(captured.getAttributes()).containsKey(DELETED_AT_ATTRIBUTE_NAME);
    assertThat(captured.getAttributes().get(DELETED_AT_ATTRIBUTE_NAME))
        .containsExactly(DELETED_AT_TIME);
  }

  private static UserRepresentation asUserRepresentation(String username) {
    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setUsername(username);
    return userRepresentation;
  }
}
