package com.silenteight.sens.webapp.keycloak.usermanagement.query.role;

import lombok.NonNull;

import com.silenteight.sens.webapp.keycloak.usermanagement.query.role.CachedRolesProviderFixtures.UserRoles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.CachedRolesProviderFixtures.USER_1_ROLES;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.CachedRolesProviderFixtures.USER_2_NO_ROLES;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CachedRolesProviderTest {

  private static final int CACHE_SIZE = 4;
  @Mock
  private RolesProvider nestedRolesProvider;
  private CachedRolesProvider underTest;

  @BeforeEach
  void setUp() {
    underTest = new CachedRolesProvider(nestedRolesProvider, CACHE_SIZE, Duration.ofMinutes(1));
  }

  private void givenNestedProviderReturns(UserRoles user) {
    @NonNull List<String> roles = user.getRoles() == null ? emptyList() : user.getRoles();
    given(nestedRolesProvider.getForUserId(user.getUserId())).willReturn(roles);
  }

  @Test
  void requiredNonCachedRole_fetchesFromNestedProvider() {
    givenNestedProviderReturns(USER_1_ROLES);

    List<String> actual = underTest.getForUserId(USER_1_ROLES.getUserId());

    then(nestedRolesProvider).should().getForUserId(USER_1_ROLES.getUserId());
    assertThat(actual).isEqualTo(USER_1_ROLES.getRoles());
  }

  @Test
  void requiredCachedRoles_returnsFromCache() {
    underTest.update(USER_1_ROLES.getUserId(), USER_1_ROLES.getRoles());

    List<String> actual = underTest.getForUserId(USER_1_ROLES.getUserId());

    then(nestedRolesProvider).shouldHaveNoInteractions();
    assertThat(actual).isEqualTo(USER_1_ROLES.getRoles());
  }

  @Test
  void requiredCachedRolesOfUserWithNoRoles_returnsEmptyListFromCache() {
    underTest.update(USER_2_NO_ROLES.getUserId(), USER_2_NO_ROLES.getRoles());

    List<String> actual = underTest.getForUserId(USER_2_NO_ROLES.getUserId());

    then(nestedRolesProvider).shouldHaveNoInteractions();
    assertThat(actual).isEmpty();
  }
}
