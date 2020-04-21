package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.user.domain.validator.UsernameUniquenessValidator.UsernameNotUniqueError;

import io.vavr.control.Option;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KeycloakUsernameUniquenessValidatorTest {

  @InjectMocks
  KeycloakUsernameUniquenessValidator underTest;
  @Mock
  private UsersResource usersResource;
  @Mock
  private AuditLog auditLog;

  @Test
  void searchReturnsNoUsers_noError() {
    given(usersResource.search(Fixtures.USERNAME)).willReturn(emptyList());

    Option<UsernameNotUniqueError> error = underTest.validate(Fixtures.USERNAME);

    assertThat(error).isEmpty();
  }

  @Test
  void searchReturnsUsersButNoneExactMatch_returnsFalse() {
    given(usersResource.search(Fixtures.USERNAME)).willReturn(Fixtures.NON_MATCH_USERS);

    Option<UsernameNotUniqueError> error = underTest.validate(Fixtures.USERNAME);

    assertThat(error).isEmpty();
  }

  @Test
  void searchReturnsUsersAndOneExactMatch_returnsFalse() {
    given(usersResource.search(Fixtures.USERNAME)).willReturn(Fixtures.MATCH_USERS);

    Option<UsernameNotUniqueError> error = underTest.validate(Fixtures.USERNAME);

    assertThat(error).isNotEmpty();
  }

  private static class Fixtures {

    public static final String USERNAME = "johndoe5";
    public static final String SIMILAR_USERNAME = "johndoe52";
    public static final String DIFFERENT_USERNAME = "johndoe52";

    public static final List<UserRepresentation> NON_MATCH_USERS = asList(
        createUserRepresentation(SIMILAR_USERNAME),
        createUserRepresentation(DIFFERENT_USERNAME)
    );

    public static final List<UserRepresentation> MATCH_USERS = asList(
        createUserRepresentation(USERNAME),
        createUserRepresentation(DIFFERENT_USERNAME)
    );

    @NotNull
    private static UserRepresentation createUserRepresentation(String name) {
      UserRepresentation userRepresentation = new UserRepresentation();
      userRepresentation.setUsername(name);
      return userRepresentation;
    }
  }
}
