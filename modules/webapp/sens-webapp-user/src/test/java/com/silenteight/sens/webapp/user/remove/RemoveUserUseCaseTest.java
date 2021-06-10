package com.silenteight.sens.webapp.user.remove;

import com.silenteight.sens.webapp.audit.api.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;
import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.user.remove.RemoveUserUseCase.RemoveUserCommand;
import com.silenteight.sens.webapp.user.roles.ScopeUserRoles;
import com.silenteight.sens.webapp.user.roles.UserRolesRetriever;
import com.silenteight.sep.usermanagement.api.UserQuery;
import com.silenteight.sep.usermanagement.api.UserRemover;
import com.silenteight.sep.usermanagement.api.UserRoles;
import com.silenteight.sep.usermanagement.api.dto.UserDto;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.DELETE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveUserUseCaseTest {

  private static final String ROLES_SCOPE = "frontend";
  private static final String COUNTRY_GROUPS_SCOPE = "kibana";

  @Mock
  private UserQuery userQuery;
  @Mock
  private UserRemover userRemover;
  @Mock
  private AuditTracer auditTracer;
  @Mock
  private UserRolesRetriever userRolesRetriever;

  private RemoveUserUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new RemoveUserUseCase(
        userQuery, userRemover, auditTracer, userRolesRetriever, ROLES_SCOPE, COUNTRY_GROUPS_SCOPE);
  }

  @Test
  void removeUser() {
    String username = "jsmith";
    String origin = "orig123";
    when(userQuery.find(username, Set.of(ROLES_SCOPE, COUNTRY_GROUPS_SCOPE)))
        .thenReturn(Optional.of(userDtoWith(origin)));
    List<String> roles = List.of("role1", "role3");
    List<String> countryGroups = List.of("SG", "HK");
    Map<String, List<String>> scopeRoles = Map.of(
        ROLES_SCOPE, roles,
        COUNTRY_GROUPS_SCOPE, countryGroups);
    UserRoles userRoles = new ScopeUserRoles(scopeRoles);
    when(userRolesRetriever.rolesOf(username)).thenReturn(userRoles);

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

    when(userQuery.find(username,  Set.of(ROLES_SCOPE, COUNTRY_GROUPS_SCOPE)))
        .thenReturn(Optional.of(userDtoWith(origin)));
    List<String> roles = List.of("role1", "role3");
    List<String> countryGroups = List.of("SG", "HK");
    Map<String, List<String>> scopeRoles = Map.of(
        ROLES_SCOPE, roles,
        COUNTRY_GROUPS_SCOPE, countryGroups);
    UserRoles userRoles = new ScopeUserRoles(scopeRoles);
    when(userRolesRetriever.rolesOf(username)).thenReturn(userRoles);

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

    AuditEvent auditEvent1 = auditEvents.get(0);
    assertThat(auditEvent1.getType()).isEqualTo("UserRemovalRequested");
    assertThat(auditEvent1.getEntityAction()).isEqualTo(DELETE.toString());
    assertThat(auditEvent1.getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent1.getDetails()).isEqualTo(command);

    AuditEvent auditEvent = auditEvents.get(1);
    assertThat(auditEvent.getType()).isEqualTo("UserRemoved");
    assertThat(auditEvent.getEntityAction()).isEqualTo(DELETE.toString());
    assertThat(auditEvent.getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.getDetails())
        .isEqualTo(new RemovedUserDetails(command, new HashSet<>(roles)));
  }

  @Test
  void throwsExceptionIfOriginDoesNotMatch() {
    String username = "userABC";

    when(userQuery.find(username,  Set.of(ROLES_SCOPE, COUNTRY_GROUPS_SCOPE)))
        .thenReturn(Optional.of(userDtoWith("some_origin")));

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
}
