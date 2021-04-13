package com.silenteight.hsbc.bridge.auth;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationProvider;

public class HsbcAuthorizationProviderConfigurer {

  public AuthenticationProvider create() {
    return new HsbcJwtAuthorizationProvider(Jwts.parser());
  }
}
