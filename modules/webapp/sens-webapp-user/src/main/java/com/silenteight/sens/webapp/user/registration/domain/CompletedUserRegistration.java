package com.silenteight.sens.webapp.user.registration.domain;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.sens.webapp.user.registration.domain.NewUserDetails.Credentials;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;

@Value
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CompletedUserRegistration {

  @NonNull
  NewUserDetails userDetails;
  @NonNull
  String origin;
  @NonNull
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

  public Optional<Credentials> getCredentials() {
    return userDetails.getCredentials();
  }
}
