package com.silenteight.sens.auth.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
@Slf4j
class KeycloakUserDetailsAuthenticationProvider implements AuthenticationProvider {

  private final KeycloakAuthenticationProvider keycloakAuthenticationProvider;
  private final KeycloakAuthoritiesExtractor keycloakAuthoritiesExtractor;

  @Override
  public Authentication authenticate(Authentication authentication) {
    KeycloakAuthenticationToken token =
        (KeycloakAuthenticationToken) keycloakAuthenticationProvider.authenticate(authentication);

    if (token == null)
      return null;

    KeycloakWebappUserDetails userDetails = new KeycloakWebappUserDetails(
        keycloakAuthoritiesExtractor.extract(token), token);

    return new KeycloakUserDetailsPrincipalAuthenticationToken(token, userDetails);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return keycloakAuthenticationProvider.supports(authentication);
  }
}
