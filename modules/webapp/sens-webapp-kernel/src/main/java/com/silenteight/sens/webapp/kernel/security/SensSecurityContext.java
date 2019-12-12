package com.silenteight.sens.webapp.kernel.security;

import lombok.AllArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import javax.annotation.Nullable;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

public final class SensSecurityContext {

  private static final ThreadLocal<CurrentUser> CURRENT_USER = new ThreadLocal<>();

  public static Optional<SensUserDetails> currentUser() {
    CurrentUser currentUser = CURRENT_USER.get();

    if (currentUser != null) {
      return ofNullable(currentUser.value);
    } else {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null)
        return empty();

      return ofNullable((SensUserDetails) authentication.getPrincipal());
    }
  }

  public static SensUserDetails requireUser() {
    return currentUser().orElseThrow(UnauthenticatedException::new);
  }

  public static void setUserForTesting(@Nullable SensUserDetails user) {
    CURRENT_USER.set(new CurrentUser(user));
  }

  public static void clearUserForTesting() {
    CURRENT_USER.set(new CurrentUser(null));
  }

  private SensSecurityContext() {
  }

  @AllArgsConstructor
  private static final class CurrentUser {
    SensUserDetails value;
  }
}
