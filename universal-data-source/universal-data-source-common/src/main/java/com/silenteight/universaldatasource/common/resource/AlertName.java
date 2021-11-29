package com.silenteight.universaldatasource.common.resource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AlertName {

  public static List<String> getListOfAlerts(List<Integer> alertIds) {
    return alertIds.stream()
        .map(AlertName::getAlertName)
        .collect(toList());
  }

  private static String getAlertName(int alertId) {
    return "alerts/" + alertId;
  }
}
