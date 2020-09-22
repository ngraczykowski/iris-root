package com.silenteight.sep.usermanagement.keycloak.update;


import com.silenteight.sep.usermanagement.keycloak.KeycloakUserAttributeNames;
import com.silenteight.sep.usermanagement.keycloak.retrieval.KeycloakUserRetriever;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakUserLockerTest {

  private static final String USERNAME = "jsmith";
  private static final String LOCKED_AT_TIME = "2011-12-03T10:15:30Z";
  @Captor
  ArgumentCaptor<UserRepresentation> userRepresentationCaptor;
  @Mock
  private KeycloakUserRetriever keycloakUserRetriever;
  private KeycloakUserLocker underTest;

  @BeforeEach
  void setUp() {
    underTest = new KeycloakUserLocker(keycloakUserRetriever);
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
    assertThat(captured.getAttributes()).doesNotContainKeys(KeycloakUserAttributeNames.LOCKED_AT);
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
    userRepresentation.singleAttribute(KeycloakUserAttributeNames.LOCKED_AT, LOCKED_AT_TIME);
    return userRepresentation;
  }
}
