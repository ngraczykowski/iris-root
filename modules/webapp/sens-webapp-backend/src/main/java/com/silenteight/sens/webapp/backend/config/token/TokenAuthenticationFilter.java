package com.silenteight.sens.webapp.backend.config.token;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public final class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private static final String BEARER = "Bearer";

public TokenAuthenticationFilter(RequestMatcher requiresAuth) {
  super(requiresAuth);
}

  @Override
  protected boolean requiresAuthentication(HttpServletRequest req, HttpServletResponse res) {
    return req.getHeader(AUTHORIZATION) != null;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) {

    String token = getApiToken(request);
    Authentication auth = new UsernamePasswordAuthenticationToken(token, token);

    return getAuthenticationManager().authenticate(auth);
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) throws IOException, ServletException {

    super.successfulAuthentication(request, response, chain, authResult);
    chain.doFilter(request, response);
  }

  private static String getApiToken(HttpServletRequest request) {
    return Optional
        .ofNullable(request.getHeader(AUTHORIZATION))
        .map(value -> removeStart(value, BEARER))
        .map(String::trim)
        .orElseThrow(() -> new BadCredentialsException("Missing Authentication Token"));
  }
}
