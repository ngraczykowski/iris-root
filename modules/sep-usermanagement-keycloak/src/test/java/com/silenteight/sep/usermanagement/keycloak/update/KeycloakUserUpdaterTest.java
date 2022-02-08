package com.silenteight.sep.usermanagement.keycloak.update;

import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.sep.usermanagement.api.user.dto.UpdateUserCommand;
import com.silenteight.sep.usermanagement.api.user.dto.UpdateUserCommand.UpdateUserCommandBuilder;
import com.silenteight.sep.usermanagement.keycloak.assignrole.KeycloakUserRoleAssigner;
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

import static com.silenteight.sep.usermanagement.keycloak.KeycloakUserAttributeNames.LOCKED_AT;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.time.Instant.parse;
import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakUserUpdaterTest {

  private static final String USERNAME = "jsmith";
  private static final String LOCKED_AT_TIME = "2011-12-03T10:15:30Z";
  @Mock
  private KeycloakUserRetriever keycloakUserRetriever;
  @Mock
  private KeycloakUserRoleAssigner roleAssigner;
  @Captor
  ArgumentCaptor<UserRepresentation> userRepresentationCaptor;

  private KeycloakUserUpdater underTest;

  @BeforeEach
  void setUp() {
    underTest = new KeycloakUserUpdater(
        keycloakUserRetriever, roleAssigner, new MockTimeSource(parse(LOCKED_AT_TIME)));
  }

  @Test
  void lockUser() {
    UserResource userResource = mock(UserResource.class);
    UserRepresentation userRepresentation = userRepresentationWithEnabled(TRUE);
    when(keycloakUserRetriever.retrieve(USERNAME)).thenReturn(userResource);
    when(userResource.toRepresentation()).thenReturn(userRepresentation);

    UpdateUserCommand updateUserCommand =
        updatedUserWithDefaults().username(USERNAME).locked(TRUE).build();

    underTest.update(updateUserCommand);

    verify(userResource).update(userRepresentationCaptor.capture());
    UserRepresentation captured = userRepresentationCaptor.getValue();

    assertThat(captured.isEnabled()).isFalse();
    assertThat(captured.getAttributes()).containsKey(LOCKED_AT);
    assertThat(captured.getAttributes().get(LOCKED_AT)).containsExactly(LOCKED_AT_TIME);
  }

  @Test
  void unlockUser() {
    UserResource userResource = mock(UserResource.class);
    UserRepresentation userRepresentation = userRepresentationWithEnabled(FALSE);
    userRepresentation.singleAttribute(LOCKED_AT, now().toString());

    when(keycloakUserRetriever.retrieve(USERNAME)).thenReturn(userResource);
    when(userResource.toRepresentation()).thenReturn(userRepresentation);

    UpdateUserCommand updateUserCommand =
        updatedUserWithDefaults().username(USERNAME).locked(FALSE).build();

    underTest.update(updateUserCommand);

    verify(userResource).update(userRepresentationCaptor.capture());
    UserRepresentation captured = userRepresentationCaptor.getValue();

    assertThat(captured.isEnabled()).isTrue();
    assertThat(captured.getAttributes()).doesNotContainKeys(LOCKED_AT);
  }

  private UpdateUserCommandBuilder updatedUserWithDefaults() {
    return UpdateUserCommand.builder().updateDate(now());
  }

  private static UserRepresentation userRepresentationWithEnabled(Boolean enabled) {
    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setEnabled(enabled);
    return userRepresentation;
  }
}
