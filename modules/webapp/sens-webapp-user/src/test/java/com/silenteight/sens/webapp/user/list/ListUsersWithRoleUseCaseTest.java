package com.silenteight.sens.webapp.user.list;

import com.silenteight.sens.webapp.user.config.RolesProperties;
import com.silenteight.sep.usermanagement.api.user.UserQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.sens.webapp.user.list.ListUsersWithRoleUseCaseFixtures.COUNTRY_GROUPS_NAME;
import static com.silenteight.sens.webapp.user.list.ListUsersWithRoleUseCaseFixtures.COUNTRY_GROUPS_SCOPE;
import static com.silenteight.sens.webapp.user.list.ListUsersWithRoleUseCaseFixtures.USER_LIST_DTO;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListUsersWithRoleUseCaseTest {

  @Mock
  private UserQuery userQuery;
  @Mock
  private RolesProperties rolesProperties;

  private ListUsersWithRoleUseCase underTest;

  @BeforeEach
  void setUp() {
    given(rolesProperties.getCountryGroupsScope()).willReturn(COUNTRY_GROUPS_SCOPE);
    underTest =
        new ListUserUseCaseConfiguration().listUsersInRoleUseCase(userQuery, rolesProperties);
  }

  @Test
  void shouldListUsersWithRole() {
    // given
    when(userQuery.listAll(COUNTRY_GROUPS_NAME, COUNTRY_GROUPS_SCOPE))
        .thenReturn(of(USER_LIST_DTO));

    // when
    List<UserListDto> actual = underTest.apply(COUNTRY_GROUPS_NAME);

    // then
    assertThat(actual)
        .hasSize(1)
        .extracting(UserListDto::getCountryGroups)
        .hasSize(1);
  }
}
