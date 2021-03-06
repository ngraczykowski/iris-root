package com.silenteight.sens.webapp.scb.report;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditDataDto.AuditDataDtoBuilder;
import com.silenteight.auditing.bs.AuditingFinder;
import com.silenteight.sens.webapp.user.registration.CreatedUserDetails;
import com.silenteight.sens.webapp.user.registration.CreatedUserDetails.ObfuscatedCredentials;
import com.silenteight.sens.webapp.user.registration.CreatedUserDetails.UserDetails;
import com.silenteight.sens.webapp.user.remove.RemoveUserUseCase.RemoveUserCommand;
import com.silenteight.sens.webapp.user.remove.RemovedUserDetails;
import com.silenteight.sens.webapp.user.update.UpdatedUserDetails;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.sep.usermanagement.api.user.dto.UpdateUserCommand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.time.OffsetDateTime.now;
import static java.util.Collections.emptySet;
import static java.util.Set.of;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdManagementEventProviderTest {

  private static final JsonConversionHelper JSON_CONVERTER = JsonConversionHelper.INSTANCE;
  private static final Set<String> USER_MANAGEMENT_EVENT_TYPES =
      of("UserCreated", "UserUpdated", "UserRemoved");

  @Mock
  private AuditingFinder auditingFinder;

  private IdManagementEventProvider underTest;

  @BeforeEach
  void setUp() {
    underTest = new IdManagementEventProvider(auditingFinder);
  }

  @Test
  void returnsCreateEventBasingOnAudit() {
    String username = "user11";
    String principal = "user2";
    String role1 = "roleA";
    String role2 = "roleB";
    Set<String> roles = Set.of(role1, role2);

    OffsetDateTime from = now().minusHours(6);
    OffsetDateTime to = now();

    when(auditingFinder.find(from, to, USER_MANAGEMENT_EVENT_TYPES))
        .thenReturn(List.of(
            auditDataDtoWithDefaults()
                .entityId(username)
                .timestamp(Timestamp.valueOf("2020-04-15 12:14:32"))
                .type("UserCreated")
                .principal(principal)
                .details(jsonStringOf(createdUserDetailsWith(roles)))
                .build()));

    List<IdManagementEventDto> idManagementEvents =
        underTest.idManagementEvents(from, to);

    assertThat(idManagementEvents).hasSize(1);

    IdManagementEventDto idManagementEvent1 = idManagementEvents.get(0);
    assertThat(idManagementEvent1.getUsername()).isEqualTo(username);
    assertThat(idManagementEvent1.getTimestamp()).isEqualTo(
        Instant.parse("2020-04-15T12:14:32Z"));
    assertThat(idManagementEvent1.getPrincipal()).isEqualTo(principal);
    assertThat(idManagementEvent1.getAction()).isEqualTo("CREATE");
    assertThat(idManagementEvent1.getRoles()).contains(role1, role2);
  }

  @Test
  void returnsCreateEventBasingOnAudit_userHasNoRoles() {
    OffsetDateTime from = now().minusHours(12);
    OffsetDateTime to = now();

    when(auditingFinder.find(from, to, USER_MANAGEMENT_EVENT_TYPES))
        .thenReturn(List.of(
            auditDataDtoWithDefaults()
                .type("UserCreated")
                .details(jsonStringOf(createdUserDetailsWith(emptySet())))
                .build()));

    List<IdManagementEventDto> idManagementEvents =
        underTest.idManagementEvents(from, to);

    assertThat(idManagementEvents).hasSize(1);

    IdManagementEventDto idManagementEvent1 = idManagementEvents.get(0);
    assertThat(idManagementEvent1.getAction()).isEqualTo("CREATE");
    assertThat(idManagementEvent1.getRoles()).contains("NO_ROLE");
  }

  @Test
  void returnsModifyEventBasingOnAudit() {
    String username = "user2";
    String principal = "user3";
    String role = "roleC";

    OffsetDateTime from = now().minusHours(7);
    OffsetDateTime to = now();
    Set<String> roles = Set.of(role);

    when(auditingFinder.find(from, to, USER_MANAGEMENT_EVENT_TYPES))
        .thenReturn(List.of(
            auditDataDtoWithDefaults()
                .entityId(username)
                .timestamp(Timestamp.valueOf("2020-04-15 12:14:32.456"))
                .type("UserUpdated")
                .principal(principal)
                .details(jsonStringOf(updatedUserDetailsWith(roles)))
                .build()));

    List<IdManagementEventDto> idManagementEvents =
        underTest.idManagementEvents(from, to);

    assertThat(idManagementEvents).hasSize(1);

    IdManagementEventDto idManagementEvent1 = idManagementEvents.get(0);
    assertThat(idManagementEvent1.getUsername()).isEqualTo(username);
    assertThat(idManagementEvent1.getTimestamp()).isEqualTo(
        Instant.parse("2020-04-15T12:14:32.456Z"));
    assertThat(idManagementEvent1.getPrincipal()).isEqualTo(principal);
    assertThat(idManagementEvent1.getAction()).isEqualTo("MODIFY");
    assertThat(idManagementEvent1.getRoles()).contains(role);
  }

  @Test
  void returnsDeleteEventBasingOnAudit_userHasNoRoles() {
    String username = "user2";
    String principal = "user3";

    OffsetDateTime from = now().minusHours(7);
    OffsetDateTime to = now();

    when(auditingFinder.find(from, to, USER_MANAGEMENT_EVENT_TYPES))
        .thenReturn(List.of(
            auditDataDtoWithDefaults()
                .entityId(username)
                .timestamp(Timestamp.valueOf("2020-04-15 12:14:32.456"))
                .type("UserUpdated")
                .principal(principal)
                .details(jsonStringOf(updatedUserDetailsWith(emptySet())))
                .build()));

    List<IdManagementEventDto> idManagementEvents =
        underTest.idManagementEvents(from, to);

    assertThat(idManagementEvents).hasSize(1);

    IdManagementEventDto idManagementEvent1 = idManagementEvents.get(0);
    assertThat(idManagementEvent1.getUsername()).isEqualTo(username);
    assertThat(idManagementEvent1.getTimestamp()).isEqualTo(
        Instant.parse("2020-04-15T12:14:32.456Z"));
    assertThat(idManagementEvent1.getPrincipal()).isEqualTo(principal);
    assertThat(idManagementEvent1.getAction()).isEqualTo("DELETE");
    assertThat(idManagementEvent1.getRoles()).isEmpty();
  }

  @Test
  void returnsDeleteEventBasingOnAudit() {
    String username = "user3";
    String principal = "user4";
    String role = "roleD";

    OffsetDateTime from = now().minusHours(7);
    OffsetDateTime to = now();
    List<String> roles = List.of(role);

    when(auditingFinder.find(from, to, USER_MANAGEMENT_EVENT_TYPES))
        .thenReturn(List.of(
            auditDataDtoWithDefaults()
                .entityId(username)
                .timestamp(Timestamp.valueOf("2020-04-16 12:14:32.789"))
                .type("UserRemoved")
                .principal(principal)
                .details(jsonStringOf(removedUserDetailsWith(roles)))
                .build()));

    List<IdManagementEventDto> idManagementEvents =
        underTest.idManagementEvents(from, to);

    assertThat(idManagementEvents).hasSize(1);

    IdManagementEventDto idManagementEvent1 = idManagementEvents.get(0);
    assertThat(idManagementEvent1.getUsername()).isEqualTo(username);
    assertThat(idManagementEvent1.getTimestamp()).isEqualTo(
        Instant.parse("2020-04-16T12:14:32.789Z"));
    assertThat(idManagementEvent1.getPrincipal()).isEqualTo(principal);
    assertThat(idManagementEvent1.getAction()).isEqualTo("DELETE");
    assertThat(idManagementEvent1.getRoles()).contains(role);
  }

  private static RemovedUserDetails removedUserDetailsWith(Collection<String> roles) {
    return new RemovedUserDetails(
        RemoveUserCommand.builder()
            .username("not_used")
            .expectedOrigin("not_used")
            .build(),
        roles);
  }

  private static AuditDataDtoBuilder auditDataDtoWithDefaults() {
    return AuditDataDto.builder()
        .eventId(randomUUID())
        .correlationId(randomUUID())
        .timestamp(Timestamp.valueOf(LocalDateTime.now()));
  }

  private static CreatedUserDetails createdUserDetailsWith(Set<String> roles) {
    return new CreatedUserDetails(
        new UserDetails(
            "not_used",
            "not_used",
            new ObfuscatedCredentials(),
            roles),
        "not_used",
        now());
  }

  private static UpdatedUserDetails updatedUserDetailsWith(Set<String> roles) {
    return new UpdatedUserDetails(
        UpdateUserCommand.builder()
            .username("not_used")
            .updateDate(now())
            .build(),
        null,
        roles);
  }

  private static String jsonStringOf(Object details) {
    JSON_CONVERTER.objectMapper().disable(WRITE_DATES_AS_TIMESTAMPS);
    try {
      return JSON_CONVERTER.serializeToString(details);
    } finally {
      JSON_CONVERTER.objectMapper().enable(WRITE_DATES_AS_TIMESTAMPS);
    }
  }
}
