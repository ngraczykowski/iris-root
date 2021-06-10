package com.silenteight.sens.webapp.user.registration.domain;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.sep.usermanagement.api.NewUserDetails;
import com.silenteight.sep.usermanagement.api.NewUserDetails.Credentials;

import java.util.Set;

@Value
public class NewUserRegistration {

  @NonNull
  NewUserDetails userDetails;
  @NonNull
  String origin;

  public String getUsername() {
    return getUserDetails().getUsername();
  }

  public String getDisplayName() {
    return getUserDetails().getDisplayName();
  }

  public Credentials getCredentials() {
    return getUserDetails().getCredentials();
  }

  public boolean hasRoles(String rolesScope) {
    return !getRoles(rolesScope).isEmpty();
  }

  public Set<String> getRoles(String rolesScope) {
    return getUserDetails()
        .getRoles()
        .getRoles(rolesScope);
  }
}
