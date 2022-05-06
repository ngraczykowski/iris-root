package com.silenteight.sep.auth.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties({ AuthorizationProperties.class })
class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

  @Autowired
  private AuthorizationProperties authorizationProperties;

  @Autowired
  private PermissionsByRoleProvider permissionsByRoleProvider;

  @Override
  protected MethodSecurityExpressionHandler createExpressionHandler() {
    return new RolePermissionMethodSecurityExpressionHandler(
        authorizationProperties.permissionsByMethod(), permissionsByRoleProvider);
  }
}
