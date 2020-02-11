package com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime;

import com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.CachedLastLoginTimeProviderFixtures.LastLoginTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.CachedLastLoginTimeProviderFixtures.USER_1_LAST_LOGIN_TIME;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.CachedLastLoginTimeProviderFixtures.USER_2_LAST_LOGIN_TIME;
import static java.util.Collections.emptyMap;
import static java.util.Map.of;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.atMost;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CachedLastLoginTimeProviderTest {

  private static final int CACHE_SIZE = 4;
  @Mock
  private LastLoginTimeProvider nestedLoginTimeProvider;
  @Mock
  private LastLoginTimeBulkFetcher lastLoginTimeBulkFetcher;
  private CachedLastLoginTimeProvider underTest;

  private void createUnderTest() {
    underTest = new CachedLastLoginTimeProvider(nestedLoginTimeProvider, lastLoginTimeBulkFetcher,
        CACHE_SIZE, Duration.ofMinutes(1));
  }

  private void givenNestedProviderReturns(LastLoginTime user1LastLoginTime) {
    given(nestedLoginTimeProvider.getForUserId(user1LastLoginTime.getUserId()))
        .willReturn(user1LastLoginTime.getTimeOpt());
  }

  @Nested
  class GivenBulkFetcherReturnsEmpty {

    @BeforeEach
    void setUp() {
      given(lastLoginTimeBulkFetcher.fetch(CACHE_SIZE)).willReturn(emptyMap());
      createUnderTest();
    }

    @Test
    void correctlyCallsBulkFetcherUponCreationToLoadInitialData() {
      then(lastLoginTimeBulkFetcher).should().fetch(CACHE_SIZE);
    }

    @Test
    void firstUserDataCorrectlyReturnedFromCache_afterUpdatingWithFirstUserData() {
      underTest.update(USER_1_LAST_LOGIN_TIME.getUserId(), USER_1_LAST_LOGIN_TIME.getTime());
      Optional<OffsetDateTime> actual =
          underTest.getForUserId(USER_1_LAST_LOGIN_TIME.getUserId());

      then(nestedLoginTimeProvider).shouldHaveZeroInteractions();
      assertThat(actual).isEqualTo(USER_1_LAST_LOGIN_TIME.getTimeOpt());
    }
  }

  @Nested
  class GivenBulkFetcherReturnsEmptyAndNestedProviderReturnsUserOneData {

    @BeforeEach
    void setUp() {
      givenNestedProviderReturns(USER_1_LAST_LOGIN_TIME);
      given(lastLoginTimeBulkFetcher.fetch(CACHE_SIZE)).willReturn(emptyMap());

      createUnderTest();
    }

    @Test
    void usesNestedLoginTimeProviderAndReturnsCorrectResult_whenGettingLastLoginTime() {
      Optional<OffsetDateTime> actual = underTest.getForUserId(USER_1_LAST_LOGIN_TIME.getUserId());

      then(nestedLoginTimeProvider).should().getForUserId(USER_1_LAST_LOGIN_TIME.getUserId());
      assertThat(actual).isEqualTo(USER_1_LAST_LOGIN_TIME.getTimeOpt());
    }

    @Test
    void secondRequestReturnsFromCache_whenGettingLoginTimeTwoTimesForSameUser() {
      underTest.getForUserId(USER_1_LAST_LOGIN_TIME.getUserId());
      underTest.getForUserId(USER_1_LAST_LOGIN_TIME.getUserId());

      then(nestedLoginTimeProvider)
          .should(atMost(1)).getForUserId(USER_1_LAST_LOGIN_TIME.getUserId());
    }
  }

  @Nested
  class GivenBulkFetcherReturnsUser1Fixture {

    @BeforeEach
    void setUp() {
      given(lastLoginTimeBulkFetcher.fetch(CACHE_SIZE))
          .willReturn(of(
              USER_1_LAST_LOGIN_TIME.getUserId(),
              requireNonNull(USER_1_LAST_LOGIN_TIME.getTime())
          ));

      createUnderTest();
    }

    @Test
    void userOneDataIsCorrectlyReturnedFromCache() {
      Optional<OffsetDateTime> actual = underTest.getForUserId(USER_1_LAST_LOGIN_TIME.getUserId());

      then(nestedLoginTimeProvider).shouldHaveZeroInteractions();

      assertThat(actual).isEqualTo(USER_1_LAST_LOGIN_TIME.getTimeOpt());
    }

    @Test
    void userTwoDataIsFetchedFromNestedProvider() {
      givenNestedProviderReturns(USER_2_LAST_LOGIN_TIME);
      Optional<OffsetDateTime> actual = underTest.getForUserId(USER_2_LAST_LOGIN_TIME.getUserId());

      then(nestedLoginTimeProvider).should().getForUserId(USER_2_LAST_LOGIN_TIME.getUserId());

      assertThat(actual).isEqualTo(USER_2_LAST_LOGIN_TIME.getTimeOpt());
    }
  }
}
