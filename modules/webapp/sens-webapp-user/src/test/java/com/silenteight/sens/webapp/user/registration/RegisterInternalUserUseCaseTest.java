package com.silenteight.sens.webapp.user.registration;

import com.silenteight.sens.webapp.user.registration.domain.*;
import com.silenteight.sens.webapp.user.registration.domain.NameLengthValidator.InvalidNameLengthError;
import com.silenteight.sens.webapp.user.registration.domain.RegexValidator.InvalidNameCharsError;
import com.silenteight.sens.webapp.user.registration.domain.RolesValidator.RolesDontExistError;
import com.silenteight.sens.webapp.user.registration.domain.UsernameUniquenessValidator.UsernameNotUniqueError;

import io.vavr.control.Either;
import io.vavr.control.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.user.registration.ResultAssert.assertThatResult;
import static com.silenteight.sens.webapp.user.registration.UserRegistrationUseCaseFixtures.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterInternalUserUseCaseTest {

  private RegisterInternalUserUseCase underTest;

  @Mock
  private UsernameUniquenessValidator usernameUniquenessValidator;

  @Mock
  private RolesValidator rolesValidator;

  @Mock
  private RegisteredUserRepository registeredUserRepository;

  @BeforeEach
  void setUp() {
    UserRegistrationUseCaseConfiguration configuration =
        new UserRegistrationUseCaseConfiguration();
    UserRegisteringDomainService userRegisteringDomainService =
        new UserRegistrationDomainTestConfiguration()
            .userRegisteringDomainService(usernameUniquenessValidator, rolesValidator);

    underTest =
        configuration.registerInternalUserUseCase(
            userRegisteringDomainService, registeredUserRepository);
  }

  @Test
  void usernameTooLong_returnsValidDomainError() {
    Either<UserRegistrationDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(TOO_LONG_USERNAME_REQUEST);

    assertThatResult(actual).isFailureOfType(InvalidNameLengthError.class);
  }

  @Test
  void usernameTooShort_returnsValidDomainError() {
    Either<UserRegistrationDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(TOO_SHORT_USERNAME_REQUEST);

    assertThatResult(actual).isFailureOfType(InvalidNameLengthError.class);
  }

  @Test
  void usernameContainsUppercaseLetters_returnsValidDomainError() {
    Either<UserRegistrationDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(RESTRICTED_CHAR_UPPERCASE_USERNAME_REQUEST);

    assertThatResult(actual).isFailureOfType(InvalidNameCharsError.class);
  }

  @Test
  void usernameContainsNonAsciiChars_returnsValidDomainError() {
    Either<UserRegistrationDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(RESTRICTED_CHAR_NONASCII_USERNAME_REQUEST);

    assertThatResult(actual).isFailureOfType(InvalidNameCharsError.class);
  }

  @Test
  void usernameContainsInvalidSpecialChars_returnsValidDomainError() {
    Either<UserRegistrationDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(RESTRICTED_CHAR_INVALID_SPECIAL_USERNAME_REQUEST);

    assertThatResult(actual).isFailureOfType(InvalidNameCharsError.class);
  }

  @Test
  void displayNameTooLong_returnsValidDomainError() {
    given(usernameUniquenessValidator.validate(any())).willReturn(Option.none());

    Either<UserRegistrationDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(TOO_LONG_DISPLAYNAME_REQUEST);

    assertThatResult(actual).isFailureOfType(InvalidNameLengthError.class);
  }

  @Test
  void displayNameTooShort_returnsValidDomainError() {
    given(usernameUniquenessValidator.validate(any())).willReturn(Option.none());

    Either<UserRegistrationDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(TOO_SHORT_DISPLAYNAME_REQUEST);

    assertThatResult(actual).isFailureOfType(InvalidNameLengthError.class);
  }

  @Nested
  class GivenUsernameIsUnique {

    @BeforeEach
    void setUp() {
      given(usernameUniquenessValidator.validate(any())).willReturn(Option.none());
    }

    @Test
    void whenNoRoles_returnsValidSuccess() {
      Either<UserRegistrationDomainError, RegisterInternalUserUseCase.Success> actual =
          underTest.apply(NO_ROLES_REGISTRATION_REQUEST);

      assertThatResult(actual)
          .isSuccessWithUsername(NO_ROLES_REGISTRATION_REQUEST.getUsername());
    }

    @Test
    void usernameContainsValidSpecialChars_returnsValidSuccess() {
      Either<UserRegistrationDomainError, RegisterInternalUserUseCase.Success> actual =
          underTest.apply(RESTRICTED_CHAR_VALID_SPECIAL_USERNAME_REQUEST);

      assertThatResult(actual)
          .isSuccessWithUsername(RESTRICTED_CHAR_VALID_SPECIAL_USERNAME_REQUEST.getUsername());
    }

    @Test
    void whenNoRoles_savesToRepo() {
      underTest.apply(NO_ROLES_REGISTRATION_REQUEST);

      verify(registeredUserRepository).save(any());
    }
  }

  @Nested
  class GivenRolesAreValidAndUsernameIsUnique {

    @BeforeEach
    void setUp() {
      given(usernameUniquenessValidator.validate(any())).willReturn(Option.none());
      given(rolesValidator.validate(any())).willReturn(Option.none());
    }

    @Test
    void registerWithRoles_returnsValidSuccess() {
      Either<UserRegistrationDomainError, RegisterInternalUserUseCase.Success> actual =
          underTest.apply(ONE_ROLE_REGISTRATION_REQUEST);

      assertThatResult(actual)
          .isSuccessWithUsername(ONE_ROLE_REGISTRATION_REQUEST.getUsername());
    }

    @Test
    void registerWithRoles_savesToRepo() {
      underTest.apply(ONE_ROLE_REGISTRATION_REQUEST);

      verify(registeredUserRepository).save(any());
    }
  }

  @Nested
  class GivenUsernameIsNotUnique {

    @BeforeEach
    void setUp() {
      given(usernameUniquenessValidator.validate(any())).willReturn(Option.of(
          UserRegistrationUseCaseFixtures.USERNAME_NOT_UNIQUE));
    }

    @Test
    void whenNoRoles_returnsValidDomainError() {
      Either<UserRegistrationDomainError, RegisterInternalUserUseCase.Success> actual =
          underTest.apply(NO_ROLES_REGISTRATION_REQUEST);

      assertThatResult(actual).isFailureOfType(UsernameNotUniqueError.class);
    }

    @Test
    void whenNoRoles_doesNotSaveToRepo() {
      underTest.apply(NO_ROLES_REGISTRATION_REQUEST);

      verifyZeroInteractions(registeredUserRepository);
    }
  }

  @Nested
  class GivenUsernameIsUniqueAndRolesDontExist {

    @BeforeEach
    void setUp() {
      given(usernameUniquenessValidator.validate(any())).willReturn(Option.none());
      given(rolesValidator.validate(any())).willReturn(Option.of(
          UserRegistrationUseCaseFixtures.ROLES_DONT_EXIST));
    }

    @Test
    void registerWithRoles_returnsValidDomainError() {
      Either<UserRegistrationDomainError, RegisterInternalUserUseCase.Success> actual =
          underTest.apply(ONE_ROLE_REGISTRATION_REQUEST);

      assertThatResult(actual).isFailureOfType(RolesDontExistError.class);
    }

    @Test
    void registerWithRoles_doesNotSaveToRepo() {
      underTest.apply(ONE_ROLE_REGISTRATION_REQUEST);

      verifyZeroInteractions(registeredUserRepository);
    }
  }
}
