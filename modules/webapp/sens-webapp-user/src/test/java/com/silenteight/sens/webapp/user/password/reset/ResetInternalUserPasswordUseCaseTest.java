package com.silenteight.sens.webapp.user.password.reset;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.api.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;
import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.user.password.SensCompatiblePasswordGenerator;
import com.silenteight.sens.webapp.user.password.reset.ResetInternalUserPasswordUseCase.UserIsNotInternalException;
import com.silenteight.sens.webapp.user.password.reset.ResetInternalUserPasswordUseCase.UserNotFoundException;
import com.silenteight.sep.usermanagement.api.credentials.UserCredentialsQuery;
import com.silenteight.sep.usermanagement.api.credentials.UserCredentialsResetter;
import com.silenteight.sep.usermanagement.api.credentials.dto.TemporaryPassword;


import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.UPDATE;
import static com.silenteight.sens.webapp.user.password.reset.ResetInternalUserPasswordUseCaseTest.ResetInternalUserPasswordUseCaseFixtures.GENERATED_PASSWORD;
import static com.silenteight.sens.webapp.user.password.reset.ResetInternalUserPasswordUseCaseTest.ResetInternalUserPasswordUseCaseFixtures.INTERNAL_CREDENTIALS;
import static com.silenteight.sens.webapp.user.password.reset.ResetInternalUserPasswordUseCaseTest.ResetInternalUserPasswordUseCaseFixtures.NON_INTERNAL_CREDENTIALS;
import static com.silenteight.sens.webapp.user.password.reset.ResetInternalUserPasswordUseCaseTest.ResetInternalUserPasswordUseCaseFixtures.USERNAME;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ResetInternalUserPasswordUseCaseTest {

  @InjectMocks
  private ResetInternalUserPasswordUseCase underTest;

  @Mock
  private UserCredentialsQuery credentialsRepo;
  @Mock
  private SensCompatiblePasswordGenerator passwordGenerator;
  @Mock
  private AuditTracer auditTracer;

  @Test
  void userDoesntExist_throwsException() {
    given(credentialsRepo.findByUsername(USERNAME)).willReturn(empty());

    ThrowingCallable when = () -> underTest.execute(USERNAME);

    assertThatThrownBy(when).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void userExistAndIsInternal_generatesPasswordResetsAndReturnsGenerated() {
    UUID correlationId = RequestCorrelation.id();
    given(credentialsRepo.findByUsername(USERNAME)).willReturn(of(INTERNAL_CREDENTIALS));
    given(passwordGenerator.generate()).willReturn(TemporaryPassword.of(GENERATED_PASSWORD));

    TemporaryPassword actual = underTest.execute(USERNAME);

    then(passwordGenerator).should().generate();
    assertThat(INTERNAL_CREDENTIALS.isResetInvoked()).isTrue();
    assertThat(actual).extracting(TemporaryPassword::getPassword).isEqualTo(GENERATED_PASSWORD);

    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer, times(2)).save(eventCaptor.capture());
    List<AuditEvent> auditEvents = eventCaptor.getAllValues();

    assertAuditEvent(auditEvents.get(0), "PasswordResetRequested", correlationId);
    assertAuditEvent(auditEvents.get(1), "PasswordReset", correlationId);
  }

  private void assertAuditEvent(AuditEvent auditEvent, String type, UUID correlationId) {
    assertThat(auditEvent.getType()).isEqualTo(type);
    assertThat(auditEvent.getEntityAction()).isEqualTo(UPDATE.toString());
    assertThat(auditEvent.getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.getDetails()).isNull();
  }

  @Test
  void userExistsAndIsNotInternal_throwsException() {
    given(credentialsRepo.findByUsername(USERNAME)).willReturn(of(NON_INTERNAL_CREDENTIALS));

    ThrowingCallable when = () -> underTest.execute(USERNAME);

    assertThatThrownBy(when).isInstanceOf(UserIsNotInternalException.class);
  }

  static class ResetInternalUserPasswordUseCaseFixtures {

    static final String USERNAME = "jdoe123";

    static final String GENERATED_PASSWORD = "!ncr3d!bly$tr0ngP@$$w0rD";

    static final SimpleUserCredentials NON_INTERNAL_CREDENTIALS =
        new SimpleUserCredentials(false);
    static final SimpleUserCredentials INTERNAL_CREDENTIALS = new SimpleUserCredentials(true);
  }

  @RequiredArgsConstructor
  static class SimpleUserCredentials implements UserCredentialsResetter {

    private final boolean isInternal;

    @Getter
    private boolean resetInvoked = false;

    @Override
    public void reset(TemporaryPassword temporaryPassword) {
      resetInvoked = true;
    }

    @Override
    public boolean ownerIsInternal() {
      return isInternal;
    }
  }
}
