package com.silenteight.bridge.core.registration.domain.model;

import lombok.AllArgsConstructor;

import com.silenteight.bridge.core.registration.domain.model.Alert.Status;

import java.util.Map;

@AllArgsConstructor
public class AlertStatusStatistics {

  private final Map<Status, Integer> statistics;

  public Integer getAlertCountByStatus(Status status) {
    return statistics.getOrDefault(status, 0);
  }
}
