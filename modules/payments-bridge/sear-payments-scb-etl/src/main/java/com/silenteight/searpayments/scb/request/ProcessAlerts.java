package com.silenteight.searpayments.scb.request;

import com.silenteight.searpayments.scb.domain.Alert;
import com.silenteight.searpayments.scb.domain.AlertService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
class ProcessAlerts {

  @NonNull private final AlertService alertService;
  @NonNull private final PrevalidateAlertStrategy prevalidateAlertStrategy;

  @Transactional
  void invoke(List<Alert> alerts) {
    alerts.forEach(this::processAlert);
  }

  private void processAlert(Alert alert) {
    var processAlert = new ProcessAlert(alert, alertService, prevalidateAlertStrategy);
    processAlert.invoke();
  }
}
