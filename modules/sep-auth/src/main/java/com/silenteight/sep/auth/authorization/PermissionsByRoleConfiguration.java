package com.silenteight.sep.auth.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PermissionsByRoleConfiguration {

  @Bean
  StaticPermissionsByRoleProvider propertiesPermissionsByRoleProvider(
      AuthorizationProperties properties) {

    return new StaticPermissionsByRoleProvider(properties.permissionsByRole());
  }
}
