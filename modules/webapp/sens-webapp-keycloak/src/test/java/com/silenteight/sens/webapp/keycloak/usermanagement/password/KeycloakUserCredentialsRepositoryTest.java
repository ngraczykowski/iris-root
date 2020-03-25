package com.silenteight.sens.webapp.keycloak.usermanagement.password;

import com.silenteight.sens.webapp.keycloak.usermanagement.id.KeycloakUserIdProvider;
import com.silenteight.sens.webapp.user.domain.UserOrigin;
import com.silenteight.sens.webapp.user.password.TemporaryPassword;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserAttributeNames.USER_ORIGIN;
import static com.silenteight.sens.webapp.keycloak.usermanagement.password.KeycloakUserCredentialsRepositoryTest.KeycloakInternalUserCredentialsRepositoryFixtures.TEMPORARY_PASSWORD;
import static com.silenteight.sens.webapp.keycloak.usermanagement.password.KeycloakUserCredentialsRepositoryTest.KeycloakInternalUserCredentialsRepositoryFixtures.USERNAME;
import static com.silenteight.sens.webapp.keycloak.usermanagement.password.KeycloakUserCredentialsRepositoryTest.KeycloakInternalUserCredentialsRepositoryFixtures.USER_ID;
import static com.silenteight.sens.webapp.user.domain.UserOrigin.GNS;
import static com.silenteight.sens.webapp.user.domain.UserOrigin.SENS;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class KeycloakUserCredentialsRepositoryTest {

  @Mock
  private UsersResource usersResource;

  @Mock
  private KeycloakUserIdProvider keycloakUserIdProvider;

  @Captor
  private ArgumentCaptor<CredentialRepresentation> credentialsCaptor;

  @InjectMocks
  private KeycloakUserCredentialsRepository underTest;

  @Test
  void userDoesNotExist_returnsEmpty() {
    given(keycloakUserIdProvider.findId(USERNAME)).willReturn(empty());

    var actual = underTest.findUserCredentials(USERNAME);

    assertThat(actual).isEmpty();
  }

  @Test
  void userExistsAndIsNotInternal_returnsCredentialsForNonInternalUser() {
    var externalUser = nonInternalUser();
    given(keycloakUserIdProvider.findId(USERNAME)).willReturn(of(USER_ID));
    given(usersResource.get(USER_ID)).willReturn(externalUser);

    var actual = underTest.findUserCredentials(USERNAME);

    assertThat(actual).isNotEmpty();
    assertThat(actual.get().ownerIsNotInternal()).isTrue();
  }

  private static UserResource nonInternalUser() {
    return userWithOrigin(GNS);
  }

  @Test
  void userExistsAndIsInternal_returnsCredentialsForInternalUser() {
    var internalUser = internalUser();
    given(keycloakUserIdProvider.findId(USERNAME)).willReturn(of(USER_ID));
    given(usersResource.get(USER_ID)).willReturn(internalUser);

    var actual = underTest.findUserCredentials(USERNAME);

    assertThat(actual).isNotEmpty();
    assertThat(actual.get().ownerIsInternal()).isTrue();
  }

  @Test
  void userExistsAndIsInternal_resetPasswordInteractsWithKeycloakCorrectly() {
    var internalUser = internalUser();
    given(keycloakUserIdProvider.findId(USERNAME)).willReturn(of(USER_ID));
    given(usersResource.get(USER_ID)).willReturn(internalUser);

    var actual = underTest.findUserCredentials(USERNAME).orElseThrow();

    actual.ownerIsInternal();
    then(internalUser).should().toRepresentation();

    actual.reset(TEMPORARY_PASSWORD);
    then(internalUser).should().resetPassword(credentialsCaptor.capture());
    assertThat(credentialsCaptor.getValue())
        .satisfies(credentials -> {
          assertThat(credentials.getType()).isEqualTo("password");
          assertThat(credentials.getValue()).isEqualTo(TEMPORARY_PASSWORD.getPassword());
          assertThat(credentials.isTemporary()).isTrue();
        });
  }

  private static UserResource internalUser() {
    return userWithOrigin(SENS);
  }

  private static UserResource userWithOrigin(UserOrigin origin) {
    UserResource userResource = mock(UserResource.class);
    given(userResource.toRepresentation()).willReturn(userRepresentation(origin));
    return userResource;
  }

  private static UserRepresentation userRepresentation(UserOrigin origin) {
    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.singleAttribute(USER_ORIGIN, origin.toString());
    return userRepresentation;
  }

  static class KeycloakInternalUserCredentialsRepositoryFixtures {

    static final String USERNAME = "jdoe123";
    static final String USER_ID = "35fyj6";
    static final TemporaryPassword TEMPORARY_PASSWORD = TemporaryPassword.of("somePassword");
  }
}
