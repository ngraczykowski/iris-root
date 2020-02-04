package com.silenteight.sens.webapp.backend.user.registration.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.sens.webapp.backend.user.registration.domain.NewUserDetails.Credentials;

import java.time.OffsetDateTime;
import java.util.Set;

@Value
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CompletedUserRegistration {

  NewUserDetails userDetails;
  OffsetDateTime registrationDate;

  public Set<String> getRoles() {
    return userDetails.getRoles();
  }

  public String getDisplayName() {
    return userDetails.getDisplayName();
  }

  public String getUsername() {
    return userDetails.getUsername();
  }

  public Credentials getCredentials() {
    return userDetails.getCredentials();
  }
}
