package com.silenteight.hsbc.bridge.auth;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Collections;

@RequiredArgsConstructor
@Slf4j
class HsbcJwtAuthorizationProvider implements AuthenticationProvider {

  private final JwtParser parser;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    var jwtAuthentication = (HsbcJwtAuthenticationToken) authentication;
    var token = (String) jwtAuthentication.getCredentials();

    var parsedToken = parse(token);

    //TODO(bgulowaty): do some stuff when we have example and clarification on JWT token

    return jwtAuthentication.authenticated(Collections.emptyList());
  }

  private Jwt parse(String token) {
    try {
      return parser.parse(token);
    } catch (MalformedJwtException | SignatureException | ExpiredJwtException | IllegalArgumentException e) {
      log.error(e.getMessage());
      throw new JwtTokenException(e);
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return HsbcJwtAuthenticationToken.class.isAssignableFrom(authentication);
  }

  @ToString(callSuper = true)
  private static class JwtTokenException extends AuthenticationException {

    private static final long serialVersionUID = -1745121661481798717L;

    JwtTokenException(Throwable cause) {
      super(cause.getMessage(), cause);
    }
  }
}
