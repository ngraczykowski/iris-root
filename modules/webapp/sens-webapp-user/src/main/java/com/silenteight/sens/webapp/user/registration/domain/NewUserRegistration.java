package com.silenteight.sens.webapp.user.registration.domain;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.sens.webapp.user.registration.domain.ExternalUserRegisterer.ExternalUserRegistration;
import com.silenteight.sens.webapp.user.registration.domain.InternalUserRegisterer.InternalUserRegistration;

@Value
public class NewUserRegistration implements ExternalUserRegistration, InternalUserRegistration {

  @NonNull
  NewUserDetails userDetails;
  @NonNull
  RegistrationSource source;

  boolean isInternal() {
    return RegistrationSource.INTERNAL == source;
  }
}
