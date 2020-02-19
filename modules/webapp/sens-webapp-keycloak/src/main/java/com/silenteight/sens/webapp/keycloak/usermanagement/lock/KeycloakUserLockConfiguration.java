package com.silenteight.sens.webapp.keycloak.usermanagement.lock;

import com.silenteight.sens.webapp.keycloak.usermanagement.retrieval.KeycloakUserRetriever;
import com.silenteight.sens.webapp.user.lock.UserLocker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.sens.webapp.common.time.DefaultTimeSource.INSTANCE;

@Configuration
class KeycloakUserLockConfiguration {

  @Bean
  UserLocker keycloakUserLocker(KeycloakUserRetriever keycloakUserRetriever) {
    return new KeycloakUserLocker(keycloakUserRetriever, INSTANCE);
  }
}
