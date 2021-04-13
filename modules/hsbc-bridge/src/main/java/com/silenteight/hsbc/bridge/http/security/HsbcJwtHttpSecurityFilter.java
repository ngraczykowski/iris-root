package com.silenteight.hsbc.bridge.http.security;

import com.silenteight.hsbc.bridge.auth.HsbcJwtAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class HsbcJwtHttpSecurityFilter extends AbstractAuthenticationProcessingFilter {

  private static final String AUTH_HEADER = "Authorization";

  protected HsbcJwtHttpSecurityFilter() {
    super("/**");
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) {
    var headerValue = request.getHeader(AUTH_HEADER);

    if (headerValue == null) {
      throw AuthorizationHeaderException.noHeader();
    }

    if (!headerValue.startsWith("Bearer")) {
      throw AuthorizationHeaderException.invalidHeader();
    }

    var jwtToken = headerValue.substring(7);

    return getAuthenticationManager().authenticate(
        HsbcJwtAuthenticationToken.unauthenticated(jwtToken)
    );
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authResult);

    chain.doFilter(request, response);
  }

  private static class AuthorizationHeaderException extends AuthenticationException {

    private static final long serialVersionUID = -1841560814001824241L;

    private AuthorizationHeaderException(String message) {
      super(message);
    }

    private static AuthorizationHeaderException invalidHeader() {
      return new AuthorizationHeaderException(
          "Authorization header doesn't start with \"Bearer\".");
    }

    private static AuthorizationHeaderException noHeader() {
      return new AuthorizationHeaderException("No authorization header has been found.");
    }
  }
}
