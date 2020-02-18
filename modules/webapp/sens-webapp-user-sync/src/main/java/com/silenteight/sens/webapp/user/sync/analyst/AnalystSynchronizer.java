package com.silenteight.sens.webapp.user.sync.analyst;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.sens.webapp.user.dto.UserDto;
import com.silenteight.sens.webapp.user.sync.analyst.dto.Analyst;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.silenteight.sens.webapp.user.domain.UserRole.ANALYST;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

class AnalystSynchronizer {

  SynchronizedAnalysts synchronize(
      Collection<UserDto> users, Collection<Analyst> analysts) {

    return new SynchronizedAnalysts(
        analystsToCreate(users, analysts),
        analystsToUpdateRole(users, analysts),
        analystsToUpdateDisplayName(users, analysts),
        analystsToDelete(users, analysts));
  }

  private static List<Analyst> analystsToCreate(
      Collection<UserDto> users, Collection<Analyst> analysts) {

    Set<String> userNames = extractUserNames(users);
    return analysts
        .stream()
        .filter(it -> !userNames.contains(it.getUserName()))
        .collect(toList());
  }

  private static Set<String> extractUserNames(Collection<UserDto> users) {
    return users
        .stream()
        .map(UserDto::getUserName)
        .collect(toSet());
  }

  private static List<String> analystsToUpdateRole(
      Collection<UserDto> users, Collection<Analyst> analysts) {

    Set<String> analystUserNames = extractAnalystsUserNames(analysts);
    return getExternalUsers(users)
        .filter(it -> analystUserNames.contains(it.getUserName()))
        .filter(it -> !it.hasRole(ANALYST))
        .map(UserDto::getUserName)
        .collect(toList());
  }

  private static Set<String> extractAnalystsUserNames(Collection<Analyst> analysts) {
    return analysts
        .stream()
        .map(Analyst::getUserName)
        .collect(toSet());
  }

  private static Stream<UserDto> getExternalUsers(Collection<UserDto> users) {
    return users
        .stream()
        .filter(UserDto::isExternalUser);
  }

  private static List<UpdatedAnalyst> analystsToUpdateDisplayName(
      Collection<UserDto> users, Collection<Analyst> analysts) {

    Map<String, Analyst> analystByUserName = groupByUserName(analysts);
    return getExternalUsers(users)
        .filter(it -> analystByUserName.containsKey(it.getUserName()))
        .map(it -> new UserAnalystPair(it, analystByUserName.get(it.getUserName())))
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
    return getExternalUsers(users)
        .filter(it -> !analystUserNames.contains(it.getUserName()))
        .filter(it -> it.hasOnlyRole(ANALYST))
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
    List<String> updatedRole;

    @NonNull
    List<UpdatedAnalyst> updatedDisplayName;

    @NonNull
    List<String> deleted;

    int addedCount() {
      return added.size();
    }

    int updatedRoleCount() {
      return updatedRole.size();
    }

    int updatedDisplayNameCount() {
      return updatedDisplayName.size();
    }

    int deletedCount() {
      return deleted.size();
    }
  }

  @Value
  static class UpdatedAnalyst {

    @NonNull
    String userName;

    String displayName;
  }
}
