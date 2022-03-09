package com.silenteight.sens.webapp.sso.domain;

import com.silenteight.sep.usermanagement.api.identityprovider.IdentityProviderRoleMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SsoMappingConfiguration {

  @Bean
  SsoMappingService ssoMappingService(
      IdentityProviderRoleMapper identityProviderRoleMapper,
      RoleMappingDtoToSsoMappingDtoConverter roleMappingDtoToSsoMappingDtoConverter) {
    return new SsoMappingService(
        identityProviderRoleMapper, roleMappingDtoToSsoMappingDtoConverter);
  }

  @Bean
  SsoMappingsDetailsQuery ssoMappingQuery(
      IdentityProviderRoleMapper identityProviderRoleMapper,
      RoleMappingDtoToSsoMappingDtoConverter roleMappingDtoToSsoMappingDtoConverter) {
    return new SsoMappingsDetailsQuery(
        identityProviderRoleMapper, roleMappingDtoToSsoMappingDtoConverter);
  }

  @Bean
  RoleMappingDtoToSsoMappingDtoConverter roleMappingDtoToSsoMappingDtoConverter() {
    return new RoleMappingDtoToSsoMappingDtoConverter();
  }
}
