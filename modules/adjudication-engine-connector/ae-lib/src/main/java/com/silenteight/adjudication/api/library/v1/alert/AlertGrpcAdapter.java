package com.silenteight.adjudication.api.library.v1.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.AdjudicationEngineLibraryRuntimeException;
import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesRequest;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsRequest;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsResponse;

import io.vavr.control.Try;

import java.util.Collection;
import java.util.List;
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
            .map(alert -> AlertGrpcMapper.toAlert(alert.getAlertId(), alert.getAlertPriority()))
            .collect(Collectors.toList()))
        .build();

    return Try.of(() -> getStub().batchCreateAlerts(grpcRequest))
        .map(AlertGrpcMapper::toBatchCreateAlertsOut)
        .onFailure(e -> log.error(CANNOT_CREATE_BATCH_ALERTS, e))
        .onSuccess(result -> log.debug("Batch created alerts successfully"))
        .getOrElseThrow(
            e -> new AdjudicationEngineLibraryRuntimeException(CANNOT_CREATE_BATCH_ALERTS, e));
  }

  @Override
  public BatchCreateAlertMatchesOut batchCreateAlertMatches(BatchCreateAlertMatchesIn request) {
    var grpcRequest = BatchCreateAlertMatchesRequest.newBuilder()
        .setAlert(request.getAlertName())
        .addAllMatches(AlertGrpcMapper.toMatches(request.getMatchIds()))
        .build();

    return Try.of(() -> getStub().batchCreateAlertMatches(grpcRequest))
        .map(AlertGrpcMapper::toBatchCreateAlertMatchesOut)
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
            .map(alert -> AlertGrpcMapper.toAlert(alert.getAlertId(), alert.getAlertPriority()))
            .collect(Collectors.toList()))
        .build();

    var registeredAlerts =
        registerAlerts(registerAlertsRequest).getAlertsList().stream()
            .collect(Collectors.toMap(Alert::getAlertId, Alert::getName));

    var alertsWithMatches = command.getAlertsWithMatches().stream()
        .map(alert -> {
          var alertId = alert.getAlertId();
          var alertName = registeredAlerts.get(alertId);
          var registeredMatches =
              registerAlertMatches(alert.getMatchIds(), alertName);

          return AlertWithMatchesOut.builder()
              .alertId(alertId)
              .alertName(alertName)
              .matches(registeredMatches)
              .build();
        })
        .collect(Collectors.toList());

    return RegisterAlertsAndMatchesOut.builder()
        .alertWithMatches(alertsWithMatches)
        .build();
  }

  private BatchCreateAlertsResponse registerAlerts(BatchCreateAlertsRequest grpcRequest) {
    return Try.of(() -> getStub().batchCreateAlerts(grpcRequest))
        .onFailure(e -> log.error(CANNOT_CREATE_BATCH_ALERTS, e))
        .onSuccess(result -> log.debug("Batch created alerts successfully"))
        .getOrElseThrow(e -> new AdjudicationEngineLibraryRuntimeException(
            CANNOT_CREATE_BATCH_ALERT_MATCHES, e));
  }

  private List<AlertMatchOut> registerAlertMatches(Collection<String> matchIds, String alertName) {
    var grpcRequest = BatchCreateAlertMatchesRequest.newBuilder()
        .setAlert(alertName)
        .addAllMatches(AlertGrpcMapper.toMatches(matchIds))
        .build();

    return Try.of(() -> getStub().batchCreateAlertMatches(grpcRequest))
        .map(AlertGrpcMapper::toAlertMatchesOut)
        .onFailure(e -> log.error(CANNOT_CREATE_BATCH_ALERT_MATCHES, e))
        .onSuccess(result -> log.debug("Batch created alert matches successfully"))
        .getOrElseThrow(e -> new AdjudicationEngineLibraryRuntimeException(
            CANNOT_CREATE_BATCH_ALERT_MATCHES, e));
  }

  private AlertServiceBlockingStub getStub() {
    return alertServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}