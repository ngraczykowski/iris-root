package com.silenteight.sep.usermanagement.keycloak.query.lastlogintime;

import com.silenteight.sep.base.common.time.DefaultTimeSource;

import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Duration;

import static java.time.Duration.ofMinutes;

@Configuration
@EnableScheduling
class KeycloakLastLoginTimeConfiguration {

  static final String CACHE_UPDATE_INTERVAL = "PT15M";
  static final String CACHE_FIRST_LOAD_DELAY = "PT5S";

  static final String LOGIN_EVENT_TYPE = "LOGIN";

  static final Duration CACHE_EXPIRATION_DURATION = ofMinutes(30);

  @Bean
  LastLoginTimeBulkFetcher lastLoginTimeBulkFetcher(RealmResource realmResource) {
    return new LastLoginTimeBulkFetcher(realmResource, DefaultTimeSource.TIME_CONVERTER);
  }

  @Bean
  CachedLastLoginTimeProvider cachedLastLoginUserDateProvider(RealmResource realmResource) {
    SingleRequestLoginTimeProvider defaultProvider =
        new SingleRequestLoginTimeProvider(realmResource, DefaultTimeSource.TIME_CONVERTER);

    return new CachedLastLoginTimeProvider(defaultProvider, 10_000, CACHE_EXPIRATION_DURATION);
  }

  @Bean
  LastLoginTimeCacheUpdater lastLoginTimeCacheUpdater(
      CachedLastLoginTimeProvider lastLoginTimeProvider,
      LastLoginTimeBulkFetcher lastLoginTimeBulkFetcher) {
    return new LastLoginTimeCacheUpdater(lastLoginTimeBulkFetcher, lastLoginTimeProvider);
  }
}
