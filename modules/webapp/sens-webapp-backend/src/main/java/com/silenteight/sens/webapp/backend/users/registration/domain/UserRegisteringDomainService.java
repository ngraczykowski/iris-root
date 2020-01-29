package com.silenteight.sens.webapp.backend.users.registration.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.users.registration.UserRegistrationDomainError;
import com.silenteight.sens.webapp.backend.users.registration.domain.RolesValidator.RolesDontExist;
import com.silenteight.sens.webapp.common.time.TimeSource;

import io.vavr.control.Either;

import java.util.Set;

import static com.silenteight.sens.webapp.backend.users.registration.domain.UsernameValidator.UsernameNotUnique;

@RequiredArgsConstructor
public class UserRegisteringDomainService {

  private final RolesValidator rolesValidator;
  private final UsernameValidator usernameValidator;
  private final TimeSource timeSource;

  public Either<UserRegistrationDomainError, CompletedUserRegistration> register(
      NewUserRegistration newUserRegistration) {
    String username = newUserRegistration.getUsername();

    if (!usernameValidator.isUnique(username))
      return Either.left(new UsernameNotUnique(username));

    Set<String> roles = newUserRegistration.getRoles();
    if (!roles.isEmpty() && !rolesValidator.rolesExist(roles))
      return Either.left(new RolesDontExist(roles));

    return Either.right(
        new CompletedUserRegistration(newUserRegistration, timeSource.offsetDateTime())
    );
  }
}
