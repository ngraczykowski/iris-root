package com.silenteight.sens.webapp.user.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.common.time.TimeSource;
import com.silenteight.sens.webapp.user.domain.validator.*;
import com.silenteight.sens.webapp.user.domain.validator.NameLengthValidator.InvalidNameLengthError;
import com.silenteight.sens.webapp.user.domain.validator.RegexValidator.RegexError;
import com.silenteight.sens.webapp.user.domain.validator.RolesValidator.RolesDontExistError;
import com.silenteight.sens.webapp.user.domain.validator.UsernameUniquenessValidator.UsernameNotUniqueError;
import com.silenteight.sens.webapp.user.registration.domain.NewUserDetails.Credentials;

import io.vavr.control.Either;
import io.vavr.control.Option;

import java.util.Optional;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

@Slf4j
@RequiredArgsConstructor
public class UserRegisteringDomainService {

  private final TimeSource timeSource;
  private final NameLengthValidator usernameLengthValidator;
  private final RegexValidator usernameCharsValidator;
  private final NameLengthValidator displayNameLengthValidator;
  private final RolesValidator rolesValidator;
  private final UsernameUniquenessValidator usernameUniquenessValidator;
  private final RegexValidator passwordCharsValidator;

  public Either<UserDomainError, CompletedUserRegistration> register(
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

    Optional<Credentials> credentialsOpt = registration.getCredentials();
    if (credentialsOpt.isPresent()) {
      Credentials credentials = credentialsOpt.get();
      Option<RegexError> passwordConstraintRestrictedCharsError =
          passwordCharsValidator.validate(credentials.getPassword());

      if (passwordConstraintRestrictedCharsError.isDefined())
        return left(passwordConstraintRestrictedCharsError.get());
    }

    return right(
        new CompletedUserRegistration(
            registration.getUserDetails(),
            registration.getOrigin(),
            timeSource.offsetDateTime()));
  }
}
