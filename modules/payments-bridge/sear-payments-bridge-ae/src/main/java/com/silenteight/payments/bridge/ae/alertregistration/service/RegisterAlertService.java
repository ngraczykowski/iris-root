package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterMatchResponse;
import com.silenteight.payments.bridge.ae.alertregistration.port.AlertClientPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
@Slf4j
class RegisterAlertService implements RegisterAlertUseCase {

  private final AlertClientPort alertClient;

  @Override
  public RegisterAlertResponse register(RegisterAlertRequest registerAlertRequest) {
    var response = alertClient.createAlert(registerAlertRequest.toCreateAlertRequest());
    var alertName = response.getName();

    var matchesNames =
        alertClient.createMatches(registerAlertRequest.toCreateMatchesRequest(alertName));

    return createRegisterAlertResponse(
        registerAlertRequest.getAlertId(), alertName, matchesNames.getMatchesList());
  }

  private RegisterAlertResponse createRegisterAlertResponse(
      String alertId, String alertName, List<Match> matches) {
    return RegisterAlertResponse
        .builder()
        .alertId(alertId)
        .alertName(alertName)
        .matchResponses(matches
            .stream()
            .map(match -> RegisterMatchResponse
                .builder()
                .matchId(match.getMatchId())
                .matchName(match.getName())
                .build())
            .collect(toList()))
        .build();
  }

  public List<RegisterAlertResponse> batchRegistration(
      List<RegisterAlertRequest> registerAlertRequests) {
    if (CollectionUtils.isEmpty(registerAlertRequests)) {
      return List.of();
    }
    var batchCreateAlertsResponse = batchCreateAlerts(registerAlertRequests);
    return batchCreateMatches(registerAlertRequests, batchCreateAlertsResponse);
  }

  private BatchCreateAlertsResponse batchCreateAlerts(
      List<RegisterAlertRequest> registerAlertRequests) {
    var alerts = registerAlertRequests.stream()
        .map(RegisterAlertRequest::toAlert)
        .collect(toList());

    var request =
        BatchCreateAlertsRequest.newBuilder()
            .addAllAlerts(alerts).build();
    return alertClient.batchCreateAlerts(request);
  }

  private List<RegisterAlertResponse> batchCreateMatches(
      List<RegisterAlertRequest> registerAlertRequests,
      BatchCreateAlertsResponse batchCreateAlertsResponse) {
    var matches =
        createAlertMatchesRequests(registerAlertRequests, batchCreateAlertsResponse);

    var matchesRequest = BatchCreateMatchesRequest.newBuilder()
        .addAllAlertMatches(matches)
        .build();

    var response = alertClient.batchCreateMatches(matchesRequest);
    var groupedByAlertName = response.getMatchesList().stream().collect(groupingBy(match ->
        String.join("/", Arrays.copyOf(match.getName().split("/"), 2))
    ));

    var alertsMap = batchCreateAlertsResponse.getAlertsList().stream()
        .collect(toMap(Alert::getName, Function.identity()));

    return groupedByAlertName.entrySet().stream()
        .map(entry -> createRegisterAlertResponse(
            alertsMap.get(entry.getKey()).getAlertId(),
            entry.getKey(),
            entry.getValue()))
        .collect(toList());
  }

  private static boolean filter(Alert alert, Map<String, RegisterAlertRequest> alertRequestMap) {
    var found = alertRequestMap.containsKey(alert.getAlertId());
    if (!found) {
      log.warn("Alert with id: {} not found in the created batch response. "
          + "Skipping adding matches.", alert.getAlertId());
    }
    return found;
  }

  private List<BatchCreateAlertMatchesRequest> createAlertMatchesRequests(
      List<RegisterAlertRequest> registerAlertRequests,
      BatchCreateAlertsResponse batchCreateAlertsResponse) {
    var alertRequestsMap = registerAlertRequests.stream()
        .collect(toMap(RegisterAlertRequest::getAlertId, Function.identity()));

    return batchCreateAlertsResponse.getAlertsList().stream()
        .filter(alert -> filter(alert, alertRequestsMap))
        .map(alert -> alertRequestsMap.get(alert.getAlertId())
            .toCreateMatchesRequest(alert.getName()))
        .collect(toList());
  }

}
