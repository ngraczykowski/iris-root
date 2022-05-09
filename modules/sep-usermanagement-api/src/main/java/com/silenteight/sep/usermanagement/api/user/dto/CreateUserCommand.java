package com.silenteight.sep.usermanagement.api.user.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.sep.usermanagement.api.role.UserRoles;
import com.silenteight.sep.usermanagement.api.user.dto.NewUserDetails.Credentials;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.OffsetDateTime;

@Value
@RequiredArgsConstructor
public class CreateUserCommand {

  private static final String OBFUSCATED_STRING = "*********";

  @NonNull
  NewUserDetails userDetails;
  @NonNull
  String origin;
  @NonNull
  OffsetDateTime registrationDate;

  @JsonIgnore
  public UserRoles getRoles() {
    return userDetails.getRoles();
  }

  @JsonIgnore
  public String getDisplayName() {
    return userDetails.getDisplayName();
  }

  @JsonIgnore
  public String getUsername() {
    return userDetails.getUsername();
  }

  @JsonIgnore
  public Credentials getCredentials() {
    return userDetails.getCredentials();
  }

  public CreateUserCommand toCompletedUserRegistrationEvent() {
    NewUserDetails userDetailsForEvent = new NewUserDetails(
        userDetails.getUsername(),
        userDetails.getDisplayName(),
        new NewUserDetails.Credentials(OBFUSCATED_STRING),
        userDetails.getRoles());

    return new CreateUserCommand(userDetailsForEvent, origin, registrationDate);
  }
}
