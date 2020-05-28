package com.silenteight.sens.webapp.keycloak.usermanagement.lock;

import com.silenteight.sens.webapp.keycloak.usermanagement.retrieval.KeycloakUserRetriever;
import com.silenteight.sens.webapp.user.lock.UserLocker;
import com.silenteight.sep.base.common.time.DefaultTimeSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakUserLockConfiguration {

  @Bean
  UserLocker keycloakUserLocker(KeycloakUserRetriever keycloakUserRetriever) {
    return new KeycloakUserLocker(keycloakUserRetriever, DefaultTimeSource.INSTANCE);
  }
}
