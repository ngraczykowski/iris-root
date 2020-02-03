package com.silenteight.sens.webapp.backend.user.registration.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.Set;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CompletedUserRegistration {

  String username;
  NewUserDetails userDetails;
  NewUserCredentials credentials;
  OffsetDateTime registrationDate;

  CompletedUserRegistration(
      NewUserRegistration newUserRegistration, OffsetDateTime registrationTime) {
    this(
        newUserRegistration.getUsername(),
        newUserRegistration.getUserDetails(),
        newUserRegistration.getCredentials(),
        registrationTime
    );
  }

  public Set<String> getRoles() {
    return userDetails.getRoles();
  }

  public String getDisplayName() {
    return userDetails.getDisplayName();
  }

  @Value
  public static class NewUserDetails {

    String displayName;
    Set<String> roles;
  }

  @Value
  public static class NewUserCredentials {

    @ToString.Exclude
    String password;
  }
}
