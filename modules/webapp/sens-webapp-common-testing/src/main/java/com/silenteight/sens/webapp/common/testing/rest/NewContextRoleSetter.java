package com.silenteight.sens.webapp.common.testing.rest;

import com.silenteight.sens.webapp.common.testing.rest.testwithrole.SecurityContextRoleSetter;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.TestSecurityContextHolder;

import java.util.List;

import static java.util.stream.Collectors.toList;

class NewContextRoleSetter implements SecurityContextRoleSetter {

  @Override
  public void setRoles(List<String> roles) {
    TestingAuthenticationToken authentication =
        new TestingAuthenticationToken(null, null, toAuthorityRoles(roles));

    TestSecurityContextHolder.setAuthentication(authentication);
  }

  private static List<GrantedAuthority> toAuthorityRoles(List<String> roles) {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role))
        .collect(toList());
  }
}
