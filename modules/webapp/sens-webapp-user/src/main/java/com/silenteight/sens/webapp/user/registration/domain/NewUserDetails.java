package com.silenteight.sens.webapp.user.registration.domain;

import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import java.util.Optional;
import java.util.Set;

@Value
public class NewUserDetails {

  String username;
  String displayName;
  @ToString.Exclude
  Optional<Credentials> credentials;
  Set<String> roles;

  @Value
  public static class Credentials {

    @NonNull
    @ToString.Exclude
    String password;
  }
}
