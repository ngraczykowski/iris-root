package com.silenteight.sens.webapp.keycloak.usermanagement.id;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.keycloak.usermanagement.id.SingleRequestKeycloakUserIdProviderTest.SingleRequestKeycloakUserIdProviderTestFixtures.ID;
import static com.silenteight.sens.webapp.keycloak.usermanagement.id.SingleRequestKeycloakUserIdProviderTest.SingleRequestKeycloakUserIdProviderTestFixtures.USERNAME;
import static com.silenteight.sens.webapp.keycloak.usermanagement.id.SingleRequestKeycloakUserIdProviderTest.SingleRequestKeycloakUserIdProviderTestFixtures.USER_REPRESENTATION;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SingleRequestKeycloakUserIdProviderTest {

  @Mock
  private UsersResource usersResource;

  @InjectMocks
  private SingleRequestKeycloakUserIdProvider underTest;

  @Test
  void nonExistingUser_returnsEmpty() {
    given(usersResource.search(USERNAME)).willReturn(emptyList());

    var actual = underTest.findId(USERNAME);

    assertThat(actual).isEmpty();
  }

  @Test
  void existingUser_returnsCorrectId() {
    given(usersResource.search(USERNAME)).willReturn(singletonList(USER_REPRESENTATION));

    var actual = underTest.findId(USERNAME);

    assertThat(actual).contains(ID);
  }

  static class SingleRequestKeycloakUserIdProviderTestFixtures {

    static final String USERNAME = "jdoe123";
    static final String ID = "h5f9g";
    static final UserRepresentation USER_REPRESENTATION = userRepresentation();

    private static UserRepresentation userRepresentation() {
      UserRepresentation userRepresentation = new UserRepresentation();
      userRepresentation.setId(ID);

      return userRepresentation;
    }
  }
}
