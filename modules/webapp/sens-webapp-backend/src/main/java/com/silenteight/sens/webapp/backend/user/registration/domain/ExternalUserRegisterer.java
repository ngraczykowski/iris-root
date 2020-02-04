package com.silenteight.sens.webapp.backend.user.registration.domain;

import io.vavr.control.Either;

class ExternalUserRegisterer {

  Either<UserRegistrationDomainError, CompletedUserRegistration> register(
      ExternalUserRegistration registration) {
    // TODO(bgulowaty): To be implemented when importing analysts from GNS (WA-171)
    throw new UnsupportedOperationException();
  }

  interface ExternalUserRegistration {

    NewUserDetails getUserDetails();
  }
}
