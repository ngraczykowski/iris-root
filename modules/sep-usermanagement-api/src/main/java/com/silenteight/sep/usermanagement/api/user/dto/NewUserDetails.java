package com.silenteight.sep.usermanagement.api.user.dto;

import lombok.*;

import com.silenteight.sep.usermanagement.api.role.UserRoles;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@NoArgsConstructor
public class NewUserDetails {

  private static final long serialVersionUID = 2440274680818692740L;

  String username;
  String displayName;
  @ToString.Exclude
  @JsonIgnore
  Credentials credentials;
  UserRoles roles;

  public NewUserDetails(String username, String displayName, UserRoles roles) {
    this.username = username;
    this.displayName = displayName;
    this.credentials = null;
    this.roles = roles;
  }

  public NewUserDetails(
      String username, String displayName, Credentials credentials, UserRoles roles) {

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
