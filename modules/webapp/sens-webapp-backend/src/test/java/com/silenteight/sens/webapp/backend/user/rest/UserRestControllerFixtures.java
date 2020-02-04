package com.silenteight.sens.webapp.backend.user.rest;

import lombok.experimental.UtilityClass;

import com.silenteight.sens.webapp.backend.user.registration.RegisterInternalUserUseCase;
import com.silenteight.sens.webapp.backend.user.registration.domain.UserRegistrationDomainError;
import com.silenteight.sens.webapp.backend.user.registration.domain.UsernameUniquenessValidator.UsernameNotUnique;

@UtilityClass
class UserRestControllerFixtures {

  static final String USERNAME = "jdoe123";
  static final RegisterInternalUserUseCase.Success USER_REGISTRATION_SUCCESS =
      () -> USERNAME;

  static final String USER_REGISTRATION_ERROR_REASON = "some reason";

  static final UserRegistrationDomainError USER_REGISTRATION_DOMAIN_ERROR =
      () -> USER_REGISTRATION_ERROR_REASON;

  static final UsernameNotUnique USERNAME_NOT_UNIQUE = new UsernameNotUnique(USERNAME);
}
