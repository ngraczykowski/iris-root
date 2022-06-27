package com.silenteight.sens.webapp.user.registration;

import lombok.NonNull;
import lombok.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.OffsetDateTime;
import java.util.Set;

@Value
public class CreatedUserDetails {

  @NonNull
  UserDetails userDetails;
  @NonNull
  String origin;
  @NonNull
  OffsetDateTime registrationDate;

  @JsonIgnore
  public Set<String> getRoles() {
    return userDetails.getRoles();
  }

  @Value
  public static class UserDetails {

    @NonNull
    String username;
    String displayName;
    @NonNull
    ObfuscatedCredentials credentials;
    @NonNull
    Set<String> roles;
  }

  @Value
  public static class ObfuscatedCredentials {

    private static final String OBFUSCATED_STRING = "*********";

    @NonNull
    String password = OBFUSCATED_STRING;
  }
}
