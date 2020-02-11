package com.silenteight.sens.webapp.keycloak.authentication;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

class KeycloakWebappUserDetails implements WebappUserDetails {

  private static final long serialVersionUID = 2457351476696048646L;

  private final Set<GrantedAuthority> grantedAuthorities;
  private final AccessToken accessToken;

  KeycloakWebappUserDetails(
      Set<GrantedAuthority> grantedAuthorities, KeycloakAuthenticationToken authToken) {
    this.grantedAuthorities = grantedAuthorities;
    this.accessToken = authToken.getAccount().getKeycloakSecurityContext().getToken();
  }

  @Override
  public String getDisplayName() {
    return accessToken.getNickName();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return grantedAuthorities;
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return accessToken.getPreferredUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return accessToken.isActive();
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
