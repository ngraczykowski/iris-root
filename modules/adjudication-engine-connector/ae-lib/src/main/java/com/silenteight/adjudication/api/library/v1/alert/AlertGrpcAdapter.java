package com.silenteight.adjudication.api.library.v1.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.AdjudicationEngineLibraryRuntimeException;
import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesRequest;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesResponse;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsRequest;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsResponse;

import io.vavr.control.Try;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class AlertGrpcAdapter implements AlertServiceClient {

  private static final String CANNOT_CREATE_BATCH_ALERTS = "Cannot create batch alerts";
  private static final String CANNOT_CREATE_BATCH_ALERT_MATCHES =
      "Cannot create batch alert matches";
  private final AlertServiceBlockingStub alertServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  public BatchCreateAlertsOut batchCreateAlerts(List<AlertIn> alerts) {
    log.info("batchCreateAlerts alerts={}", alerts.size());

    var grpcRequest = BatchCreateAlertsRequest.newBuilder()
        .addAllAlerts(alerts.stream()
            .map(alert -> AlertGrpcMapper.mapToAlert(alert.getAlertId()))
            .collect(Collectors.toList()))
        .build();

    return Try.of(() -> getStub().batchCreateAlerts(grpcRequest))
        .map(AlertGrpcMapper::mapToBatchCreateAlertsOut)
        .onFailure(e -> log.error(CANNOT_CREATE_BATCH_ALERTS, e))
        .onSuccess(result -> log.debug("Batch created alerts successfully"))
        .getOrElseThrow(
            e -> new AdjudicationEngineLibraryRuntimeException(CANNOT_CREATE_BATCH_ALERTS, e));

  }

  @Override
  public BatchCreateAlertMatchesOut batchCreateAlertMatches(BatchCreateAlertMatchesIn request) {
    var matches = request.getMatchIds().stream()
        .map(AlertGrpcMapper::mapToMatch)
        .collect(Collectors.toList());
    var grpcRequest = BatchCreateAlertMatchesRequest.newBuilder()
        .setAlert(request.getAlertId())
        .addAllMatches(matches)
        .build();

    return Try.of(() -> getStub().batchCreateAlertMatches(grpcRequest))
        .map(AlertGrpcMapper::mapToBatchCreateAlertMatchesOut)
        .onFailure(e -> log.error(CANNOT_CREATE_BATCH_ALERT_MATCHES, e))
        .onSuccess(result -> log.debug("Batch created alert matches successfully"))
        .getOrElseThrow(
            e -> new AdjudicationEngineLibraryRuntimeException(
                CANNOT_CREATE_BATCH_ALERT_MATCHES, e));
  }

  @Override
  public RegisterAlertsAndMatchesOut registerAlertsAndMatches(RegisterAlertsAndMatchesIn command) {
    var registerAlertsRequest = BatchCreateAlertsRequest.newBuilder()
        .addAllAlerts(command.getAlertsWithMatches().stream()
            .map(alert -> AlertGrpcMapper.mapToAlert(alert.getAlertId()))
            .collect(Collectors.toList()))
        .build();

    var registeredAlerts =
        registerAlerts(registerAlertsRequest).getAlertsList().stream()
            .collect(Collectors.toMap(Alert::getAlertId, Alert::getName));

    return RegisterAlertsAndMatchesOut.builder()
        .alertWithMatches(command.getAlertsWithMatches().stream()
            .map(alert -> mergeAlertWithMatches(registeredAlerts, alert))
            .collect(Collectors.toList()))
        .build();
  }

  private AlertWithMatchesOut mergeAlertWithMatches(
      Map<String, String> registeredAlerts, BatchCreateAlertMatchesIn alert) {
    return AlertGrpcMapper.mapToAlertWithMatches(
        alert, registeredAlerts.get(alert.getAlertId()), getMatches(alert));
  }

  private BatchCreateAlertsResponse registerAlerts(BatchCreateAlertsRequest grpcRequest) {
    return Try.of(() -> getStub().batchCreateAlerts(grpcRequest))
        .onFailure(e -> log.error(CANNOT_CREATE_BATCH_ALERTS, e))
        .onSuccess(result -> log.debug("Batch created alerts successfully"))
        .getOrElseThrow(e -> new AdjudicationEngineLibraryRuntimeException(
            CANNOT_CREATE_BATCH_ALERT_MATCHES, e));
  }

  private List<AlertMatchOut> getMatches(BatchCreateAlertMatchesIn alertWithMatches) {
    var matches = alertWithMatches.getMatchIds().stream()
        .map(AlertGrpcMapper::mapToMatch)
        .collect(Collectors.toList());

    var grpcRequest = BatchCreateAlertMatchesRequest.newBuilder()
        .setAlert(alertWithMatches.getAlertId())
        .addAllMatches(matches)
        .build();

    var response = registerAlertMatches(grpcRequest);
    return AlertGrpcMapper.mapToAlertMatches(response.getMatchesList());
  }

  private BatchCreateAlertMatchesResponse registerAlertMatches(
      BatchCreateAlertMatchesRequest grpcRequest) {
    return Try.of(() -> getStub().batchCreateAlertMatches(grpcRequest))
        .onFailure(e -> log.error(CANNOT_CREATE_BATCH_ALERT_MATCHES, e))
        .onSuccess(result -> log.debug("Batch created alert matches successfully"))
        .getOrElseThrow(e -> new AdjudicationEngineLibraryRuntimeException(
            CANNOT_CREATE_BATCH_ALERT_MATCHES, e));
  }

  private AlertServiceBlockingStub getStub() {
    return alertServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}