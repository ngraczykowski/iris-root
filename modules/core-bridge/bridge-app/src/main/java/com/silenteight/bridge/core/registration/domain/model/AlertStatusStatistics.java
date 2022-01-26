package com.silenteight.bridge.core.registration.domain.model;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class AlertStatusStatistics {

  private final Map<AlertStatus, Integer> statistics;

  public Integer getAlertCountByStatus(AlertStatus status) {
    return statistics.getOrDefault(status, 0);
  }
}
