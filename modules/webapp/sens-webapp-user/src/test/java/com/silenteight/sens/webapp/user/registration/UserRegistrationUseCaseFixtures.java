package com.silenteight.sens.webapp.user.registration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.user.registration.RegisterInternalUserUseCase.RegisterInternalUserCommand;
import com.silenteight.sep.usermanagement.api.role.RoleValidator.RolesDontExistError;
import com.silenteight.sep.usermanagement.api.user.UsernameUniquenessValidator.UsernameNotUniqueError;

import static java.util.Collections.emptySet;
import static java.util.Set.of;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class UserRegistrationUseCaseFixtures {

  static final RegisterInternalUserCommand NO_ROLES_REGISTRATION_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName("John Doe")
          .username("jdoe5")
          .roles(emptySet())
          .password("jdoe125!")
          .build();

  static final RegisterInternalUserCommand ONE_ROLE_REGISTRATION_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName("John Doe")
          .username("jdoe5")
          .roles(of("Role"))
          .password("jdoe125!")
          .build();

  static final RegisterInternalUserCommand TOO_LONG_USERNAME_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName("John Doe")
          .username(randomAlphabetic(31))
          .password("jdoe125!")
          .build();

  static final RegisterInternalUserCommand TOO_LONG_DISPLAYNAME_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName(randomAlphabetic(51))
          .username("jdoe123")
          .password("jdoe125!")
          .build();

  static final RegisterInternalUserCommand TOO_SHORT_DISPLAYNAME_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName(randomAlphabetic(2))
          .username("jdoe123")
          .password("jdoe125!")
          .build();

  static final RegisterInternalUserCommand TOO_SHORT_USERNAME_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName("John Doe")
          .username(randomAlphabetic(2))
          .password("jdoe125!")
          .build();

  static final RegisterInternalUserCommand RESTRICTED_CHAR_UPPERCASE_USERNAME_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName("John Doe")
          .username("JohnDoe")
          .password("jdoe125!")
          .build();

  static final RegisterInternalUserCommand RESTRICTED_CHAR_NONASCII_USERNAME_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName("John Doe")
          .username("aażółcićgęśląjaźń")
          .password("jdoe125!")
          .build();

  static final RegisterInternalUserCommand RESTRICTED_CHAR_INVALID_SPECIAL_USERNAME_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName("John Doe")
          .username("john$doe")
          .password("jdoe125!")
          .build();

  static final RegisterInternalUserCommand RESTRICTED_CHAR_VALID_SPECIAL_USERNAME_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName("John Doe")
          .username("john-_@.doe")
          .password("jdoe125!")
          .build();

  static final RegisterInternalUserCommand TOO_SHORT_PASSWORD_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName("John Doe")
          .username("jdoe5")
          .roles(of("Role"))
          .password("jdoe125")
          .build();

  static final RegisterInternalUserCommand NO_DIGIT_PASSWORD_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName("John Doe")
          .username("jdoe5")
          .roles(of("Role"))
          .password("jdoejdoe!")
          .build();

  static final RegisterInternalUserCommand NO_LETTER_PASSWORD_REQUEST =
      RegisterInternalUserCommand.builder()
          .displayName("John Doe")
          .username("jdoe5")
          .roles(of("Role"))
          .password("12345678!")
          .build();

  static final UsernameNotUniqueError USERNAME_NOT_UNIQUE =
      new UsernameNotUniqueError("some username");

  static final RolesDontExistError ROLES_DONT_EXIST =
      new RolesDontExistError(emptySet());
}
