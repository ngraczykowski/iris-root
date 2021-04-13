package com.silenteight.hsbc.bridge.auth;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HsbcJwtAuthenticationToken extends AbstractAuthenticationToken {

  private static final long serialVersionUID = -860845851077688905L;
  private final String token;

  private HsbcJwtAuthenticationToken(
      Collection<? extends GrantedAuthority> authorities,
      String token, boolean isAuthenticated) {
    super(authorities);
    this.token = token;
    setAuthenticated(isAuthenticated);
  }

  public static HsbcJwtAuthenticationToken unauthenticated(String token) {
    return new HsbcJwtAuthenticationToken(Collections.emptyList(), token, false);
  }

  HsbcJwtAuthenticationToken authenticated(Collection<? extends GrantedAuthority> authorities) {
    return new HsbcJwtAuthenticationToken(authorities, token, true);
  }


  @Override
  public Object getCredentials() {
    return token;
  }

  @Override
  public Object getPrincipal() {
    return null;
  }
}
