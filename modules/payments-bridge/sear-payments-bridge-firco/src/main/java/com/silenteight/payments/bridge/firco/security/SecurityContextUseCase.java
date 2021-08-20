package com.silenteight.payments.bridge.firco.security;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

@RequiredArgsConstructor
class SecurityContextUseCase {

  private final Collection<? extends GrantedAuthority> grantedAuthorities;

  void setAuthentication(String username, String password) {
    SecurityContextHolder
        .getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken(username,
            password,
            grantedAuthorities));
  }
}
