package com.silenteight.sens.webapp.users.bulk;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.sens.webapp.users.bulk.dto.Analyst;
import com.silenteight.sens.webapp.users.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.sens.webapp.kernel.security.authority.Role.ROLE_ANALYST;
import static java.util.stream.Collectors.toList;

class AnalystSynchronizer {

  public SynchronizedAnalysts synchronize(List<User> users, List<Analyst> analysts) {
    return new SynchronizedAnalysts(
        getAnalystsToCreate(users, analysts),
        getAnalystsToUpdate(users, analysts),
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

  private static List<Long> getAnalystsToUpdate(List<User> users, List<Analyst> analysts) {
    return getExternalUsers(users)
        .filter(u -> isUserAppearOnApproverList(u, analysts))
        .filter(u -> !u.hasRole(ROLE_ANALYST))
        .map(User::getId)
        .collect(toList());
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

  @Value
  public static class SynchronizedAnalysts {

    @NonNull
    List<Analyst> added;

    @NonNull
    List<Long> updated;

    @NonNull
    List<String> deleted;
  }
}
