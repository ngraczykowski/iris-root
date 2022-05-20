package com.silenteight.payments.bridge.ae.alertregistration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlertFixture {

  public static List<String> getListOfAlerts(Integer... alertIds) {
    return Arrays.stream(alertIds)
        .map(AlertFixture::getAlertName)
        .collect(toList());
  }

  private static String getAlertName(int alertId) {
    return "alerts/" + alertId;
  }

}
