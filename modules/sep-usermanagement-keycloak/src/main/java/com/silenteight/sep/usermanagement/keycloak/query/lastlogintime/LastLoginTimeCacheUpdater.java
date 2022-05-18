package com.silenteight.sep.usermanagement.keycloak.query.lastlogintime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.usermanagement.keycloak.logging.LogMarkers;

import org.springframework.scheduling.annotation.Scheduled;

import static one.util.streamex.EntryStream.of;

@RequiredArgsConstructor
@Slf4j
class LastLoginTimeCacheUpdater {

  private final LastLoginTimeBulkFetcher lastLoginTimeBulkFetcher;
  private final CachedLastLoginTimeProvider cachedLastLoginTimeProvider;

  @Scheduled(fixedDelayString = KeycloakLastLoginTimeConfiguration.CACHE_UPDATE_INTERVAL,
      initialDelayString = KeycloakLastLoginTimeConfiguration.CACHE_FIRST_LOAD_DELAY)
  void update() {
    log.info("Updating lastLoginTime cache");

    int cacheSize = cachedLastLoginTimeProvider.getCacheSize();
    try {
      doUpdateCache(cacheSize);
      log.info(LogMarkers.USER_MANAGEMENT, "LastLoginTime cache updated");
    } catch (RuntimeException e) {
      log.error(LogMarkers.USER_MANAGEMENT, "Could not update lastLoginTime cache", e);
    }
  }

  private void doUpdateCache(int cacheSize) {
    of(lastLoginTimeBulkFetcher.fetch(cacheSize)).forKeyValue(cachedLastLoginTimeProvider::update);
  }
}
