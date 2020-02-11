package com.silenteight.sens.webapp.keycloak.authorization;

import lombok.RequiredArgsConstructor;

import org.keycloak.AuthorizationContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@RequiredArgsConstructor
class KeycloakPermissionEvaluator implements PermissionEvaluator {

  private final NotSupportedAccessStrategy notSupportedAccessStrategy;

  @Override
  public boolean hasPermission(
      Authentication authentication, Object targetDomainObject, Object permission) {
    boolean supports = authentication instanceof KeycloakAuthenticationToken
        && targetDomainObject instanceof Secured
        && permission instanceof String;

    if (!supports)
      return notSupportedAccessStrategy.getDecision();

    AuthorizationContext authorizationContext = getAuthorizationContext(
        (KeycloakAuthenticationToken) authentication);
    String resourceIdentity = getAccessObjectIdentity(targetDomainObject);

    return authorizationContext.hasPermission(resourceIdentity, (String) permission);
  }

  private static String getAccessObjectIdentity(Object targetDomainObject) {
    return ((Secured) targetDomainObject).getAuthorizationIdentity();
  }

  private static AuthorizationContext getAuthorizationContext(
      KeycloakAuthenticationToken keycloakAuthenticationToken) {
    return keycloakAuthenticationToken
        .getAccount()
        .getKeycloakSecurityContext()
        .getAuthorizationContext();
  }

  @Override
  public boolean hasPermission(
      Authentication authentication, Serializable resourceId, String targetType, Object scope) {
    boolean supports = authentication instanceof KeycloakAuthenticationToken
        && resourceId instanceof String
        && scope instanceof String;

    if (!supports)
      return notSupportedAccessStrategy.getDecision();

    AuthorizationContext authorizationContext = getAuthorizationContext(
        (KeycloakAuthenticationToken) authentication);

    return authorizationContext.hasPermission((String) resourceId, (String) scope);
  }
}
