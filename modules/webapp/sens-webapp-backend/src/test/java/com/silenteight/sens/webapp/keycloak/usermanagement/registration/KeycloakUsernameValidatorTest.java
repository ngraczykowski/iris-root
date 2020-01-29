package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KeycloakUsernameValidatorTest {

  @InjectMocks
  KeycloakUsernameValidator underTest;
  @Mock
  private UsersResource usersResource;

  @Test
  void searchReturnsNoUsers_returnsTrue() {
    given(usersResource.search(Fixtures.USERNAME)).willReturn(emptyList());

    boolean actual = underTest.isUnique(Fixtures.USERNAME);

    assertTrue(actual);
  }

  @Test
  void searchReturnsOneUser_returnsFalse() {
    given(usersResource.search(Fixtures.USERNAME)).willReturn(List.of(new UserRepresentation()));

    boolean actual = underTest.isUnique(Fixtures.USERNAME);

    assertFalse(actual);
  }

  private static class Fixtures {

    public static final String USERNAME = "johndoe5";
  }
}
