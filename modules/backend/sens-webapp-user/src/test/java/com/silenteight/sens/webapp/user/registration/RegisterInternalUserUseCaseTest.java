package com.silenteight.sens.webapp.user.registration;

import com.silenteight.sens.webapp.audit.api.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;
import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.user.config.RolesProperties;
import com.silenteight.sens.webapp.user.domain.validator.NameLengthValidator.InvalidNameLengthError;
import com.silenteight.sens.webapp.user.domain.validator.RegexValidator.RegexError;
import com.silenteight.sens.webapp.user.registration.RegisterInternalUserUseCase.RegisterInternalUserCommand;
import com.silenteight.sens.webapp.user.registration.domain.UserRegisteringDomainService;
import com.silenteight.sens.webapp.user.registration.domain.UserRegistrationDomainTestConfiguration;
import com.silenteight.sep.usermanagement.api.error.UserDomainError;
import com.silenteight.sep.usermanagement.api.role.RoleValidator;
import com.silenteight.sep.usermanagement.api.role.RoleValidator.RolesDontExistError;
import com.silenteight.sep.usermanagement.api.user.UserCreator;
import com.silenteight.sep.usermanagement.api.user.UsernameUniquenessValidator;
import com.silenteight.sep.usermanagement.api.user.UsernameUniquenessValidator.UsernameNotUniqueError;

import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.CREATE;
import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.UPDATE;
import static com.silenteight.sens.webapp.audit.api.trace.AuditEventUtils.OBFUSCATED_STRING;
import static com.silenteight.sens.webapp.user.registration.ResultAssert.assertThatResult;
import static com.silenteight.sens.webapp.user.registration.UserRegistrationUseCaseFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterInternalUserUseCaseTest {

  private static final String ROLES_SCOPE = "frontend";
  private static final String COUNTRY_GROUPS_SCOPE = "kibana";

  @Mock
  private UsernameUniquenessValidator usernameUniquenessValidator;
  @Mock
  private RoleValidator roleValidator;
  @Mock
  private UserCreator userCreator;
  @Mock
  private AuditTracer auditTracer;
  @Mock
  private RolesProperties rolesProperties;

  private RegisterInternalUserUseCase underTest;

  @BeforeEach
  void setUp() {
    given(rolesProperties.getRolesScope()).willReturn(ROLES_SCOPE);
    given(rolesProperties.getCountryGroupsScope()).willReturn(COUNTRY_GROUPS_SCOPE);

    UserRegistrationUseCaseConfiguration configuration =
        new UserRegistrationUseCaseConfiguration();
    UserRegisteringDomainService userRegisteringDomainService =
        new UserRegistrationDomainTestConfiguration()
            .userRegisteringDomainService(
                usernameUniquenessValidator, roleValidator, rolesProperties);

    underTest =
        configuration.registerInternalUserUseCase(
            userRegisteringDomainService, userCreator, auditTracer, rolesProperties);
  }

  @Test
  void usernameTooLong_returnsValidDomainError() {
    Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(TOO_LONG_USERNAME_REQUEST);

    assertThatResult(actual)
        .isFailureOfType(InvalidNameLengthError.class)
        .containsErrorReason(TOO_LONG_USERNAME_REQUEST.getUsername()
            + " has invalid length. Should be between 3 and 30 inclusive.");
    verifyAuditLog(TOO_LONG_USERNAME_REQUEST);
  }

  @Test
  void usernameTooShort_returnsValidDomainError() {
    Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(TOO_SHORT_USERNAME_REQUEST);

    assertThatResult(actual)
        .isFailureOfType(InvalidNameLengthError.class)
        .containsErrorReason(TOO_SHORT_USERNAME_REQUEST.getUsername()
            + " has invalid length. Should be between 3 and 30 inclusive.");
    verifyAuditLog(TOO_SHORT_USERNAME_REQUEST);
  }

  @Test
  void usernameContainsUppercaseLetters_returnsValidDomainError() {
    Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(RESTRICTED_CHAR_UPPERCASE_USERNAME_REQUEST);

    assertThatResult(actual)
        .isFailureOfType(RegexError.class)
        .containsErrorReason(RESTRICTED_CHAR_UPPERCASE_USERNAME_REQUEST.getUsername()
            + " has invalid chars. Only lowercase letters, numbers and -_@. chars allowed.");
    verifyAuditLog(RESTRICTED_CHAR_UPPERCASE_USERNAME_REQUEST);
  }

  @Test
  void usernameContainsNonAsciiChars_returnsValidDomainError() {
    Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(RESTRICTED_CHAR_NONASCII_USERNAME_REQUEST);

    assertThatResult(actual)
        .isFailureOfType(RegexError.class)
        .containsErrorReason(RESTRICTED_CHAR_NONASCII_USERNAME_REQUEST.getUsername()
            + " has invalid chars. Only lowercase letters, numbers and -_@. chars allowed.");
    verifyAuditLog(RESTRICTED_CHAR_NONASCII_USERNAME_REQUEST);
  }

  @Test
  void usernameContainsInvalidSpecialChars_returnsValidDomainError() {
    Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(RESTRICTED_CHAR_INVALID_SPECIAL_USERNAME_REQUEST);

    assertThatResult(actual)
        .isFailureOfType(RegexError.class)
        .containsErrorReason(RESTRICTED_CHAR_INVALID_SPECIAL_USERNAME_REQUEST.getUsername()
            + " has invalid chars. Only lowercase letters, numbers and -_@. chars allowed.");
    verifyAuditLog(RESTRICTED_CHAR_INVALID_SPECIAL_USERNAME_REQUEST);
  }

  @Test
  void displayNameTooLong_returnsValidDomainError() {
    given(usernameUniquenessValidator.validate(any())).willReturn(Optional.empty());

    Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(TOO_LONG_DISPLAYNAME_REQUEST);

    assertThatResult(actual)
        .isFailureOfType(InvalidNameLengthError.class)
        .containsErrorReason(TOO_LONG_DISPLAYNAME_REQUEST.getDisplayName()
            + " has invalid length. Should be between 3 and 50 inclusive.");
    verifyAuditLog(TOO_LONG_DISPLAYNAME_REQUEST);
  }

  @Test
  void displayNameTooShort_returnsValidDomainError() {
    given(usernameUniquenessValidator.validate(any())).willReturn(Optional.empty());

    Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
        underTest.apply(TOO_SHORT_DISPLAYNAME_REQUEST);

    assertThatResult(actual)
        .isFailureOfType(InvalidNameLengthError.class)
        .containsErrorReason(TOO_SHORT_DISPLAYNAME_REQUEST.getDisplayName()
            + " has invalid length. Should be between 3 and 50 inclusive.");
    verifyAuditLog(TOO_SHORT_DISPLAYNAME_REQUEST);
  }

  @Nested
  class GivenUsernameIsUnique {

    @BeforeEach
    void setUp() {
      given(usernameUniquenessValidator.validate(any())).willReturn(Optional.empty());
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

      verify(userCreator).create(any());
    }
  }

  @Nested
  class GivenRolesAndCredentialsAreValidAndUsernameIsUnique {

    @BeforeEach
    void setUp() {
      given(usernameUniquenessValidator.validate(any())).willReturn(Optional.empty());
      given(roleValidator.validate(any(), any())).willReturn(Optional.empty());
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

      verify(userCreator).create(any());
    }

    @Test
    void registerWithRoles_registersEvents() {
      underTest.apply(ONE_ROLE_REGISTRATION_REQUEST);

      UUID correlationId = RequestCorrelation.id();

      ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
      verify(auditTracer, times(3)).save(eventCaptor.capture());
      List<AuditEvent> auditEvent = eventCaptor.getAllValues();

      assertThat(auditEvent.get(0).getType()).isEqualTo("InternalUserCreationRequested");
      assertThat(auditEvent.get(0).getEntityAction()).isEqualTo(CREATE.toString());
      assertThat(auditEvent.get(0).getCorrelationId()).isEqualTo(correlationId);
      assertThat(auditEvent.get(0).getDetails()).isEqualTo(obfuscatePasswordFieldForTest(
          ONE_ROLE_REGISTRATION_REQUEST));

      assertThat(auditEvent.get(1).getType()).isEqualTo("UserCreated");
      assertThat(auditEvent.get(1).getEntityAction()).isEqualTo(CREATE.toString());
      assertThat(auditEvent.get(1).getCorrelationId()).isEqualTo(correlationId);

      assertThat(auditEvent.get(2).getType()).isEqualTo("RolesAssigned");
      assertThat(auditEvent.get(2).getEntityAction()).isEqualTo(UPDATE.toString());
      assertThat(auditEvent.get(2).getCorrelationId()).isEqualTo(correlationId);
    }
  }

  @Nested
  class GivenUsernameIsNotUnique {

    @BeforeEach
    void setUp() {
      given(usernameUniquenessValidator.validate(any())).willReturn(Optional.of(
          UserRegistrationUseCaseFixtures.USERNAME_NOT_UNIQUE));
    }

    @Test
    void whenNoRoles_returnsValidDomainError() {
      Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
          underTest.apply(NO_ROLES_REGISTRATION_REQUEST);

      assertThatResult(actual).isFailureOfType(UsernameNotUniqueError.class);
      verifyAuditLog(NO_ROLES_REGISTRATION_REQUEST);
    }

    @Test
    void whenNoRoles_doesNotSaveToRepo() {
      underTest.apply(NO_ROLES_REGISTRATION_REQUEST);

      verifyNoMoreInteractions(userCreator);
      verifyAuditLog(NO_ROLES_REGISTRATION_REQUEST);
    }
  }

  @Nested
  class GivenUsernameIsUniqueAndRolesDontExist {

    @BeforeEach
    void setUp() {
      given(usernameUniquenessValidator.validate(any())).willReturn(Optional.empty());
      given(roleValidator.validate(any(), any())).willReturn(Optional.of(
          UserRegistrationUseCaseFixtures.ROLES_DONT_EXIST));
    }

    @Test
    void registerWithRoles_returnsValidDomainError() {
      Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
          underTest.apply(ONE_ROLE_REGISTRATION_REQUEST);

      assertThatResult(actual).isFailureOfType(RolesDontExistError.class);
      verifyAuditLog(ONE_ROLE_REGISTRATION_REQUEST);
    }

    @Test
    void registerWithRoles_doesNotSaveToRepo() {
      underTest.apply(ONE_ROLE_REGISTRATION_REQUEST);

      verifyNoInteractions(userCreator);
      verifyAuditLog(ONE_ROLE_REGISTRATION_REQUEST);
    }
  }

  @Nested
  class GivenUsernameIsUniqueRolesAndCredentialsAreInvalid {

    @BeforeEach
    void setUp() {
      given(usernameUniquenessValidator.validate(any())).willReturn(Optional.empty());
      given(roleValidator.validate(any(), any())).willReturn(Optional.empty());
    }

    @Test
    void whenPasswordIsTooShort_returnsValidDomainError() {
      Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
          underTest.apply(TOO_SHORT_PASSWORD_REQUEST);

      assertThatResult(actual).isFailureOfType(RegexError.class);
      verifyAuditLog(TOO_SHORT_PASSWORD_REQUEST);
    }

    @Test
    void whenPasswordDoesNotHaveDigit_returnsValidDomainError() {
      Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
          underTest.apply(NO_DIGIT_PASSWORD_REQUEST);

      assertThatResult(actual).isFailureOfType(RegexError.class);
      verifyAuditLog(NO_DIGIT_PASSWORD_REQUEST);
    }

    @Test
    void whenPasswordDoesNotHaveLetter_returnsValidDomainError() {
      Either<UserDomainError, RegisterInternalUserUseCase.Success> actual =
          underTest.apply(NO_LETTER_PASSWORD_REQUEST);

      assertThatResult(actual).isFailureOfType(RegexError.class);
      verifyAuditLog(NO_LETTER_PASSWORD_REQUEST);
    }
  }

  private void verifyAuditLog(RegisterInternalUserCommand details) {
    UUID correlationId = RequestCorrelation.id();

    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer).save(eventCaptor.capture());
    AuditEvent auditEvent = eventCaptor.getValue();

    assertThat(auditEvent.getType()).isEqualTo("InternalUserCreationRequested");
    assertThat(auditEvent.getEntityAction()).isEqualTo(CREATE.toString());
    assertThat(auditEvent.getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.getDetails()).isEqualTo(obfuscatePasswordFieldForTest(details));
  }

  private RegisterInternalUserCommand obfuscatePasswordFieldForTest(
      RegisterInternalUserCommand command) {
    return RegisterInternalUserUseCase.RegisterInternalUserCommand
        .builder()
        .displayName(command.getDisplayName())
        .password(OBFUSCATED_STRING)
        .roles(command.getRoles())
        .username(command.getUsername())
        .build();
  }
}
