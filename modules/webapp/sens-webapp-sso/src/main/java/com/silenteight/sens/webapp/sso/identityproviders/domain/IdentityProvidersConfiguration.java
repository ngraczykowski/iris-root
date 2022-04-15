package com.silenteight.sens.webapp.sso.identityproviders.domain;

import com.silenteight.sep.usermanagement.api.identityprovider.IdentityProviderQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class IdentityProvidersConfiguration {

  @Bean
  IdentityProvidersQuery identityProvidersQuery(
      IdentityProviderQuery identityProviderQuery) {

    return new IdentityProvidersQuery(identityProviderQuery);
  }
}
