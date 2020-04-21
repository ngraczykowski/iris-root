package com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.audit.api.AuditLog;

import io.vavr.control.Try;
import org.springframework.scheduling.annotation.Scheduled;

import static com.silenteight.sens.webapp.audit.api.AuditMarker.USER_MANAGEMENT;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.KeycloakLastLoginTimeConfiguration.CACHE_FIRST_LOAD_DELAY;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.KeycloakLastLoginTimeConfiguration.CACHE_UPDATE_INTERVAL;
import static one.util.streamex.EntryStream.of;

@RequiredArgsConstructor
class LastLoginTimeCacheUpdater {

  private final LastLoginTimeBulkFetcher lastLoginTimeBulkFetcher;
  private final CachedLastLoginTimeProvider cachedLastLoginTimeProvider;
  private final AuditLog auditLog;

  @Scheduled(fixedDelayString = CACHE_UPDATE_INTERVAL, initialDelayString = CACHE_FIRST_LOAD_DELAY)
  void update() {
    auditLog.logInfo(USER_MANAGEMENT, "Updating lastLoginTime cache");

    int cacheSize = cachedLastLoginTimeProvider.getCacheSize();

    Try.run(() -> doUpdateCache(cacheSize))
        .onFailure(
            e -> auditLog.logError(USER_MANAGEMENT, "Could not update lastLoginTime cache", e))
        .onSuccess(ignored -> auditLog.logInfo(USER_MANAGEMENT, "LastLoginTime cache updated"));
  }

  private void doUpdateCache(int cacheSize) {
    of(lastLoginTimeBulkFetcher.fetch(cacheSize)).forKeyValue(cachedLastLoginTimeProvider::update);
  }
}
