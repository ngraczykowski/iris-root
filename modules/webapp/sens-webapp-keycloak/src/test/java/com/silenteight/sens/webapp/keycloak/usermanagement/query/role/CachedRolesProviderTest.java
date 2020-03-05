package com.silenteight.sens.webapp.keycloak.usermanagement.query.role;

import lombok.NonNull;

import com.silenteight.sens.webapp.keycloak.usermanagement.query.role.CachedRolesProviderFixtures.UserRoles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.CachedRolesProviderFixtures.USER_1_ROLES;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.CachedRolesProviderFixtures.USER_2_NO_ROLES;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Map.of;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.atMost;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CachedRolesProviderTest {

  private static final int CACHE_SIZE = 4;
  @Mock
  private RolesProvider nestedRolesProvider;
  @Mock
  private RolesFetcher rolesFetcher;
  private CachedRolesProvider underTest;

  private void createUnderTest() {
    underTest = new CachedRolesProvider(nestedRolesProvider, rolesFetcher,
        CACHE_SIZE, Duration.ofMinutes(1));
  }

  private void givenNestedProviderReturns(UserRoles user) {
    @NonNull List<String> roles = user.getRoles() == null ? emptyList() : user.getRoles();
    given(nestedRolesProvider.getForUserId(user.getUserId())).willReturn(roles);
  }

  @Nested
  class GivenBulkFetcherReturnsEmpty {

    @BeforeEach
    void setUp() {
      given(rolesFetcher.fetch()).willReturn(emptyMap());
      createUnderTest();
    }

    @Test
    void correctlyCallsBulkFetcherUponCreationToLoadInitialData() {
      then(rolesFetcher).should().fetch();
    }

    @Test
    void firstUserDataCorrectlyReturnedFromCache_afterUpdatingWithFirstUserData() {
      underTest.update(USER_1_ROLES.getUserId(), USER_1_ROLES.getRoles());
      List<String> actual = underTest.getForUserId(USER_1_ROLES.getUserId());

      then(nestedRolesProvider).shouldHaveZeroInteractions();
      assertThat(actual).isEqualTo(USER_1_ROLES.getRoles());
    }
  }

  @Nested
  class GivenBulkFetcherReturnsEmptyAndNestedProviderReturnsUserOneData {

    @BeforeEach
    void setUp() {
      givenNestedProviderReturns(USER_1_ROLES);
      given(rolesFetcher.fetch()).willReturn(emptyMap());

      createUnderTest();
    }

    @Test
    void usesNestedLoginTimeProviderAndReturnsCorrectResult_whenGettingLastLoginTime() {
      List<String> actual = underTest.getForUserId(USER_1_ROLES.getUserId());

      then(nestedRolesProvider).should().getForUserId(USER_1_ROLES.getUserId());
      assertThat(actual).isEqualTo(USER_1_ROLES.getRoles());
    }

    @Test
    void secondRequestReturnsFromCache_whenGettingLoginTimeTwoTimesForSameUser() {
      underTest.getForUserId(USER_1_ROLES.getUserId());
      underTest.getForUserId(USER_1_ROLES.getUserId());

      then(nestedRolesProvider)
          .should(atMost(1)).getForUserId(USER_1_ROLES.getUserId());
    }
  }

  @Nested
  class GivenBulkFetcherReturnsUser1Fixture {

    @BeforeEach
    void setUp() {
      given(rolesFetcher.fetch())
          .willReturn(of(
              USER_1_ROLES.getUserId(),
              requireNonNull(USER_1_ROLES.getRoles())
          ));

      createUnderTest();
    }

    @Test
    void userOneDataIsCorrectlyReturnedFromCache() {
      List<String> actual = underTest.getForUserId(USER_1_ROLES.getUserId());

      then(nestedRolesProvider).shouldHaveZeroInteractions();

      assertThat(actual).isEqualTo(USER_1_ROLES.getRoles());
    }

    @Test
    void userTwoDataIsFetchedFromNestedProvider() {
      givenNestedProviderReturns(USER_2_NO_ROLES);
      List<String> actual = underTest.getForUserId(USER_2_NO_ROLES.getUserId());

      then(nestedRolesProvider).should().getForUserId(USER_2_NO_ROLES.getUserId());

      assertThat(actual).isEqualTo(emptyList());
    }
  }
}
