package com.silenteight.sens.webapp.user.registration.domain;

import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

@Value
public class NewUserDetails {

  String username;
  String displayName;
  @ToString.Exclude
  @JsonIgnore
  Credentials credentials;
  Set<String> roles;

  public NewUserDetails(String username, String displayName, Set<String> roles) {
    this.username = username;
    this.displayName = displayName;
    this.roles = roles;
    this.credentials = null;
  }

  public NewUserDetails(
      String username, String displayName,
      Credentials credentials, Set<String> roles) {
    this.username = username;
    this.displayName = displayName;
    this.credentials = credentials;
    this.roles = roles;
  }

  @Value
  public static class Credentials {

    @NonNull
    @ToString.Exclude
    String password;
  }
}
