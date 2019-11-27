package com.silenteight.sens.webapp.backend.config.token;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.kernel.security.SensUserDetails;
import com.silenteight.sens.webapp.kernel.security.authority.Role;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static lombok.AccessLevel.PACKAGE;

@RequiredArgsConstructor(access = PACKAGE)
public final class AdminTokenAuthenticationProvider
    extends AbstractUserDetailsAuthenticationProvider {

  @NonNull
  private final AdminTokenSecurity adminTokenSecurity;

  @Override
  protected void additionalAuthenticationChecks(
      UserDetails userDetails, UsernamePasswordAuthenticationToken auth) {
    // nothing to do
  }

  @Override
  protected UserDetails retrieveUser(
      String userName, UsernamePasswordAuthenticationToken authentication) {

    Object token = authentication.getCredentials();

    if (!adminTokenSecurity.isAdminToken(token.toString()))
      throw new UsernameNotFoundException("Cannot find admin with authentication token=" + token);

    return createUserDetails();
  }

  private UserDetails createUserDetails() {
    return SensUserDetails
        .builder()
        .username(adminTokenSecurity.getAdminUserName())
        .authorities(Role.getAllGrantedAuthorities())
        .superUser(true)
        .build();
  }
}
