package com.silenteight.sens.webapp.security.acl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import static com.silenteight.sens.webapp.kernel.OrderingConstants.GLOBAL_METHOD_SECURITY_ORDER;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, order = GLOBAL_METHOD_SECURITY_ORDER)
class AclMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

  /**
   * need to wait for ServletContext to be set:
   * https://github.com/spring-projects/spring-boot/issues/14270
   */
  @Lazy
  @Autowired
  private PermissionEvaluator permissionEvaluator;

  @Override
  protected MethodSecurityExpressionHandler createExpressionHandler() {
    DefaultMethodSecurityExpressionHandler expressionHandler
        = new DefaultMethodSecurityExpressionHandler();
    expressionHandler.setPermissionEvaluator(permissionEvaluator);
    return expressionHandler;
  }
}
