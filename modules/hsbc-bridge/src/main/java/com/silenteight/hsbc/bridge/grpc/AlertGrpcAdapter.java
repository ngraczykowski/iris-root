package com.silenteight.hsbc.bridge.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesRequest;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsRequest;
import com.silenteight.adjudication.api.v1.Match;
import com.silenteight.hsbc.bridge.alert.AlertServiceClient;
import com.silenteight.hsbc.bridge.alert.dto.*;

import io.grpc.StatusRuntimeException;
import org.springframework.retry.annotation.Retryable;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class AlertGrpcAdapter implements AlertServiceClient {

  private final AlertServiceBlockingStub alertServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  @Retryable(value = StatusRuntimeException.class)
  public BatchCreateAlertsResponseDto batchCreateAlerts(List<Alert> alerts) {
    log.info("batchCreateAlerts alerts={}", alerts.size());

    var grpcRequest = BatchCreateAlertsRequest.newBuilder()
        .addAllAlerts(alerts)
        .build();

    var response = getStub().batchCreateAlerts(grpcRequest);
    var registeredAlerts = response.getAlertsList();

    //TODO remove it once root cause of the issue is found
    log.info(
        "registered alert names={}",
        registeredAlerts.stream().map(Alert::getName).collect(Collectors.toList()));

    return BatchCreateAlertsResponseDto.builder()
        .alerts(mapAlerts(registeredAlerts))
        .build();
  }

  @Override
  @Retryable(value = StatusRuntimeException.class)
  public BatchCreateAlertMatchesResponseDto batchCreateAlertMatches(
      BatchCreateAlertMatchesRequestDto request) {
    var matches = request.getMatchIds().stream()
        .map(m -> Match.newBuilder().setMatchId(m).build())
        .collect(toList());
    var gprcRequest = BatchCreateAlertMatchesRequest.newBuilder()
        .setAlert(request.getAlert())
        .addAllMatches(matches)
        .build();

    var response = getStub().batchCreateAlertMatches(gprcRequest);
    var registeredMatches = response.getMatchesList();

    //TODO remove it once root cause of the issue is found
    log.info("Create matches for alert={}", request.getAlert());
    log.info(
        "Registered matches names={}",
        registeredMatches.stream().map(Match::getName).collect(Collectors.toList()));

    return BatchCreateAlertMatchesResponseDto.builder()
        .alertMatches(mapAlertMatches(registeredMatches))
        .build();
  }

  private List<AlertMatchDto> mapAlertMatches(List<Match> matchesList) {
    return matchesList.stream()
        .map(m -> AlertMatchDto.builder()
            .matchId(m.getMatchId())
            .name(m.getName())
            .build())
        .collect(toList());
  }

  private List<AlertDto> mapAlerts(List<Alert> alertsList) {
    return alertsList.stream()
        .map(a -> AlertDto.builder()
            .alertId(a.getAlertId())
            .name(a.getName())
            .build())
        .collect(toList());
  }

  private AlertServiceBlockingStub getStub() {
    return alertServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, SECONDS);
  }
}
