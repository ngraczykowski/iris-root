package com.silenteight.sens.webapp.backend.user.registration.domain;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.sens.webapp.backend.user.registration.domain.CompletedUserRegistration.NewUserCredentials;
import com.silenteight.sens.webapp.backend.user.registration.domain.CompletedUserRegistration.NewUserDetails;

import java.util.Set;

@Value
public class NewUserRegistration {

  @NonNull
  String username;
  @NonNull
  NewUserDetails userDetails;
  @NonNull
  NewUserCredentials credentials;

  Set<String> getRoles() {
    return userDetails.getRoles();
  }
}
