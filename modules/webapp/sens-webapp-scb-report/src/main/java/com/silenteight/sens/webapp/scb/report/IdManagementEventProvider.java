package com.silenteight.sens.webapp.scb.report;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingFinder;
import com.silenteight.sens.webapp.user.remove.RemovedUserDetails;
import com.silenteight.sens.webapp.user.update.UpdatedUserDetails;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.sep.usermanagement.api.CompletedUserRegistration;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class IdManagementEventProvider {

  private static final String CREATED_WITHOUT_ROLES = "NO_ROLE";
  private static final JsonConversionHelper JSON_CONVERTER = JsonConversionHelper.INSTANCE;
  private static final String USER_CREATED = "UserCreated";
  private static final String USER_UPDATED = "UserUpdated";
  private static final String USER_REMOVED = "UserRemoved";

  private static final Map<String, String> ACTIONS =
      Map.of(USER_CREATED, "CREATE", USER_UPDATED, "MODIFY", USER_REMOVED, "DELETE");

  private static final Map<String, Function<AuditDataDto, Collection<String>>>
      ROLES_EXTRACTING_FUNCTIONS = Map.of(
      USER_CREATED, IdManagementEventProvider::createdUserRolesOf,
      USER_UPDATED, IdManagementEventProvider::updatedUserRolesOf,
      USER_REMOVED, IdManagementEventProvider::removedUserRolesOf);

  @NonNull
  private final AuditingFinder auditingFinder;

  List<IdManagementEventDto> idManagementEvents(
      @NonNull OffsetDateTime from, @NonNull OffsetDateTime to) {

    return auditingFinder.find(from, to, ACTIONS.keySet())
        .stream()
        .map(this::idManagementEventDtoFrom)
        .collect(toList());
  }

  private IdManagementEventDto idManagementEventDtoFrom(AuditDataDto auditData) {
    Collection<String> roles = ROLES_EXTRACTING_FUNCTIONS.get(auditData.getType()).apply(auditData);
    return IdManagementEventDto.builder()
        .username(auditData.getEntityId())
        .action(getAction(auditData, roles))
        .principal(auditData.getPrincipal())
        .roles(roles)
        .timestamp(auditData.getTimestamp().toInstant())
        .build();
  }

  private static String getAction(AuditDataDto auditData, Collection<String> roles) {
    if (allRolesRemovedDuringUpdate(auditData, roles))
      return ACTIONS.get(USER_REMOVED);

    return ACTIONS.get(auditData.getType());
  }

  private static boolean allRolesRemovedDuringUpdate(
      AuditDataDto auditData, Collection<String> roles) {
    return roles.isEmpty() && USER_UPDATED.equals(auditData.getType());
  }

  private static Collection<String> createdUserRolesOf(AuditDataDto auditData) {
    CompletedUserRegistration completedUserRegistration =
        JSON_CONVERTER.deserializeObject(
            JSON_CONVERTER.deserializeFromString(auditData.getDetails()),
            CompletedUserRegistration.class);
    Set<String> roles = completedUserRegistration.getRoles();
    return roles.isEmpty() ? Set.of(CREATED_WITHOUT_ROLES) : roles;
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
