package com.silenteight.sens.webapp.user.registration.domain;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.sens.webapp.user.domain.UserOrigin;
import com.silenteight.sens.webapp.user.registration.domain.NewUserDetails.Credentials;

import java.util.Optional;
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

  public Optional<Credentials> getCredentials() {
    return getUserDetails().getCredentials();
  }

  public boolean hasRoles() {
    return !getRoles().isEmpty();
  }

  public Set<String> getRoles() {
    return getUserDetails().getRoles();
  }
}
