package com.silenteight.sens.webapp.scb.report;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingFinder;
import com.silenteight.sens.webapp.user.remove.RemovedUserDetails;
import com.silenteight.sens.webapp.user.update.UpdatedUserDetails;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.sep.usermanagement.api.CompletedUserRegistration;

import org.apache.commons.lang3.tuple.Pair;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class IdManagementEventProvider {

  // INFO(kdzieciol): If you need to do something in this class, please feel free to reformat
  //                  and rearrange the code to simplify it.
  private static final JsonConversionHelper JSON_CONVERTER = JsonConversionHelper.INSTANCE;
  private static final String USER_CREATED = "UserCreated";
  private static final String USER_UPDATED = "UserUpdated";
  private static final String USER_REMOVED = "UserRemoved";

  private static final Map<String, String> ACTIONS =
      Map.of(USER_CREATED, "CREATE", USER_UPDATED, "MODIFY", USER_REMOVED, "DELETE");

  private static final Map<String, Function<AuditDataDto, Collection<String>>>
      ROLES_EXTRACTING_FUNCTIONS = Map.of(
      USER_CREATED, auditData -> createdUserRolesOf(auditData),
      USER_UPDATED, auditData -> updatedUserRolesOf(auditData),
      USER_REMOVED, auditData -> removedUserRolesOf(auditData));

  @NonNull
  private final AuditingFinder auditingFinder;

  List<IdManagementEventDto> idManagementEvents(
      @NonNull OffsetDateTime from, @NonNull OffsetDateTime to) {

    return auditingFinder.find(from, to, ACTIONS.keySet())
        .stream()
        .map(auditDataDto ->
            Pair.of(
                auditDataDto,
                ROLES_EXTRACTING_FUNCTIONS.get(auditDataDto.getType())
                    .apply(auditDataDto)))
        .flatMap(auditDataAndRoles -> idManagementEventDtoStream(auditDataAndRoles))
        .collect(toList());
  }

  private Stream<IdManagementEventDto> idManagementEventDtoStream(
      Pair<AuditDataDto, Collection<String>> auditDataAndRoles) {
    AuditDataDto auditData = auditDataAndRoles.getLeft();
    Collection<String> roles = auditDataAndRoles.getRight();
    return roles.isEmpty() ?
           Stream.of(idManagementEventDtoFrom(auditData, null)) :
           roles.stream()
               .sorted()
               .map(role -> idManagementEventDtoFrom(auditData, role));
  }

  private IdManagementEventDto idManagementEventDtoFrom(AuditDataDto auditData, String role) {
    return IdManagementEventDto.builder()
        .username(auditData.getEntityId())
        .action(ACTIONS.get(auditData.getType()))
        .principal(auditData.getPrincipal())
        .role(role)
        .timestamp(auditData.getTimestamp().toInstant())
        .build();
  }

  private static Collection<String> createdUserRolesOf(AuditDataDto auditData) {
    CompletedUserRegistration completedUserRegistration =
        JSON_CONVERTER.deserializeObject(
            JSON_CONVERTER.deserializeFromString(auditData.getDetails()),
            CompletedUserRegistration.class);
    return completedUserRegistration.getRoles();
  }

  private static Collection<String> updatedUserRolesOf(AuditDataDto auditData) {
    UpdatedUserDetails updatedUserDetails =
        JSON_CONVERTER.deserializeObject(
            JSON_CONVERTER.deserializeFromString(auditData.getDetails()),
            UpdatedUserDetails.class);
    return updatedUserDetails.getCurrentRoles();
  }

  private static Collection<String> removedUserRolesOf(AuditDataDto auditData) {
    RemovedUserDetails removedUserDetails =
        JSON_CONVERTER.deserializeObject(
            JSON_CONVERTER.deserializeFromString(auditData.getDetails()),
            RemovedUserDetails.class);
    return removedUserDetails.getRoles();
  }
}
