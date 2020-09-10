package com.silenteight.sep.keycloak.lock;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.keycloak.retrieval.KeycloakUserRetriever;

import com.sillenteight.sep.usermanagement.api.UserLocker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakUserLockConfiguration {

  @Bean
  UserLocker keycloakUserLocker(KeycloakUserRetriever keycloakUserRetriever) {
    return new KeycloakUserLocker(keycloakUserRetriever, DefaultTimeSource.INSTANCE);
  }
}
