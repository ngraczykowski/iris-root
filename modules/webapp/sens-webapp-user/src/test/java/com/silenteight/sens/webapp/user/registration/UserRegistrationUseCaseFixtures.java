package com.silenteight.sens.webapp.user.registration;

import com.silenteight.sens.webapp.user.registration.RegisterInternalUserUseCase.RegisterInternalUserCommand;
import com.silenteight.sens.webapp.user.registration.domain.RolesValidator.RolesDontExist;
import com.silenteight.sens.webapp.user.registration.domain.UsernameUniquenessValidator.UsernameNotUnique;

import static java.util.Collections.emptySet;
import static java.util.Set.of;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

class UserRegistrationUseCaseFixtures {

  static final RegisterInternalUserCommand NO_ROLES_REGISTRATION_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName("John Doe")
          .username("jdoe5")
          .roles(emptySet())
          .password("jdoe125")
          .build();

  static final RegisterInternalUserCommand ANALYST_REGISTRATION_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName("John Doe")
          .username("jdoe5")
          .roles(of("Analyst"))
          .password("jdoe125")
          .build();

  static final RegisterInternalUserCommand TOO_LONG_USERNAME_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName("John Doe")
          .username(randomAlphabetic(31))
          .password("jdoe125")
          .build();

  static final RegisterInternalUserCommand TOO_LONG_DISPLAYNAME_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName(randomAlphabetic(51))
          .username("jdoe123")
          .password("jdoe125")
          .build();

  static final RegisterInternalUserCommand TOO_SHORT_DISPLAYNAME_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName(randomAlphabetic(2))
          .username("jdoe123")
          .password("jdoe125")
          .build();

  static final RegisterInternalUserCommand TOO_SHORT_USERNAME_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName("John Doe")
          .username(randomAlphabetic(2))
          .password("jdoe125")
          .build();

  static final UsernameNotUnique USERNAME_NOT_UNIQUE =
      new UsernameNotUnique("some username");

  static final RolesDontExist ROLES_DONT_EXIST =
      new RolesDontExist(emptySet());
}
