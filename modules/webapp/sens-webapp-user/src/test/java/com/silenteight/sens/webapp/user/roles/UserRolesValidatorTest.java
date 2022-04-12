package com.silenteight.sens.webapp.user.roles;

import com.silenteight.sep.usermanagement.api.user.UserQuery;
import com.silenteight.sep.usermanagement.api.user.dto.UserDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static com.silenteight.sens.webapp.user.roles.RolesTestFixtures.ROLE_NAME;
import static com.silenteight.sens.webapp.user.roles.RolesTestFixtures.ROLE_SCOPE;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRolesValidatorTest {

  private UserRolesValidator underTest;

  @Mock
  private UserQuery userQuery;

  @BeforeEach
  void setUp() {
    underTest = new UserRolesValidator(userQuery, ROLE_SCOPE);
  }


  @Test
  void anyUserHasRoleIsFalse() {
    // given
    when(userQuery.listAll(ROLE_NAME, ROLE_SCOPE)).thenReturn(emptyList());

    // when
    boolean result = underTest.isAssigned(ROLE_NAME);

    // then
    assertThat(result).isFalse();
  }

  @Test
  void anyUserHasRoleIsTrue() {
    // given
    UserDto user = UserDto.builder()
        .userName("jsmith")
        .origin("SENS")
        .roles(new ScopeUserRoles(Map.of(ROLE_SCOPE, List.of(ROLE_NAME))))
        .build();
    when(userQuery.listAll(ROLE_NAME, ROLE_SCOPE)).thenReturn(singletonList(user));

    // when
    boolean result = underTest.isAssigned(ROLE_NAME);

    // then
    assertThat(result).isTrue();
  }
}
