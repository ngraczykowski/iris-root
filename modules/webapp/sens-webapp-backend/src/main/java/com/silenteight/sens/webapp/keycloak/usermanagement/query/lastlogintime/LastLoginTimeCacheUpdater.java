package com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.KeycloakLastLoginTimeConfiguration.CACHE_UPDATE_INTERVAL;
import static one.util.streamex.EntryStream.of;

@RequiredArgsConstructor
@Slf4j
class LastLoginTimeCacheUpdater {

  private final LastLoginTimeBulkFetcher lastLoginTimeBulkFetcher;
  private final CachedLastLoginTimeProvider cachedLastLoginTimeProvider;

  @Scheduled(fixedDelayString = CACHE_UPDATE_INTERVAL)
  void update() {
    log.debug("Updating lastLoginTime cache");

    int cacheSize = cachedLastLoginTimeProvider.getCacheSize();

    of(lastLoginTimeBulkFetcher.fetch(cacheSize))
        .forKeyValue(cachedLastLoginTimeProvider::update);
  }
}
