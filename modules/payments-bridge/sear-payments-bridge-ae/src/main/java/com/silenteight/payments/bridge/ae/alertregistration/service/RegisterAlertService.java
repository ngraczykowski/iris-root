package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterMatchResponse;
import com.silenteight.payments.bridge.ae.alertregistration.domain.SaveRegisteredAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.AlertClientPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisteredAlertDataAccessPort;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.context.ApplicationEventPublisher;
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
  private final RegisteredAlertDataAccessPort registeredAlertDataAccessPort;
  private final ApplicationEventPublisher applicationEventPublisher;

  private static RegisterAlertResponse createRegisterAlertResponse(
      String systemId, String alertName, List<Match> matches) {
    return RegisterAlertResponse
        .builder()
        .systemId(systemId)
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

  private static boolean filter(Alert alert, Map<String, RegisterAlertRequest> alertRequestMap) {
    var found = alertRequestMap.containsKey(alert.getAlertId());
    if (!found) {
      log.warn("Alert with id: {} not found in the created batch response. "
          + "Skipping adding matches.", alert.getAlertId());
    }
    return found;
  }

  private static List<BatchCreateAlertMatchesRequest> createAlertMatchesRequests(
      List<RegisterAlertRequest> registerAlertRequests,
      BatchCreateAlertsResponse batchCreateAlertsResponse) {
    var alertRequestsMap = registerAlertRequests.stream()
        .collect(toMap(RegisterAlertRequest::getFkcoSystemId, Function.identity()));

    return batchCreateAlertsResponse.getAlertsList().stream()
        .filter(alert -> filter(alert, alertRequestsMap))
        .map(alert -> alertRequestsMap.get(alert.getAlertId())
            .toCreateMatchesRequest(alert.getName()))
        .collect(toList());
  }

  @Override
  @Timed
  public RegisterAlertResponse register(RegisterAlertRequest request) {
    var response = alertClient.createAlert(request.toCreateAlertRequest());
    var alertName = response.getName();

    var matchesNames =
        alertClient.createMatches(request.toCreateMatchesRequest(alertName));

    var registerAlertResponse = createRegisterAlertResponse(
        request.getFkcoSystemId(), alertName, matchesNames.getMatchesList());
    applicationEventPublisher.publishEvent(registerAlertResponse);
    registeredAlertDataAccessPort.save(List.of(SaveRegisteredAlertRequest
        .builder()
        .alertMessageId(request.getAlertMessageId())
        .alertName(registerAlertResponse.getAlertName())
        .fkcoSystemId(request.getFkcoSystemId())
        .matches(registerAlertResponse
            .getMatchResponses()
            .stream()
            .map(RegisterMatchResponse::toSaveRegisteredMatchRequest)
            .collect(toList()))
        .build()));

    return registerAlertResponse;
  }

  @Override
  public List<RegisterAlertResponse> batchRegistration(
      List<RegisterAlertRequest> registerAlertRequests) {
    if (CollectionUtils.isEmpty(registerAlertRequests)) {
      return List.of();
    }
    var batchCreateAlertsResponse = batchCreateAlerts(registerAlertRequests);
    var registeredAlertMatches =
        batchCreateMatches(registerAlertRequests, batchCreateAlertsResponse);
    var saveRequest = registeredAlertMatches
        .stream()
        .map(am -> am.toSaveRegisterAlertRequest(registerAlertRequests))
        .collect(toList());

    registeredAlertDataAccessPort.save(saveRequest);

    return registeredAlertMatches;
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

}
