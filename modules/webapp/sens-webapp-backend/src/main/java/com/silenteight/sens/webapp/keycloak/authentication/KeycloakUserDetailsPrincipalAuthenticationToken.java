package com.silenteight.sens.webapp.keycloak.authentication;

import lombok.EqualsAndHashCode;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Extension of {@link KeycloakAuthenticationToken} which uses custom UserDetails as principal. In
 * general, this allows retrieving our custom {@link UserDetails} via {@link
 * org.springframework.security.core.annotation.AuthenticationPrincipal} when using Keycloak.
 */
@EqualsAndHashCode(callSuper = true)
class KeycloakUserDetailsPrincipalAuthenticationToken extends KeycloakAuthenticationToken {

  private static final long serialVersionUID = -487332714100799241L;

  private final KeycloakWebappUserDetails userDetails;

  public KeycloakUserDetailsPrincipalAuthenticationToken(
      KeycloakAuthenticationToken authToken,
      KeycloakWebappUserDetails userDetails) {
    super(
        authToken.getAccount(),
        authToken.isInteractive(),
        userDetails.getAuthorities());

    this.userDetails = userDetails;
  }

  @Override
  public Object getPrincipal() {
    return userDetails;
  }
}
