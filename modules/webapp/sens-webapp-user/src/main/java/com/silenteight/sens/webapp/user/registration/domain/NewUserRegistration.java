package com.silenteight.sens.webapp.user.registration.domain;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.sens.webapp.user.domain.UserOrigin;

import java.util.Set;

@Value
public class NewUserRegistration {

  @NonNull
  NewUserDetails userDetails;
  @NonNull
  UserOrigin origin;

  public String getUsername() {
    return getUserDetails().getUsername();
  }

  public String getDisplayName() {
    return getUserDetails().getDisplayName();
  }

  public boolean hasRoles() {
    return !getRoles().isEmpty();
  }

  public Set<String> getRoles() {
    return getUserDetails().getRoles();
  }
}
