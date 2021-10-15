package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterMatchResponse;
import com.silenteight.payments.bridge.ae.alertregistration.port.AlertClientPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertService;

import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class RegisterAlertsService implements RegisterAlertService {

  private final AlertClientPort alertClient;

  @Override
  public RegisterAlertResponse register(RegisterAlertRequest registerAlertRequest) {
    var response = alertClient.createAlert(registerAlertRequest.toCreateAlertRequest());
    var alertName = response.getName();

    var matchesNames =
        alertClient.createMatches(registerAlertRequest.toCreateMatchesRequest(alertName));

    return RegisterAlertResponse
        .builder()
        .alertId(registerAlertRequest.getAlertId())
        .alertName(alertName)
        .matchResponses(matchesNames
            .getMatchesList()
            .stream()
            .map(m -> RegisterMatchResponse
                .builder()
                .matchId(m.getMatchId())
                .matchName(m.getName())
                .build())
            .collect(toList()))
        .build();
  }
}
