package com.silenteight.sens.webapp.keycloak.usermanagement.lock;

import com.silenteight.sens.webapp.keycloak.usermanagement.retrieval.KeycloakUserRetriever;
import com.silenteight.sep.base.testing.time.MockTimeSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserAttributeNames.DELETED_AT;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.time.Instant.parse;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakUserLockerTest {

  private static final String USERNAME = "jsmith";
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
    UserResource userResource = mock(UserResource.class);
    UserRepresentation userRepresentation = asUserRepresentation(USERNAME);
    when(keycloakUserRetriever.retrieve(USERNAME)).thenReturn(userResource);
    when(userResource.toRepresentation()).thenReturn(userRepresentation);

    // when
    underTest.lock(USERNAME);

    // then
    verify(keycloakUserRetriever).retrieve(USERNAME);
    verify(userResource).update(userRepresentationCaptor.capture());
    UserRepresentation captured = userRepresentationCaptor.getValue();
    assertThat(captured.getUsername()).isEqualTo(USERNAME);
    assertThat(captured.isEnabled()).isFalse();
    assertThat(captured.getAttributes()).containsKey(DELETED_AT);
    assertThat(captured.getAttributes().get(DELETED_AT))
        .containsExactly(DELETED_AT_TIME);
  }

  @Test
  void givenUsername_unlockUser() {
    // given
    UserResource userResource = mock(UserResource.class);
    UserRepresentation userRepresentation = asDeletedUserRepresentation(USERNAME);
    when(keycloakUserRetriever.retrieve(USERNAME)).thenReturn(userResource);
    when(userResource.toRepresentation()).thenReturn(userRepresentation);

    // when
    underTest.unlock(USERNAME);

    // then
    verify(keycloakUserRetriever).retrieve(USERNAME);
    verify(userResource).update(userRepresentationCaptor.capture());
    UserRepresentation captured = userRepresentationCaptor.getValue();
    assertThat(captured.getUsername()).isEqualTo(USERNAME);
    assertThat(captured.isEnabled()).isTrue();
    assertThat(captured.getAttributes()).doesNotContainKeys(DELETED_AT);
  }

  private static UserRepresentation asUserRepresentation(String username) {
    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setUsername(username);
    userRepresentation.setEnabled(TRUE);
    return userRepresentation;
  }

  private static UserRepresentation asDeletedUserRepresentation(String username) {
    UserRepresentation userRepresentation = asUserRepresentation(username);
    userRepresentation.setEnabled(FALSE);
    userRepresentation.singleAttribute(DELETED_AT, DELETED_AT_TIME);
    return userRepresentation;
  }
}
