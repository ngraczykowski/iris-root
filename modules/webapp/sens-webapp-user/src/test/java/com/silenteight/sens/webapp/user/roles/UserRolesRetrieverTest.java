package com.silenteight.sens.webapp.user.roles;

import com.silenteight.sep.usermanagement.api.UserQuery;
import com.silenteight.sep.usermanagement.api.UserRoles;
import com.silenteight.sep.usermanagement.api.dto.UserDto;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRolesRetrieverTest {

  private static final String ROLES_SCOPE = "frontend";
  private static final String COUNTRY_GROUPS_SCOPE = "kibana";

  @Mock
  private UserQuery userQuery;

  private UserRolesRetriever underTest;

  @BeforeEach
  void setUp() {
    underTest = new UserRolesRetriever(userQuery, ROLES_SCOPE, COUNTRY_GROUPS_SCOPE);
  }

  @Test
  void retrieveRoles() {
    // given
    String username = "jsmith";
    List<String> roles = List.of("APPROVER", "ANALYST", "BUSINESS_OPERATOR");
    List<String> countryGroups = List.of("SG", "HK");
    Map<String, List<String>> rolesByScope = Map.of(
        ROLES_SCOPE, roles,
        COUNTRY_GROUPS_SCOPE, countryGroups);
    when(userQuery.find(username, Set.of(ROLES_SCOPE, COUNTRY_GROUPS_SCOPE)))
        .thenReturn(of(user(rolesByScope)));

    // when
    UserRoles results = underTest.rolesOf(username);

    // then
    assertThat(results.getSortedRoles(ROLES_SCOPE)).isEqualTo(roles);
    assertThat(results.getSortedRoles(COUNTRY_GROUPS_SCOPE)).isEqualTo(countryGroups);
  }

  @Test
  void throwsUserNotFoundExceptionIfUserDoesNotExist() {
    String username = "jsmith";
    when(userQuery.find(username, Set.of(ROLES_SCOPE, COUNTRY_GROUPS_SCOPE)))
        .thenThrow(new UserNotFoundException());

    ThrowingCallable rolesRetrievalCall = () -> underTest.rolesOf(username);

    assertThatThrownBy(rolesRetrievalCall).isInstanceOf(UserNotFoundException.class);
  }

  private UserDto user(Map<String, List<String>> roles) {
    UserDto userDto = new UserDto();
    userDto.setRoles(new ScopeUserRoles(roles));
    return userDto;
  }
}
