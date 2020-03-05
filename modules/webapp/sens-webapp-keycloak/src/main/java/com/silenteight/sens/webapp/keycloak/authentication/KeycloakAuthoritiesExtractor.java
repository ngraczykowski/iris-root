package com.silenteight.sens.webapp.keycloak.authentication;

import lombok.RequiredArgsConstructor;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessToken.Access;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.concat;

@RequiredArgsConstructor
class KeycloakAuthoritiesExtractor {

  private final AuthorityNameNormalizer roleNameNormalizer;
  private final AuthorityNameNormalizer authorityNormalizer;

  Set<GrantedAuthority> extract(KeycloakAuthenticationToken authenticationToken) {
    KeycloakSecurityContext keycloakSecurityContext =
        (KeycloakSecurityContext) authenticationToken.getCredentials();

    AccessToken accessToken = keycloakSecurityContext.getToken();

    return concat(
        globalRoles(accessToken),
        backendAuthorities(authenticationToken)
    ).collect(toSet());
  }


  private Stream<GrantedAuthority> backendAuthorities(KeycloakAuthenticationToken token) {
    return token.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .map(authorityNormalizer::normalize)
        .map(SimpleGrantedAuthority::new);
  }

  private Stream<GrantedAuthority> globalRoles(AccessToken token) {
    return ofNullable(token.getRealmAccess())
        .map(Access::getRoles)
        .orElse(emptySet())
        .stream()
        .map(roleNameNormalizer::normalize)
        .map(SimpleGrantedAuthority::new);
  }
}
