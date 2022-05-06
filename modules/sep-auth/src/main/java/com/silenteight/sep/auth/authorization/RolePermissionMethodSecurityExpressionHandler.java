package com.silenteight.sep.auth.authorization;

import lombok.RequiredArgsConstructor;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

import java.util.Map;
import java.util.Set;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
class RolePermissionMethodSecurityExpressionHandler
    extends DefaultMethodSecurityExpressionHandler {

  @NotNull
  private final Map<String, Set<String>> permissionsByMethod;
  @NotNull
  private final PermissionsByRoleProvider permissionsByRoleProvider;

  @Override
  protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
      Authentication authentication, MethodInvocation invocation) {

    RolePermissionMethodSecurityExpressionRoot root =
        new RolePermissionMethodSecurityExpressionRoot(
            authentication, permissionsByMethod, permissionsByRoleProvider);
    root.setThis(invocation.getThis());
    root.setPermissionEvaluator(getPermissionEvaluator());
    root.setTrustResolver(new AuthenticationTrustResolverImpl());
    root.setRoleHierarchy(getRoleHierarchy());
    return root;
  }
}
