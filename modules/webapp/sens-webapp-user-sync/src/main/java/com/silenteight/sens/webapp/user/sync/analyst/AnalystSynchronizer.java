package com.silenteight.sens.webapp.user.sync.analyst;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.user.dto.UserDto;
import com.silenteight.sens.webapp.user.sync.analyst.domain.GnsOrigin;
import com.silenteight.sens.webapp.user.sync.analyst.dto.Analyst;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.silenteight.sens.webapp.audit.api.AuditMarker.USER_MANAGEMENT;
import static com.silenteight.sens.webapp.user.domain.UserRole.ANALYST;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
class AnalystSynchronizer {

  private final AuditLog auditLog;

  SynchronizedAnalysts synchronize(
      @NonNull Collection<UserDto> users, @NonNull Collection<Analyst> analysts) {
    auditLog.logInfo(USER_MANAGEMENT, "Synchronizing Analysts");

    return new SynchronizedAnalysts(
        analystsToCreate(users, analysts),
        analystsToRestore(users, analysts),
        analystsToAddRole(users, analysts),
        analystsToUpdateDisplayName(users, analysts),
        analystsToDelete(users, analysts));
  }

  private static List<Analyst> analystsToCreate(
      Collection<UserDto> users, Collection<Analyst> analysts) {

    Set<String> userNames = extractUserNames(users);
    return analysts
        .stream()
        .filter(analyst -> !userNames.contains(analyst.getUserName()))
        .collect(toList());
  }

  private static Set<String> extractUserNames(Collection<UserDto> users) {
    return users
        .stream()
        .map(UserDto::getUserName)
        .collect(toSet());
  }

  private static List<String> analystsToRestore(
      Collection<UserDto> users, Collection<Analyst> analysts) {

    Set<String> analystUserNames = extractAnalystsUserNames(analysts);
    return getDeletedExternalUsers(users)
        .filter(user -> analystUserNames.contains(user.getUserName()))
        .filter(user -> user.hasOnlyRole(ANALYST))
        .map(UserDto::getUserName)
        .collect(toList());
  }

  private static Set<String> extractAnalystsUserNames(Collection<Analyst> analysts) {
    return analysts
        .stream()
        .map(Analyst::getUserName)
        .collect(toSet());
  }

  private static Stream<UserDto> getDeletedExternalUsers(Collection<UserDto> users) {
    return users
        .stream()
        .filter(user -> nonNull(user.getDeletedAt()))
        .filter(user -> user.hasOrigin(new GnsOrigin()));
  }

  private static List<String> analystsToAddRole(
      Collection<UserDto> users, Collection<Analyst> analysts) {

    Set<String> analystUserNames = extractAnalystsUserNames(analysts);
    return getNonDeletedExternalUsers(users)
        .filter(user -> analystUserNames.contains(user.getUserName()))
        .filter(user -> !user.hasRole(ANALYST))
        .map(UserDto::getUserName)
        .collect(toList());
  }

  private static Stream<UserDto> getNonDeletedExternalUsers(Collection<UserDto> users) {
    return users
        .stream()
        .filter(user -> isNull(user.getDeletedAt()))
        .filter(user -> user.hasOrigin(new GnsOrigin()));
  }

  private static List<UpdatedAnalyst> analystsToUpdateDisplayName(
      Collection<UserDto> users, Collection<Analyst> analysts) {

    Map<String, Analyst> analystByUserName = groupByUserName(analysts);
    return getNonDeletedExternalUsers(users)
        .filter(user -> analystByUserName.containsKey(user.getUserName()))
        .map(user -> new UserAnalystPair(user, analystByUserName.get(user.getUserName())))
        .filter(UserAnalystPair::haveDifferentDisplayNames)
        .map(UserAnalystPair::toUpdatedAnalyst)
        .collect(toList());
  }

  private static Map<String, Analyst> groupByUserName(Collection<Analyst> analysts) {
    return analysts
        .stream()
        .collect(toMap(Analyst::getUserName, identity()));
  }

  private static List<String> analystsToDelete(
      Collection<UserDto> users, Collection<Analyst> analysts) {

    Set<String> analystUserNames = extractAnalystsUserNames(analysts);
    return getNonDeletedExternalUsers(users)
        .filter(user -> !analystUserNames.contains(user.getUserName()))
        .filter(user -> user.hasOnlyRole(ANALYST))
        .map(UserDto::getUserName)
        .collect(toList());
  }

  @RequiredArgsConstructor
  private static class UserAnalystPair {

    @NonNull
    private final UserDto user;

    @NonNull
    private final Analyst analyst;

    boolean haveDifferentDisplayNames() {
      return !StringUtils.equals(getUserDisplayName(), getAnalystDisplayName());
    }

    private String getUserUserName() {
      return user.getUserName();
    }

    private String getUserDisplayName() {
      return user.getDisplayName();
    }

    private String getAnalystDisplayName() {
      return analyst.getDisplayName();
    }

    UpdatedAnalyst toUpdatedAnalyst() {
      return new UpdatedAnalyst(getUserUserName(), getAnalystDisplayName());
    }
  }

  @Value
  static class SynchronizedAnalysts {

    @NonNull
    List<Analyst> added;

    @NonNull
    List<String> restored;

    @NonNull
    List<String> addedRole;

    @NonNull
    List<UpdatedAnalyst> updatedDisplayName;

    @NonNull
    List<String> deleted;
  }

  @Value
  static class UpdatedAnalyst {

    @NonNull
    String userName;

    String displayName;
  }
}
