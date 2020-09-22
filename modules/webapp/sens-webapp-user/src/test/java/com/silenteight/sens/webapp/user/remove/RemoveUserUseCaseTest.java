package com.silenteight.sens.webapp.user.remove;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.trace.AuditEvent;
import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.user.remove.RemoveUserUseCase.RemoveUserCommand;
import com.silenteight.sep.usermanagement.api.UserQuery;
import com.silenteight.sep.usermanagement.api.UserRemover;
import com.silenteight.sep.usermanagement.api.dto.UserDto;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.silenteight.sens.webapp.audit.trace.AuditEvent.EntityAction.DELETE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveUserUseCaseTest {

  @Mock
  private UserQuery userQuery;
  @Mock
  private UserRemover userRemover;
  @Mock
  private AuditTracer auditTracer;

  private RemoveUserUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new RemoveUserUseCase(userQuery, userRemover, auditTracer);
  }

  @Test
  void removeUser() {
    String username = "jsmith";
    String origin = "orig123";
    when(userQuery.find(username)).thenReturn(Optional.of(userDtoWith(origin)));

    underTest.apply(
        RemoveUserCommand
            .builder()
            .username(username)
            .expectedOrigin(origin)
            .build());

    verify(userRemover).remove(username);
  }

  @Test
  void registerAuditEvents() {
    String username = "abc";
    String origin = "oryg123";

    when(userQuery.find(username)).thenReturn(Optional.of(userDtoWith(origin)));
    UUID correlationId = RequestCorrelation.id();

    RemoveUserCommand command = RemoveUserCommand
        .builder()
        .username(username)
        .expectedOrigin(origin)
        .build();
    underTest.apply(command);

    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer, times(2)).save(eventCaptor.capture());
    List<AuditEvent> auditEvents = eventCaptor.getAllValues();

    assertAuditEvent(auditEvents.get(0), "UserRemovalRequested", correlationId, command);
    assertAuditEvent(auditEvents.get(1), "UserRemoved", correlationId, command);
  }

  @Test
  void throwsExceptionIfOriginDoesNotMatch() {
    String username = "userABC";

    when(userQuery.find(username)).thenReturn(Optional.of(userDtoWith("some_origin")));

    ThrowingCallable removalCall = () ->
        underTest.apply(
            RemoveUserCommand
                .builder()
                .username(username)
                .expectedOrigin("other_origin")
                .build());

    assertThatThrownBy(removalCall).isInstanceOf(OriginNotMatchingException.class);
  }

  private UserDto userDtoWith(String origin) {
    UserDto userDto = new UserDto();
    userDto.setOrigin(origin);
    return userDto;
  }

  private void assertAuditEvent(
      AuditEvent auditEvent, String type, UUID correlationId, RemoveUserCommand command) {
    assertThat(auditEvent.getType()).isEqualTo(type);
    assertThat(auditEvent.getEntityAction()).isEqualTo(DELETE.toString());
    assertThat(auditEvent.getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.getDetails()).isEqualTo(command);
  }
}
