package com.silenteight.sens.webapp.user.registration.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.common.time.TimeSource;
import com.silenteight.sens.webapp.user.registration.domain.NameLengthValidator.InvalidNameLengthError;
import com.silenteight.sens.webapp.user.registration.domain.RolesValidator.RolesDontExistError;
import com.silenteight.sens.webapp.user.registration.domain.UsernameUniquenessValidator.UsernameNotUniqueError;

import io.vavr.control.Either;
import io.vavr.control.Option;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

@RequiredArgsConstructor
public class UserRegisteringDomainService {

  private final TimeSource timeSource;
  private final NameLengthValidator usernameLengthValidator;
  private final NameLengthValidator displayNameLengthValidator;
  private final RolesValidator rolesValidator;
  private final UsernameUniquenessValidator usernameUniquenessValidator;

  public Either<UserRegistrationDomainError, CompletedUserRegistration> register(
      NewUserRegistration registration) {

    Option<InvalidNameLengthError> invalidUsernameLengthError =
        usernameLengthValidator.validate(registration.getUsername());
    if (invalidUsernameLengthError.isDefined())
      return left(invalidUsernameLengthError.get());

    Option<UsernameNotUniqueError> usernameNotUniqueError =
        usernameUniquenessValidator.validate(registration.getUsername());
    if (usernameNotUniqueError.isDefined())
      return left(usernameNotUniqueError.get());

    Option<InvalidNameLengthError> invalidDisplayNameLengthError =
        displayNameLengthValidator.validate(registration.getDisplayName());
    if (invalidDisplayNameLengthError.isDefined())
      return left(invalidDisplayNameLengthError.get());

    if (registration.hasRoles()) {
      Option<RolesDontExistError> rolesDontExistError =
          rolesValidator.validate(registration.getRoles());

      if (rolesDontExistError.isDefined())
        return left(rolesDontExistError.get());
    }

    return right(
        new CompletedUserRegistration(
            registration.getUserDetails(),
            registration.getOrigin(),
            timeSource.offsetDateTime()));
  }
}
