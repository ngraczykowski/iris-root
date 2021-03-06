package com.silenteight.sens.webapp.user.registration.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.user.domain.validator.NameLengthValidator;
import com.silenteight.sens.webapp.user.domain.validator.NameLengthValidator.InvalidNameLengthError;
import com.silenteight.sens.webapp.user.domain.validator.RegexValidator;
import com.silenteight.sens.webapp.user.domain.validator.RegexValidator.RegexError;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.usermanagement.api.error.UserDomainError;
import com.silenteight.sep.usermanagement.api.role.RoleValidator;
import com.silenteight.sep.usermanagement.api.role.RoleValidator.RolesDontExistError;
import com.silenteight.sep.usermanagement.api.user.UsernameUniquenessValidator;
import com.silenteight.sep.usermanagement.api.user.UsernameUniquenessValidator.UsernameNotUniqueError;
import com.silenteight.sep.usermanagement.api.user.dto.CreateUserCommand;
import com.silenteight.sep.usermanagement.api.user.dto.NewUserDetails.Credentials;


import io.vavr.control.Either;
import io.vavr.control.Option;

import java.util.Optional;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

@Slf4j
@RequiredArgsConstructor
public class UserRegisteringDomainService {

  @NonNull
  private final TimeSource timeSource;
  @NonNull
  private final NameLengthValidator usernameLengthValidator;
  @NonNull
  private final RegexValidator usernameCharsValidator;
  @NonNull
  private final NameLengthValidator displayNameLengthValidator;
  @NonNull
  private final RoleValidator roleValidator;
  @NonNull
  private final UsernameUniquenessValidator usernameUniquenessValidator;
  @NonNull
  private final RegexValidator passwordCharsValidator;
  @NonNull
  private final String rolesScope;

  public Either<UserDomainError, CreateUserCommand> register(
      NewUserRegistration registration) {

    log.info(USER_MANAGEMENT, "Registering User. registration={}", registration);
    Option<InvalidNameLengthError> invalidUsernameLengthError =
        usernameLengthValidator.validate(registration.getUsername());
    if (invalidUsernameLengthError.isDefined())
      return left(invalidUsernameLengthError.get());

    Option<RegexError> usernameConstraintRestrictedCharsError =
        usernameCharsValidator.validate(registration.getUsername());
    if (usernameConstraintRestrictedCharsError.isDefined())
      return left(usernameConstraintRestrictedCharsError.get());

    Optional<UsernameNotUniqueError> usernameNotUniqueError =
        usernameUniquenessValidator.validate(registration.getUsername());
    if (usernameNotUniqueError.isPresent())
      return left(usernameNotUniqueError.get());

    Option<InvalidNameLengthError> invalidDisplayNameLengthError =
        displayNameLengthValidator.validate(registration.getDisplayName());
    if (invalidDisplayNameLengthError.isDefined())
      return left(invalidDisplayNameLengthError.get());

    if (registration.hasRoles(rolesScope)) {
      Optional<RolesDontExistError> rolesDontExistError =
          roleValidator.validate(rolesScope, registration.getRoles(rolesScope));

      if (rolesDontExistError.isPresent())
        return left(rolesDontExistError.get());
    }

    Credentials credentials = registration.getCredentials();
    if (credentials != null) {
      Option<RegexError> passwordConstraintRestrictedCharsError =
          passwordCharsValidator.validate(credentials.getPassword());

      if (passwordConstraintRestrictedCharsError.isDefined())
        return left(passwordConstraintRestrictedCharsError.get());
    }

    return right(
        new CreateUserCommand(
            registration.getUserDetails(),
            registration.getOrigin(),
            timeSource.offsetDateTime()));
  }
}
