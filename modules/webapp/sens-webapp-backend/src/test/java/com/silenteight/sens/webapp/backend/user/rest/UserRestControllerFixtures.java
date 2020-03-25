package com.silenteight.sens.webapp.backend.user.rest;

import lombok.experimental.UtilityClass;

import com.silenteight.sens.webapp.user.domain.validator.UserDomainError;
import com.silenteight.sens.webapp.user.domain.validator.UsernameUniquenessValidator.UsernameNotUniqueError;
import com.silenteight.sens.webapp.user.registration.RegisterInternalUserUseCase;

@UtilityClass
class UserRestControllerFixtures {

  static final String USERNAME = "jdoe123";
  static final RegisterInternalUserUseCase.Success USER_REGISTRATION_SUCCESS =
      () -> USERNAME;

  static final String USER_REGISTRATION_ERROR_REASON = "some reason";

  static final UserDomainError USER_REGISTRATION_DOMAIN_ERROR =
      () -> USER_REGISTRATION_ERROR_REASON;

  static final UsernameNotUniqueError USERNAME_NOT_UNIQUE = new UsernameNotUniqueError(USERNAME);
}
