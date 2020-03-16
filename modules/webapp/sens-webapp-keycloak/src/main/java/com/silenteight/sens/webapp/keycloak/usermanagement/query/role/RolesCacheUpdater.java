package com.silenteight.sens.webapp.keycloak.usermanagement.query.role;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.vavr.control.Try;
import org.springframework.scheduling.annotation.Scheduled;

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.KeycloakRolesConfiguration.CACHE_FIRST_LOAD_DELAY;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.role.KeycloakRolesConfiguration.CACHE_UPDATE_INTERVAL;
import static one.util.streamex.EntryStream.of;

@RequiredArgsConstructor
@Slf4j
class RolesCacheUpdater {

  @NonNull
  private final RolesFetcher rolesFetcher;
  @NonNull
  private final CachedRolesProvider cachedRolesProvider;

  @Scheduled(fixedDelayString = CACHE_UPDATE_INTERVAL, initialDelayString = CACHE_FIRST_LOAD_DELAY)
  void update() {
    log.debug("Updating roles cache");

    Try.run(this::doUpdateCache)
        .onFailure(e -> log.error("Could not update roles cache", e));
  }

  private void doUpdateCache() {
    of(rolesFetcher.fetch()).forKeyValue(cachedRolesProvider::update);
  }
}
