package com.silenteight.sens.webapp.backend.user.registration.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.user.registration.domain.NameLengthValidator.InvalidNameLength;
import com.silenteight.sens.webapp.backend.user.registration.domain.RolesValidator.RolesDontExist;
import com.silenteight.sens.webapp.backend.user.registration.domain.UsernameUniquenessValidator.UsernameNotUnique;
import com.silenteight.sens.webapp.common.time.TimeSource;

import io.vavr.control.Either;
import io.vavr.control.Option;

import java.util.Set;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

@RequiredArgsConstructor
class InternalUserRegisterer {

  private final TimeSource timeSource;
  private final NameLengthValidator usernameLengthValidator;
  private final NameLengthValidator displayNameLengthValidator;
  private final RolesValidator rolesValidator;
  private final UsernameUniquenessValidator usernameUniquenessValidator;

  Either<UserRegistrationDomainError, CompletedUserRegistration> register(
      InternalUserRegistration registration) {
    String username = registration.getUsername();

    Option<InvalidNameLength> invalidUsernameLength = usernameLengthValidator.validate(username);
    if (invalidUsernameLength.isDefined())
      return left(invalidUsernameLength.get());

    Option<InvalidNameLength> invalidDisplayNameLength = displayNameLengthValidator.validate(
        registration.getDisplayName());
    if (invalidDisplayNameLength.isDefined())
      return left(invalidDisplayNameLength.get());

    Option<UsernameNotUnique> usernameNotUnique = usernameUniquenessValidator.validate(username);
    if (usernameNotUnique.isDefined())
      return left(usernameNotUnique.get());

    if (registration.hasRoles()) {
      Option<RolesDontExist> roleDontExistError = rolesValidator.validate(registration.getRoles());

      if (roleDontExistError.isDefined())
        return left(roleDontExistError.get());
    }

    return right(
        new CompletedUserRegistration(registration.getUserDetails(), timeSource.offsetDateTime()));
  }

  interface InternalUserRegistration {

    default String getDisplayName() {
      return getUserDetails().getDisplayName();
    }

    NewUserDetails getUserDetails();

    default Set<String> getRoles() {
      return getUserDetails().getRoles();
    }

    default String getUsername() {
      return getUserDetails().getUsername();
    }

    default boolean hasRoles() {
      return !getRoles().isEmpty();
    }
  }
}
