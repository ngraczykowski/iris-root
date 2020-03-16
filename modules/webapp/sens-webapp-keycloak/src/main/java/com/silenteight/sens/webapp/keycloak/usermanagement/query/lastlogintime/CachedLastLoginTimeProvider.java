package com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

class CachedLastLoginTimeProvider implements LastLoginTimeProvider {

  @Getter(value = AccessLevel.PACKAGE)
  private final int cacheSize;

  private final LoadingCache<String, Optional<OffsetDateTime>> cache;

  CachedLastLoginTimeProvider(
      @NonNull LastLoginTimeProvider lastLoginTimeProvider,
      int cacheSize,
      @NonNull Duration cacheExpirationDuration) {

    this.cacheSize = cacheSize;
    this.cache = Caffeine.newBuilder()
        .maximumSize(cacheSize)
        .weakKeys()
        .weakValues()
        .expireAfterWrite(cacheExpirationDuration)
        .build(lastLoginTimeProvider::getForUserId);
  }

  @Override
  public Optional<OffsetDateTime> getForUserId(String userId) {
    return cache.get(userId);
  }

  void update(String userId, OffsetDateTime time) {
    cache.put(userId, Optional.of(time));
  }
}
