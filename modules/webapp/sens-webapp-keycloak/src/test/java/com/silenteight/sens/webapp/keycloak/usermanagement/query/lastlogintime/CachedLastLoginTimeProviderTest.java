package com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime;

import com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.CachedLastLoginTimeProviderFixtures.LastLoginTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.CachedLastLoginTimeProviderFixtures.USER_1_LAST_LOGIN_TIME;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CachedLastLoginTimeProviderTest {

  private static final int CACHE_SIZE = 1;

  @Mock
  private LastLoginTimeProvider nestedLoginTimeProvider;

  private CachedLastLoginTimeProvider underTest;

  @BeforeEach
  void setUp() {
    underTest = new CachedLastLoginTimeProvider(
        nestedLoginTimeProvider, CACHE_SIZE, Duration.ofMinutes(1));
  }

  private void givenNestedProviderReturns(LastLoginTime lastLoginTime) {
    given(nestedLoginTimeProvider.getForUserId(lastLoginTime.getUserId()))
        .willReturn(lastLoginTime.getTimeOpt());
  }


  @Test
  void requiredNonCachedLastLoginTime_fetchesFromNestedProvider() {
    givenNestedProviderReturns(USER_1_LAST_LOGIN_TIME);

    Optional<OffsetDateTime> actual = underTest.getForUserId(USER_1_LAST_LOGIN_TIME.getUserId());

    then(nestedLoginTimeProvider).should().getForUserId(USER_1_LAST_LOGIN_TIME.getUserId());
    assertThat(actual).contains(USER_1_LAST_LOGIN_TIME.getTime());
  }

  @Test
  void requiredCachedLastLoginTime_returnsFromCache() {
    underTest.update(USER_1_LAST_LOGIN_TIME.getUserId(), USER_1_LAST_LOGIN_TIME.getTime());

    Optional<OffsetDateTime> actual = underTest.getForUserId(USER_1_LAST_LOGIN_TIME.getUserId());

    then(nestedLoginTimeProvider).shouldHaveNoInteractions();
    assertThat(actual).contains(USER_1_LAST_LOGIN_TIME.getTime());
  }
}
