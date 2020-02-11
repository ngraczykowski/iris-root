package com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime;

import one.util.streamex.EntryStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Map;

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.LastLoginTimeCacheUpdaterTest.LastLoginTimeCacheUpdaterFixtures.CACHE_SIZE;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.LastLoginTimeCacheUpdaterTest.LastLoginTimeCacheUpdaterFixtures.FETCHED_LOGIN_TIMES;
import static java.time.OffsetDateTime.parse;
import static java.util.Map.of;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class LastLoginTimeCacheUpdaterTest {

  @InjectMocks
  private LastLoginTimeCacheUpdater underTest;

  @Mock
  private LastLoginTimeBulkFetcher lastLoginTimeBulkFetcher;

  @Mock
  private CachedLastLoginTimeProvider cachedLastLoginTimeProvider;

  @Test
  void updatesCorrectly() {
    given(cachedLastLoginTimeProvider.getCacheSize()).willReturn(CACHE_SIZE);
    given(lastLoginTimeBulkFetcher.fetch(CACHE_SIZE)).willReturn(FETCHED_LOGIN_TIMES);

    underTest.update();

    EntryStream.of(FETCHED_LOGIN_TIMES)
        .forKeyValue(
            (userId, time) -> then(cachedLastLoginTimeProvider)
                .should().update(userId, time));
  }

  static class LastLoginTimeCacheUpdaterFixtures {

    static final int CACHE_SIZE = 10;

    static final Map<String, OffsetDateTime> FETCHED_LOGIN_TIMES = of(
        "userId1", parse("2011-12-03T10:15:30+01:00"),
        "userId2", parse("2011-12-21T10:15:30+01:00")
    );
  }
}
