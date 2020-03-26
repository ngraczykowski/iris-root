package com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.vavr.control.Try;
import org.springframework.scheduling.annotation.Scheduled;

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.KeycloakLastLoginTimeConfiguration.CACHE_FIRST_LOAD_DELAY;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.KeycloakLastLoginTimeConfiguration.CACHE_UPDATE_INTERVAL;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static one.util.streamex.EntryStream.of;

@RequiredArgsConstructor
@Slf4j
class LastLoginTimeCacheUpdater {

  private final LastLoginTimeBulkFetcher lastLoginTimeBulkFetcher;
  private final CachedLastLoginTimeProvider cachedLastLoginTimeProvider;

  @Scheduled(fixedDelayString = CACHE_UPDATE_INTERVAL, initialDelayString = CACHE_FIRST_LOAD_DELAY)
  void update() {
    log.debug(USER_MANAGEMENT, "Updating lastLoginTime cache");

    int cacheSize = cachedLastLoginTimeProvider.getCacheSize();

    Try.run(() -> doUpdateCache(cacheSize))
        .onFailure(e -> log.error(USER_MANAGEMENT, "Could not update lastLoginTime cache", e))
        .onSuccess(ignored -> log.debug(USER_MANAGEMENT, "LastLoginTime cache updated"));
  }

  private void doUpdateCache(int cacheSize) {
    of(lastLoginTimeBulkFetcher.fetch(cacheSize)).forKeyValue(cachedLastLoginTimeProvider::update);
  }
}
