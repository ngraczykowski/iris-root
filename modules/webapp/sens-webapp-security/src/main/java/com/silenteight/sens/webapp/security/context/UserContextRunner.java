package com.silenteight.sens.webapp.security.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.kernel.security.SensUserDetails;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserContextRunner {

  public static void runAs(String username, Set<GrantedAuthority> authorities, Runnable function) {
    SensUserDetails userDetails = createUserDetails(username, authorities);
    runAs(userDetails, function);
  }

  public static void runAs(SensUserDetails userDetails, Runnable function) {
    final SecurityContext originalContext = SecurityContextHolder.getContext();
    signInAs(userDetails);

    try {
      function.run();
    } finally {
      restoreSecurityContext(originalContext);
    }
  }

  private static SensUserDetails createUserDetails(
      String username, Set<GrantedAuthority> authorities) {

    return SensUserDetails
        .builder()
        .username(username)
        .authorities(authorities)
        .superUser(false)
        .build();
  }

  private static void signInAs(SensUserDetails userDetails) {
    Authentication authentication = new SensUserDetailsAuthentication(userDetails);
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
  }

  private static void restoreSecurityContext(final SecurityContext context) {
    SecurityContextHolder.setContext(context);
  }

  private static class SensUserDetailsAuthentication implements Authentication {

    private static final long serialVersionUID = -5452271580754915990L;

    private final SensUserDetails userDetails;
    private boolean authenticated;

    SensUserDetailsAuthentication(@NonNull SensUserDetails userDetails) {
      this.userDetails = userDetails;
      this.authenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
      return null;
    }

    @Override
    public Object getDetails() {
      return null;
    }

    @Override
    public Object getPrincipal() {
      return userDetails;
    }

    @Override
    public boolean isAuthenticated() {
      return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
      authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
      return userDetails.getUsername();
    }
  }
}
