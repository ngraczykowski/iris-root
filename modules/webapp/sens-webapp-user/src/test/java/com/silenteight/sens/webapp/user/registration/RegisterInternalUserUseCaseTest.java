package com.silenteight.sens.webapp.user.registration;

import com.silenteight.sens.webapp.user.registration.RegisterInternalUserUseCase.Success;
import com.silenteight.sens.webapp.user.registration.domain.*;
import com.silenteight.sens.webapp.user.registration.domain.NameLengthValidator.InvalidNameLength;
import com.silenteight.sens.webapp.user.registration.domain.RolesValidator.RolesDontExist;
import com.silenteight.sens.webapp.user.registration.domain.UsernameUniquenessValidator.UsernameNotUnique;

import io.vavr.control.Either;
import io.vavr.control.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.user.registration.UserRegistrationUseCaseFixtures.ANALYST_REGISTRATION_REQUEST;
import static com.silenteight.sens.webapp.user.registration.UserRegistrationUseCaseFixtures.NO_ROLES_REGISTRATION_REQUEST;
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
    UserRegisteringDomainService userRegisteringService =
        new UserRegistrationDomainTestConfiguration()
            .userRegisteringService(usernameUniquenessValidator, rolesValidator);

    underTest =
        configuration.registerInternalUserUseCase(
            userRegisteringService, registeredUserRepository);
  }

  @Test
  void usernameTooLong_returnsValidDomainError() {
    Either<UserRegistrationDomainError, Success> actual =
        underTest.apply(UserRegistrationUseCaseFixtures.TOO_LONG_USERNAME_REQUEST);

    ResultAssert.assertThatResult(actual).isFailureOfType(InvalidNameLength.class);
  }

  @Test
  void usernameTooShort_returnsValidDomainError() {
    Either<UserRegistrationDomainError, Success> actual =
        underTest.apply(UserRegistrationUseCaseFixtures.TOO_SHORT_USERNAME_REQUEST);

    ResultAssert.assertThatResult(actual).isFailureOfType(InvalidNameLength.class);
  }

  @Test
  void displayNameTooLong_returnsValidDomainError() {
    Either<UserRegistrationDomainError, Success> actual =
        underTest.apply(UserRegistrationUseCaseFixtures.TOO_LONG_DISPLAYNAME_REQUEST);

    ResultAssert.assertThatResult(actual).isFailureOfType(InvalidNameLength.class);
  }

  @Test
  void displayNameTooShort_returnsValidDomainError() {
    Either<UserRegistrationDomainError, Success> actual =
        underTest.apply(UserRegistrationUseCaseFixtures.TOO_SHORT_DISPLAYNAME_REQUEST);

    ResultAssert.assertThatResult(actual).isFailureOfType(InvalidNameLength.class);
  }

  @Nested
  class GivenUsernameIsUnique {

    @BeforeEach
    void setUp() {
      given(usernameUniquenessValidator.validate(any())).willReturn(Option.none());
    }

    @Test
    void whenNoRoles_returnsValidSuccess() {
      Either<UserRegistrationDomainError, Success> actual =
          underTest.apply(NO_ROLES_REGISTRATION_REQUEST);

      ResultAssert.assertThatResult(actual)
          .isSuccessWithUsername(NO_ROLES_REGISTRATION_REQUEST.getUsername());
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
      Either<UserRegistrationDomainError, Success> actual =
          underTest.apply(ANALYST_REGISTRATION_REQUEST);

      ResultAssert.assertThatResult(actual)
          .isSuccessWithUsername(ANALYST_REGISTRATION_REQUEST.getUsername());
    }

    @Test
    void registerWithRoles_savesToRepo() {
      underTest.apply(ANALYST_REGISTRATION_REQUEST);

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
      Either<UserRegistrationDomainError, Success> actual =
          underTest.apply(NO_ROLES_REGISTRATION_REQUEST);

      ResultAssert.assertThatResult(actual).isFailureOfType(UsernameNotUnique.class);
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
      Either<UserRegistrationDomainError, Success> actual =
          underTest.apply(ANALYST_REGISTRATION_REQUEST);

      ResultAssert.assertThatResult(actual).isFailureOfType(RolesDontExist.class);
    }

    @Test
    void registerWithRoles_doesNotSaveToRepo() {
      underTest.apply(ANALYST_REGISTRATION_REQUEST);

      verifyZeroInteractions(registeredUserRepository);
    }
  }
}
