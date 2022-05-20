package com.silenteight.universaldatasource.common.resource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AlertName {

  public static List<String> getListOfAlerts(Integer... alertIds) {
    return Arrays.stream(alertIds)
        .map(AlertName::getAlertName)
        .collect(toList());
  }

  private static String getAlertName(int alertId) {
    return "alerts/" + alertId;
  }
}
