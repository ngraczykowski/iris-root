package com.silenteight.adjudication.engine.alerts.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsRequest;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsResponse;
import com.silenteight.adjudication.api.v1.CreateAlertRequest;
import com.silenteight.adjudication.engine.alerts.alert.AlertFacade;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
class AlertService {

  @NonNull
  private final AlertFacade alertFacade;

  Alert createAlert(CreateAlertRequest request) {
    return alertFacade.createAlerts(List.of(request.getAlert())).get(0);
  }

  BatchCreateAlertsResponse batchCreateAlerts(BatchCreateAlertsRequest request) {
    var alerts = alertFacade.createAlerts(request.getAlertsList());
    return BatchCreateAlertsResponse.newBuilder().addAllAlerts(alerts).build();
  }
}
