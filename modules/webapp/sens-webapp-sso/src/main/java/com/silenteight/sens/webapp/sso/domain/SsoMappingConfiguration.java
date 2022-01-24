package com.silenteight.sens.webapp.sso.domain;

import com.silenteight.sep.usermanagement.api.IdentityProviderRoleMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SsoMappingConfiguration {

  @Bean
  SsoMappingService ssoMappingService(
      IdentityProviderRoleMapper identityProviderRoleMapper) {
    return new SsoMappingService(identityProviderRoleMapper);
  }
}
