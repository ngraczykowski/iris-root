package com.silenteight.sens.webapp.sso.identityproviders.domain;

import com.silenteight.sep.usermanagement.api.IdentityProviderRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class IdentityProvidersConfiguration {

  @Bean
  IdentityProvidersQuery identityProvidersQuery(
      IdentityProviderRepository identityProviderRepository) {

    return new IdentityProvidersQuery(identityProviderRepository) {};
  }
}
