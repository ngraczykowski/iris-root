package com.silenteight.adjudication.api.library.v1.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.AdjudicationEngineLibraryRuntimeException;
import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;

import io.grpc.StatusRuntimeException;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class AlertGrpcAdapter implements AlertServiceClient {

  private final AlertServiceBlockingStub alertServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  public BatchCreateAlertsOut batchCreateAlerts(List<AlertIn> alerts) {
    log.info("batchCreateAlerts alerts={}", alerts.size());

    var grpcRequest = BatchCreateAlertsRequest.newBuilder()
        .addAllAlerts(alerts.stream().map(alert -> Alert.newBuilder()
                .setAlertId(alert.getAlertId())
                .build())
            .collect(Collectors.toList()))
        .build();

    try {
      var response = getStub().batchCreateAlerts(grpcRequest);

      return BatchCreateAlertsOut.builder()
          .alerts(mapAlerts(response.getAlertsList()))
          .build();
    } catch (StatusRuntimeException e) {
      log.error("Cannot batch create alerts", e);
      throw new AdjudicationEngineLibraryRuntimeException("Cannot batch create alerts", e);
    }
  }

  @Override
  public BatchCreateAlertMatchesOut batchCreateAlertMatches(BatchCreateAlertMatchesIn request) {
    var matches = request.getMatchIds().stream()
        .map(m -> Match.newBuilder().setMatchId(m).build())
        .collect(Collectors.toList());
    var grpcRequest = BatchCreateAlertMatchesRequest.newBuilder()
        .setAlert(request.getAlertId())
        .addAllMatches(matches)
        .build();

    try {
      var response = getStub().batchCreateAlertMatches(grpcRequest);

      return BatchCreateAlertMatchesOut.builder()
          .alertMatches(mapAlertMatches(response.getMatchesList()))
          .build();
    } catch (StatusRuntimeException e) {
      log.error("Cannot batch create alert matches", e);
      throw new AdjudicationEngineLibraryRuntimeException("Cannot batch create alert matches", e);
    }
  }

  @Override
  public RegisterAlertsAndMatchesOut registerAlertsAndMatches(RegisterAlertsAndMatchesIn command) {
    var registerAlertsRequest = BatchCreateAlertsRequest.newBuilder()
        .addAllAlerts(command.getAlertsWithMatches().stream()
            .map(alert -> Alert.newBuilder()
                .setAlertId(alert.getAlertId())
                .build())
            .collect(Collectors.toList()))
        .build();

    var registeredAlerts =
        registerAlerts(registerAlertsRequest).getAlertsList().stream()
            .collect(Collectors.toMap(Alert::getAlertId, Alert::getName));

    return RegisterAlertsAndMatchesOut.builder()
        .alertWithMatches(command.getAlertsWithMatches().stream()
            .map(alert -> mapAlertWithMatches(alert, registeredAlerts.get(alert.getAlertId())))
            .collect(Collectors.toList()))
        .build();
  }

  private BatchCreateAlertsResponse registerAlerts(BatchCreateAlertsRequest grpcRequest) {
    try {
      return getStub().batchCreateAlerts(grpcRequest);
    } catch (StatusRuntimeException e) {
      log.error("Cannot batch create alert matches", e);
      throw new AdjudicationEngineLibraryRuntimeException("Cannot batch create alert matches", e);
    }
  }

  private AlertWithMatchesOut mapAlertWithMatches(
      BatchCreateAlertMatchesIn alert, String alertName) {
    return AlertWithMatchesOut.builder()
        .alertId(alert.getAlertId())
        .alertName(alertName)
        .matches(getMatches(alert))
        .build();
  }

  private List<AlertMatchOut> getMatches(BatchCreateAlertMatchesIn alertWithMatches) {
    var matches = alertWithMatches.getMatchIds().stream()
        .map(m -> Match.newBuilder().setMatchId(m).build())
        .collect(Collectors.toList());

    var grpcRequest = BatchCreateAlertMatchesRequest.newBuilder()
        .setAlert(alertWithMatches.getAlertId())
        .addAllMatches(matches)
        .build();

    var response = registerAlertMatches(grpcRequest);
    return mapAlertMatches(response.getMatchesList());
  }

  private BatchCreateAlertMatchesResponse registerAlertMatches(
      BatchCreateAlertMatchesRequest grpcRequest) {
    try {
      return getStub().batchCreateAlertMatches(grpcRequest);
    } catch (StatusRuntimeException e) {
      log.error("Cannot batch create alert matches", e);
      throw new AdjudicationEngineLibraryRuntimeException("Cannot batch create alert matches", e);
    }
  }

  private List<AlertMatchOut> mapAlertMatches(List<Match> matchesList) {
    return matchesList.stream()
        .map(m -> AlertMatchOut.builder()
            .matchId(m.getMatchId())
            .name(m.getName())
            .build())
        .collect(Collectors.toList());
  }

  private List<AlertOut> mapAlerts(List<Alert> alertsList) {
    return alertsList.stream()
        .map(a -> AlertOut.builder()
            .alertId(a.getAlertId())
            .name(a.getName())
            .build())
        .collect(Collectors.toList());
  }

  private AlertServiceBlockingStub getStub() {
    return alertServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
