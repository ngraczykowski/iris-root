package com.silenteight.sens.webapp.user.registration;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.user.domain.validator.NameLengthValidator.InvalidNameLengthError;
import com.silenteight.sens.webapp.user.domain.validator.RegexValidator.RegexError;
import com.silenteight.sens.webapp.user.domain.validator.RolesValidator;
import com.silenteight.sens.webapp.user.domain.validator.RolesValidator.RolesDontExistError;
import com.silenteight.sens.webapp.user.domain.validator.UserDomainError;
import com.silenteight.sens.webapp.user.domain.validator.UsernameUniquenessValidator;
import com.silenteight.sens.webapp.user.domain.validator.UsernameUniquenessValidator.UsernameNotUniqueError;
import com.silenteight.sens.webapp.user.registration.domain.UserRegisteringDomainService;
import com.silenteight.sens.webapp.user.registration.domain.UserRegistrationDomainTestConfiguration;

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

  @Mock
  private AuditLog auditLog;

  @BeforeEach
  void setUp() {
    UserRegistrationUseCaseConfiguration configuration =
        new UserRegistrationUseCaseConfiguration();
    UserRegisteringDomainService userRegisteringDomainService =
        new UserRegistrationDomainTestConfiguration()
            .userRegisteringDomainService(usernameUniquenessValidator, rolesValidator, auditLog);

    underTest =
        configuration.registerInternalUserUseCase(
            userRegisteringDomainService, registeredUserRepository, auditLog);
  }

  @Test
  void usernameTooLong_returnsValidDomainError() {
    Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(TOO_LONG_USERNAME_REQUEST);

    assertThatResult(actual)
        .isFailureOfType(InvalidNameLengthError.class)
        .containsErrorReason(TOO_LONG_USERNAME_REQUEST.getUsername()
            + " has invalid length. Should be between 3 and 30 inclusive.");
  }

  @Test
  void usernameTooShort_returnsValidDomainError() {
    Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(TOO_SHORT_USERNAME_REQUEST);

    assertThatResult(actual)
        .isFailureOfType(InvalidNameLengthError.class)
        .containsErrorReason(TOO_SHORT_USERNAME_REQUEST.getUsername()
            + " has invalid length. Should be between 3 and 30 inclusive.");
  }

  @Test
  void usernameContainsUppercaseLetters_returnsValidDomainError() {
    Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(RESTRICTED_CHAR_UPPERCASE_USERNAME_REQUEST);

    assertThatResult(actual)
        .isFailureOfType(RegexError.class)
        .containsErrorReason(RESTRICTED_CHAR_UPPERCASE_USERNAME_REQUEST.getUsername()
            + " has invalid chars. Only lowercase letters, numbers and -_@. chars allowed.");
  }

  @Test
  void usernameContainsNonAsciiChars_returnsValidDomainError() {
    Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(RESTRICTED_CHAR_NONASCII_USERNAME_REQUEST);

    assertThatResult(actual)
        .isFailureOfType(RegexError.class)
        .containsErrorReason(RESTRICTED_CHAR_NONASCII_USERNAME_REQUEST.getUsername()
            + " has invalid chars. Only lowercase letters, numbers and -_@. chars allowed.");
  }

  @Test
  void usernameContainsInvalidSpecialChars_returnsValidDomainError() {
    Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(RESTRICTED_CHAR_INVALID_SPECIAL_USERNAME_REQUEST);

    assertThatResult(actual)
        .isFailureOfType(RegexError.class)
        .containsErrorReason(RESTRICTED_CHAR_INVALID_SPECIAL_USERNAME_REQUEST.getUsername()
            + " has invalid chars. Only lowercase letters, numbers and -_@. chars allowed.");
  }

  @Test
  void displayNameTooLong_returnsValidDomainError() {
    given(usernameUniquenessValidator.validate(any())).willReturn(Option.none());

    Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(TOO_LONG_DISPLAYNAME_REQUEST);

    assertThatResult(actual)
        .isFailureOfType(InvalidNameLengthError.class)
        .containsErrorReason(TOO_LONG_DISPLAYNAME_REQUEST.getDisplayName()
            + " has invalid length. Should be between 3 and 50 inclusive.");
  }

  @Test
  void displayNameTooShort_returnsValidDomainError() {
    given(usernameUniquenessValidator.validate(any())).willReturn(Option.none());

    Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(TOO_SHORT_DISPLAYNAME_REQUEST);

    assertThatResult(actual)
        .isFailureOfType(InvalidNameLengthError.class)
        .containsErrorReason(TOO_SHORT_DISPLAYNAME_REQUEST.getDisplayName()
            + " has invalid length. Should be between 3 and 50 inclusive.");
  }

  @Nested
  class GivenUsernameIsUnique {

    @BeforeEach
    void setUp() {
      given(usernameUniquenessValidator.validate(any())).willReturn(Option.none());
    }

    @Test
    void whenNoRoles_returnsValidSuccess() {
      Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
          underTest.apply(NO_ROLES_REGISTRATION_REQUEST);

      assertThatResult(actual)
          .isSuccessWithUsername(NO_ROLES_REGISTRATION_REQUEST.getUsername());
    }

    @Test
    void usernameContainsValidSpecialChars_returnsValidSuccess() {
      Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
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
  class GivenRolesAndCredentialsAreValidAndUsernameIsUnique {

    @BeforeEach
    void setUp() {
      given(usernameUniquenessValidator.validate(any())).willReturn(Option.none());
      given(rolesValidator.validate(any())).willReturn(Option.none());
    }

    @Test
    void registerWithRoles_returnsValidSuccess() {
      Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
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
      Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
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
      Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
          underTest.apply(ONE_ROLE_REGISTRATION_REQUEST);

      assertThatResult(actual).isFailureOfType(RolesDontExistError.class);
    }

    @Test
    void registerWithRoles_doesNotSaveToRepo() {
      underTest.apply(ONE_ROLE_REGISTRATION_REQUEST);

      verifyNoInteractions(registeredUserRepository);
    }
  }

  @Nested
  class GivenUsernameIsUniqueRolesAndCredentialsAreInvalid {

    @BeforeEach
    void setUp() {
      given(usernameUniquenessValidator.validate(any())).willReturn(Option.none());
      given(rolesValidator.validate(any())).willReturn(Option.none());
    }

    @Test
    void whenPasswordIsTooShort_returnsValidDomainError() {
      Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
          underTest.apply(TOO_SHORT_PASSWORD_REQUEST);

      assertThatResult(actual).isFailureOfType(RegexError.class);
    }

    @Test
    void whenPasswordDoesNotHaveDigit_returnsValidDomainError() {
      Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
          underTest.apply(NO_DIGIT_PASSWORD_REQUEST);

      assertThatResult(actual).isFailureOfType(RegexError.class);
    }

    @Test
    void whenPasswordDoesNotHaveLetter_returnsValidDomainError() {
      Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
          underTest.apply(NO_LETTER_PASSWORD_REQUEST);

      assertThatResult(actual).isFailureOfType(RegexError.class);
    }
  }
}
