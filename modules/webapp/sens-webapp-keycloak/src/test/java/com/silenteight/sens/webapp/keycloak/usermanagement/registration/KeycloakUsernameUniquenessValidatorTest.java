package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import com.silenteight.sens.webapp.user.registration.domain.UsernameUniquenessValidator.UsernameNotUniqueError;

import io.vavr.control.Option;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KeycloakUsernameUniquenessValidatorTest {

  @InjectMocks
  KeycloakUsernameUniquenessValidator underTest;
  @Mock
  private UsersResource usersResource;

  @Test
  void searchReturnsNoUsers_noError() {
    given(usersResource.search(Fixtures.USERNAME)).willReturn(emptyList());

    Option<UsernameNotUniqueError> error = underTest.validate(Fixtures.USERNAME);

    assertThat(error).isEmpty();
  }

  @Test
  void searchReturnsOneUser_returnsFalse() {
    given(usersResource.search(Fixtures.USERNAME)).willReturn(List.of(new UserRepresentation()));

    Option<UsernameNotUniqueError> error = underTest.validate(Fixtures.USERNAME);

    assertThat(error).isNotEmpty();
  }

  private static class Fixtures {

    public static final String USERNAME = "johndoe5";
  }
}
