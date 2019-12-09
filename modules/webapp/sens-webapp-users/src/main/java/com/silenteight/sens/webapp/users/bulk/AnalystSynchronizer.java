package com.silenteight.sens.webapp.users.bulk;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.sens.webapp.users.bulk.dto.Analyst;
import com.silenteight.sens.webapp.users.bulk.dto.UpdatedUser;
import com.silenteight.sens.webapp.users.user.User;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.silenteight.sens.webapp.kernel.security.authority.Role.ROLE_ANALYST;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

class AnalystSynchronizer {

  public SynchronizedAnalysts synchronize(List<User> users, List<Analyst> analysts) {
    return new SynchronizedAnalysts(
        getAnalystsToCreate(users, analysts),
        getAnalystsWithMissingRole(users, analysts),
        getAnalystsWithUpdatedDisplayName(users, analysts),
        getAnalystsToDelete(users, analysts));
  }

  private static List<Analyst> getAnalystsToCreate(List<User> users, List<Analyst> analysts) {
    List<Analyst> analystsToCreate = new ArrayList<>(analysts);
    List<String> userNames = convertToUserNames(users);
    return analystsToCreate
        .stream()
        .filter(analyst -> !userNames.contains(analyst.getLogin()))
        .collect(toList());
  }

  private static List<String> convertToUserNames(List<User> users) {
    return users.stream().map(User::getUserName).collect(toList());
  }

  private static List<Long> getAnalystsWithMissingRole(List<User> users, List<Analyst> analysts) {
    return getExternalUsers(users)
        .filter(u -> isUserAppearOnApproverList(u, analysts))
        .filter(u -> !u.hasRole(ROLE_ANALYST))
        .map(User::getId)
        .collect(toList());
  }

  private static List<UpdatedUser> getAnalystsWithUpdatedDisplayName(
      List<User> users, List<Analyst> analysts) {

    List<Analyst> analystsToUpdate = new ArrayList<>(analysts);
    Map<String, User> userByUserNameMap = getUserByUserNameMap(users);
    return analystsToUpdate
        .stream()
        .filter(analyst -> analystIsOnUserList(analyst, userByUserNameMap))
        .map(analyst -> new AnalystUserPair(analyst, userByUserNameMap.get(analyst.getLogin())))
        .filter(AnalystUserPair::differentDisplayNames)
        .map(pair -> new UpdatedUser(pair.getUserId(), pair.getAnalystDisplayName()))
        .collect(toList());
  }

  private static Map<String, User> getUserByUserNameMap(List<User> users) {
    return getExternalUsers(users).collect(toMap(User::getUserName, Function.identity()));
  }

  private static boolean analystIsOnUserList(
      Analyst analyst, Map<String, User> userByUserNameMap) {

    return userByUserNameMap.containsKey(analyst.getLogin());
  }

  private static Long getUserId(String userName, Map<String, User> userByUserNameMap) {
    return Optional
        .ofNullable(userByUserNameMap.get(userName))
        .map(User::getId)
        .orElse(-1L);
  }

  private static List<String> getAnalystsToDelete(List<User> users, List<Analyst> analysts) {
    return getExternalUsers(users)
        .filter(u -> !isUserAppearOnApproverList(u, analysts))
        .filter(u -> u.hasOnlyRole(ROLE_ANALYST))
        .map(User::getUserName)
        .collect(toList());
  }

  private static boolean isUserAppearOnApproverList(User user, List<Analyst> analysts) {
    return analysts
        .stream()
        .map(Analyst::getLogin)
        .anyMatch(login -> login.equals(user.getUserName()));
  }

  private static Stream<User> getExternalUsers(List<User> users) {
    return users.stream().filter(User::isExternalUser);
  }

  @Data
  @RequiredArgsConstructor
  private static class AnalystUserPair {

    @NonNull
    private final Analyst analyst;

    @NonNull
    private final User user;

    public boolean differentDisplayNames() {
      return !StringUtils.equals(analyst.getDisplayName(), user.getDisplayName());
    }

    public Long getUserId() {
      return user.getId();
    }

    public String getAnalystDisplayName() {
      return analyst.getDisplayName();
    }
  }

  @Value
  public static class SynchronizedAnalysts {

    @NonNull
    List<Analyst> added;

    @NonNull
    List<Long> missingRole;

    @NonNull
    List<UpdatedUser> updatedDisplayName;

    @NonNull
    List<String> deleted;
  }
}
