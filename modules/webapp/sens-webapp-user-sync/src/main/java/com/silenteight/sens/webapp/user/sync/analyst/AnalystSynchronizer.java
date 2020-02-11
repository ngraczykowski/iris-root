package com.silenteight.sens.webapp.user.sync.analyst;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.sens.webapp.user.sync.analyst.dto.Analyst;
import com.silenteight.sens.webapp.user.sync.analyst.dto.ExternalAnalyst;
import com.silenteight.sens.webapp.user.sync.analyst.dto.InternalAnalyst;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

class AnalystSynchronizer {

  SynchronizedAnalysts synchronize(
      List<InternalAnalyst> internalAnalysts, List<ExternalAnalyst> externalAnalysts) {

    return new SynchronizedAnalysts(
        analystsToCreate(internalAnalysts, externalAnalysts),
        analystsToUpdate(internalAnalysts, externalAnalysts),
        analystsToDelete(internalAnalysts, externalAnalysts));
  }

  private static List<NewAnalyst> analystsToCreate(
      List<InternalAnalyst> internalAnalysts, List<ExternalAnalyst> externalAnalysts) {

    Set<String> userNames = extractUserNames(internalAnalysts);
    return externalAnalysts
        .stream()
        .filter(it -> !userNames.contains(it.getUserName()))
        .map(NewAnalyst::toNewAnalyst)
        .collect(toList());
  }

  private static Set<String> extractUserNames(List<? extends Analyst> analysts) {
    return analysts
        .stream()
        .map(Analyst::getUserName)
        .collect(toSet());
  }

  private static List<UpdatedAnalyst> analystsToUpdate(
      List<InternalAnalyst> internalAnalysts, List<ExternalAnalyst> externalAnalysts) {

    Map<String, ExternalAnalyst> groupedByUserName =
        groupByUserName(externalAnalysts);
    return internalAnalysts
        .stream()
        .filter(it -> groupedByUserName.containsKey(it.getUserName()))
        .map(it -> new AnalystPair(it, groupedByUserName.get(it.getUserName())))
        .filter(AnalystPair::haveDifferentDisplayNames)
        .map(AnalystPair::toUpdatedAnalyst)
        .collect(toList());
  }

  private static Map<String, ExternalAnalyst> groupByUserName(List<ExternalAnalyst> analysts) {
    return analysts
        .stream()
        .collect(toMap(ExternalAnalyst::getUserName, identity()));
  }

  private static List<String> analystsToDelete(
      List<InternalAnalyst> internalAnalysts, List<ExternalAnalyst> externalAnalysts) {

    Set<String> userNames = extractUserNames(externalAnalysts);
    return internalAnalysts
        .stream()
        .filter(it -> !userNames.contains(it.getUserName()))
        .map(InternalAnalyst::getUserName)
        .collect(toList());
  }

  @RequiredArgsConstructor
  private static class AnalystPair {

    @NonNull
    private final InternalAnalyst internal;

    @NonNull
    private final ExternalAnalyst external;

    boolean haveDifferentDisplayNames() {
      return !StringUtils.equals(getInternalDisplayName(), getExternalDisplayName());
    }

    private String getInternalUserName() {
      return internal.getUserName();
    }

    private String getInternalDisplayName() {
      return internal.getDisplayName();
    }

    private String getExternalDisplayName() {
      return external.getDisplayName();
    }

    UpdatedAnalyst toUpdatedAnalyst() {
      return new UpdatedAnalyst(getInternalUserName(), getExternalDisplayName());
    }
  }

  @Value
  static class SynchronizedAnalysts {

    @NonNull
    List<NewAnalyst> added;

    @NonNull
    List<UpdatedAnalyst> updated;

    @NonNull
    List<String> deleted;
  }

  @Value
  static class NewAnalyst {

    @NonNull
    String userName;

    String displayName;

    static NewAnalyst toNewAnalyst(ExternalAnalyst externalAnalyst) {
      return new NewAnalyst(externalAnalyst.getUserName(), externalAnalyst.getDisplayName());
    }
  }

  @Value
  static class UpdatedAnalyst {

    @NonNull
    String userName;

    String displayName;
  }
}
