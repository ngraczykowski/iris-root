package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.payments.bridge.ae.alertregistration.domain.*;
import com.silenteight.payments.bridge.ae.alertregistration.port.AlertClientPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisteredAlertDataAccessPort;
import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.model.AlertData;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.common.protobuf.TimestampConverter.fromOffsetDateTime;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
@Slf4j
class RegisterAlertService implements RegisterAlertUseCase {

  private final AlertClientPort alertClient;
  private final RegisteredAlertDataAccessPort registeredAlertDataAccessPort;

  @Override
  public RegisterAlertResponse register(AlertData alertData, AlertMessageDto alertDto) {
    var request = createRequest(alertData, alertDto);
    var response = alertClient.createAlert(request.toCreateAlertRequest());
    var alertName = response.getName();

    var matchesNames =
        alertClient.createMatches(request.toCreateMatchesRequest(alertName));

    var registerAlertResponse = createRegisterAlertResponse(
        request.getAlertId(), alertName, matchesNames.getMatchesList());

    registeredAlertDataAccessPort.save(SaveRegisteredAlertRequest
        .builder()
        .alertId(alertData.getAlertId())
        .alertName(registerAlertResponse.getAlertName())
        .matchNames(registerAlertResponse
            .getMatchResponses()
            .stream()
            .map(RegisterMatchResponse::getMatchName)
            .collect(toList()))
        .build());

    return registerAlertResponse;
  }

  private RegisterAlertRequest createRequest(AlertData alertData, AlertMessageDto alertDto) {

    var matchIds = getMatchIds(alertDto);
    return RegisterAlertRequest.builder()
        .alertId(alertDto.getSystemID())
        .alertTime(fromOffsetDateTime(alertDto.getFilteredAt(ZoneOffset.UTC)))
        .priority(alertData.getPriority())
        .matchIds(matchIds)
        .label(Label.of("source", "CMAPI"))
        .label(Label.of("alertMessageId", alertData.getAlertId().toString()))
        .build();
  }

  @Nonnull
  private static List<String> getMatchIds(AlertMessageDto alertDto) {
    var hits = alertDto.getHits();

    // XXX(ahaczewski): WATCH OUT! AlertParserService#createAlertEtlResponse() assumes the same
    //  iteration order!!! Make sure you keep it in sync, until shit gets cleaned!!!
    return IntStream.range(0, hits.size())
        .<Optional<String>>mapToObj(idx -> {
          var hit = hits.get(idx).getHit();
          return hit.isBlocking() ? Optional.of(hit.getMatchId(idx)) : Optional.empty();
        })
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
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

  @Override
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
