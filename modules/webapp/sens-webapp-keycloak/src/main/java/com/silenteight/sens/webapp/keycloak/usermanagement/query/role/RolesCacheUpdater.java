package com.silenteight.sens.webapp.keycloak.usermanagement.query.role;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.KeycloakRolesConfiguration.CACHE_UPDATE_INTERVAL;
import static one.util.streamex.EntryStream.of;

@RequiredArgsConstructor
@Slf4j
class RolesCacheUpdater {

  @NonNull
  private final RolesFetcher rolesFetcher;
  @NonNull
  private final CachedRolesProvider cachedRolesProvider;

  @Scheduled(fixedDelayString = CACHE_UPDATE_INTERVAL)
  void update() {
    log.debug("Updating roles cache");
    of(rolesFetcher.fetch()).forKeyValue(cachedRolesProvider::update);
  }
}
