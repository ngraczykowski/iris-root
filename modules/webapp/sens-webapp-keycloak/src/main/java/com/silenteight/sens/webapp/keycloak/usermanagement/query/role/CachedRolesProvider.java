package com.silenteight.sens.webapp.keycloak.usermanagement.query.role;

import lombok.NonNull;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.time.Duration;
import java.util.List;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

class CachedRolesProvider implements RolesProvider {

  private final LoadingCache<String, @NonNull List<String>> cache;

  CachedRolesProvider(
      @NonNull RolesProvider rolesProvider,
      int cacheSize,
      @NonNull Duration cacheExpirationDuration) {

    this.cache = Caffeine.newBuilder()
        .maximumSize(cacheSize)
        .expireAfterWrite(cacheExpirationDuration)
        .build(rolesProvider::getForUserId);
  }

  @Override
  @NonNull
  public List<String> getForUserId(String userId) {

    return ofNullable(cache.get(userId))
        .orElseThrow(CouldNotFetchRolesException::new)
        .stream()
        .sorted()
        .collect(toList());
  }

  void update(String userId, List<String> roles) {
    cache.put(userId, roles);
  }
}
