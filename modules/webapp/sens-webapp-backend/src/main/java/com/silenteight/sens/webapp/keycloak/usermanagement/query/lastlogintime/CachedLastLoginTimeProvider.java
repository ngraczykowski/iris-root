package com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

import static one.util.streamex.EntryStream.of;

class CachedLastLoginTimeProvider implements LastLoginTimeProvider {

  private final LastLoginTimeBulkFetcher lastLoginTimeBulkFetcher;

  @Getter(value = AccessLevel.PACKAGE)
  private final int cacheSize;

  private final LoadingCache<String, Optional<OffsetDateTime>> cache;

  CachedLastLoginTimeProvider(
      @NonNull LastLoginTimeProvider lastLoginTimeProvider,
      @NonNull LastLoginTimeBulkFetcher lastLoginTimeBulkFetcher,
      int cacheSize,
      Duration cacheExpirationDuration) {

    this.lastLoginTimeBulkFetcher = lastLoginTimeBulkFetcher;
    this.cacheSize = cacheSize;
    this.cache = Caffeine.newBuilder()
        .maximumSize(cacheSize)
        .weakKeys()
        .weakValues()
        .expireAfterWrite(cacheExpirationDuration)
        .build(lastLoginTimeProvider::getForUserId);

    loadInitialData();
  }

  private void loadInitialData() {
    of(lastLoginTimeBulkFetcher.fetch(cacheSize))
        .mapValues(Optional::of)
        .forKeyValue(cache::put);
  }

  @Override
  public Optional<OffsetDateTime> getForUserId(String userId) {
    return cache.get(userId);
  }

  void update(String userId, OffsetDateTime time) {
    cache.put(userId, Optional.of(time));
  }
}
