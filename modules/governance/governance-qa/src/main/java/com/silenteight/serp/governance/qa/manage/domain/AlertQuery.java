package com.silenteight.serp.governance.qa.manage.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AlertQuery {

  @NonNull
  private final AlertRepository alertRepository;

  public List<Long> findIdsForAlertsNames(List<String> alertNames) {
    return alertRepository.findIdByAlertNameIn(alertNames);
  }
}
