package com.silenteight.sep.usermanagement.keycloak.lock;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.usermanagement.api.UserLocker;
import com.silenteight.sep.usermanagement.keycloak.retrieval.KeycloakUserRetriever;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakUserLockConfiguration {

  @Bean
  UserLocker keycloakUserLocker(KeycloakUserRetriever keycloakUserRetriever) {
    return new KeycloakUserLocker(keycloakUserRetriever, DefaultTimeSource.INSTANCE);
  }
}
