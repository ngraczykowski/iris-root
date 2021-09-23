package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesRequest;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsRequest;
import com.silenteight.adjudication.api.v1.BatchCreateMatchesRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.AlertClientPort;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class CreateAlertsService {

  private final AlertClientPort alertClient;

  public List<String> createAlert(List<RegisterAlertRequest> registerAlertRequest) {
    var response = alertClient.createAlert(BatchCreateAlertsRequest
        .newBuilder()
        .addAllAlerts(registerAlertRequest
            .stream()
            .map(RegisterAlertRequest::toCreateAlertRequest)
            .collect(
                toList()))
        .build());
    var alertNames = response.getAlertsList().stream().map(Alert::getName).collect(toList());

    alertClient.createMatches(createMatchesRequest(alertNames, registerAlertRequest));
    return alertNames;
  }

  private static BatchCreateMatchesRequest createMatchesRequest(
      List<String> alertNames, List<RegisterAlertRequest> registerAlertRequest) {
    var matchRequests = new ArrayList<BatchCreateAlertMatchesRequest>();

    for (int i = 0; i < alertNames.size(); i++) {
      matchRequests.add(registerAlertRequest.get(i).toCreateMatchesRequest(alertNames.get(0)));
    }

    return BatchCreateMatchesRequest.newBuilder().addAllAlertMatches(matchRequests).build();
  }
}
