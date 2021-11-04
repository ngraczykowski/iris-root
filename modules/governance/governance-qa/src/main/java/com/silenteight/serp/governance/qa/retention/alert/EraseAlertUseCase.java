package com.silenteight.serp.governance.qa.retention.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.qa.manage.domain.AlertQuery;
import com.silenteight.serp.governance.qa.manage.domain.AlertService;
import com.silenteight.serp.governance.qa.manage.domain.dto.EraseAlertRequest;

import java.util.List;

import static java.time.OffsetDateTime.now;
import static org.apache.commons.collections4.ListUtils.partition;

@RequiredArgsConstructor
@Slf4j
class EraseAlertUseCase {

  static final String PRINCIPAL_NAME = "governance-app";

  @NonNull
  private final AlertQuery alertQuery;
  @NonNull
  private final AlertService alertService;
  private final int batchSize;

  public void activate(List<String> alerts) {
    log.info("Looking up for alerts counts {}.", alerts.size());
    partition(alerts, batchSize)
        .stream()
        .map(alertQuery::findIdsForDiscriminators)
        .forEach(this::eraseAlerts);
  }

  private void eraseAlerts(List<Long> alertIds) {
    alertIds.forEach(alertId ->
        alertService.eraseAlert(toEraseAlertRequest(alertId)));
  }

  private static EraseAlertRequest toEraseAlertRequest(long alertId) {
    return EraseAlertRequest.of(alertId, PRINCIPAL_NAME, now());
  }
}
