package com.silenteight.sens.webapp.backend.user.registration;

import com.silenteight.sens.webapp.backend.user.registration.RegisterUserUseCase.RegisterUserCommand;
import com.silenteight.sens.webapp.backend.user.registration.RegisterUserUseCase.Success;
import com.silenteight.sens.webapp.backend.user.registration.domain.RolesValidator;
import com.silenteight.sens.webapp.backend.user.registration.domain.RolesValidator.RolesDontExist;
import com.silenteight.sens.webapp.backend.user.registration.domain.UserRegisteringDomainService;
import com.silenteight.sens.webapp.backend.user.registration.domain.UsernameValidator;
import com.silenteight.sens.webapp.backend.user.registration.domain.UsernameValidator.UsernameNotUnique;

import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static com.silenteight.sens.webapp.backend.user.registration.ResultAssert.assertThatResult;
import static java.util.Set.of;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

  private RegisterUserUseCase underTest;

  @Mock
  private UsernameValidator usernameValidator;

  @Mock
  private RolesValidator rolesValidator;

  @Mock
  private RegisteredUserRepository registeredUserRepository;

  @BeforeEach
  void setUp() {
    UserRegistrationConfiguration configuration = new UserRegistrationConfiguration();
    UserRegisteringDomainService userRegisteringService =
        configuration.userRegisteringService(usernameValidator, rolesValidator);
    underTest = configuration.registerUserUseCase(userRegisteringService, registeredUserRepository);
  }

  @Nested
  class GivenUsernameIsUnique {

    @BeforeEach
    void setUp() {
      given(usernameValidator.isUnique(any())).willReturn(true);
    }

    @Test
    void whenNoRoles_returnsValidSuccess() {
      Either<UserRegistrationDomainError, Success> actual =
          underTest.apply(Fixtures.NO_ROLES_REGISTRATION_REQUEST);

      assertThatResult(actual)
          .isSuccessWithUsername(Fixtures.NO_ROLES_REGISTRATION_REQUEST.getUsername());
    }

    @Test
    void whenNoRoles_savesToRepo() {
      underTest.apply(Fixtures.NO_ROLES_REGISTRATION_REQUEST);

      verify(registeredUserRepository).save(any());
    }
  }

  @Nested
  class GivenRolesAreValidAndUsernameIsUnique {

    @BeforeEach
    void setUp() {
      given(usernameValidator.isUnique(any())).willReturn(true);
      given(rolesValidator.rolesExist(any())).willReturn(true);
    }

    @Test
    void registerWithRoles_returnsValidSuccess() {
      Either<UserRegistrationDomainError, Success> actual =
          underTest.apply(Fixtures.ANALYST_REGISTRATION_REQUEST);

      assertThatResult(actual)
          .isSuccessWithUsername(Fixtures.ANALYST_REGISTRATION_REQUEST.getUsername());
    }

    @Test
    void registerWithRoles_savesToRepo() {
      underTest.apply(Fixtures.ANALYST_REGISTRATION_REQUEST);

      verify(registeredUserRepository).save(any());
    }
  }

  @Nested
  class GivenUsernameIsNotUnique {

    @BeforeEach
    void setUp() {
      given(usernameValidator.isUnique(any())).willReturn(false);
    }

    @Test
    void whenNoRoles_returnsValidDomainError() {
      Either<UserRegistrationDomainError, Success> actual =
          underTest.apply(Fixtures.NO_ROLES_REGISTRATION_REQUEST);

      assertThatResult(actual).isFailureOfType(UsernameNotUnique.class);
    }

    @Test
    void whenNoRoles_doesNotSaveToRepo() {
      Either<UserRegistrationDomainError, Success> actualResult =
          underTest.apply(Fixtures.NO_ROLES_REGISTRATION_REQUEST);

      verifyZeroInteractions(registeredUserRepository);
    }
  }

  @Nested
  class GivenUsernameIsUniqueAndRolesDontExist {

    @BeforeEach
    void setUp() {
      given(usernameValidator.isUnique(any())).willReturn(true);
      given(rolesValidator.rolesExist(any())).willReturn(false);
    }

    @Test
    void registerWithRoles_returnsValidDomainError() {
      Either<UserRegistrationDomainError, Success> actual =
          underTest.apply(Fixtures.ANALYST_REGISTRATION_REQUEST);

      assertThatResult(actual).isFailureOfType(RolesDontExist.class);
    }

    @Test
    void registerWithRoles_doesNotSaveToRepo() {
      underTest.apply(Fixtures.ANALYST_REGISTRATION_REQUEST);

      verifyZeroInteractions(registeredUserRepository);
    }
  }

  private static class Fixtures {

    public static final RegisterUserCommand NO_ROLES_REGISTRATION_REQUEST =
        RegisterUserCommand.builder()
            .displayName("John Doe")
            .username("jdoe5")
            .roles(Collections.emptySet())
            .password("jdoe125")
            .build();

    public static final RegisterUserCommand ANALYST_REGISTRATION_REQUEST =
        RegisterUserCommand.builder()
            .displayName("John Doe")
            .username("jdoe5")
            .roles(of("Analyst"))
            .password("jdoe125")
            .build();
  }
}
