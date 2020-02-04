package com.silenteight.sens.webapp.backend.user.registration.domain;

import lombok.ToString;
import lombok.Value;

import java.util.Set;

@Value
public class NewUserDetails {

  String username;
  String displayName;
  @ToString.Exclude
  Credentials credentials;
  Set<String> roles;

  @Value
  public static class Credentials {

    @ToString.Exclude
    String password;
  }
}
