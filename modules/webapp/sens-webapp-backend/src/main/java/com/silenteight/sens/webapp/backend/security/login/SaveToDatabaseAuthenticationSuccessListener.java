package com.silenteight.sens.webapp.backend.security.login;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.kernel.security.SensUserDetails;
import com.silenteight.sens.webapp.users.user.UserService;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
public class SaveToDatabaseAuthenticationSuccessListener implements
    ApplicationListener<AbstractAuthenticationEvent> {

  @NonNull
  private final UserService userService;

  @Override
  public void onApplicationEvent(@NotNull AbstractAuthenticationEvent authenticationEvent) {
    if (authenticationEvent instanceof AuthenticationSuccessEvent) {
      AuthenticationSuccessEvent successEvent = (AuthenticationSuccessEvent) authenticationEvent;
      saveToDb(successEvent.getAuthentication().getPrincipal());
    }
  }

  private void saveToDb(Object principal) {
    if (principal instanceof SensUserDetails) {
      SensUserDetails userDetails = (SensUserDetails) principal;
      if (userDetails.getUserId() != null)
        userService.userLoggedIn(userDetails.getUserId());
    }
  }
}
