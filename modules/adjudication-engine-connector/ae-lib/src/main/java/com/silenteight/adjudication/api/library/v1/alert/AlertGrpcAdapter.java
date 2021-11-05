package com.silenteight.adjudication.api.library.v1.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.AdjudicationEngineLibraryRuntimeException;
import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesRequest;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsRequest;
import com.silenteight.adjudication.api.v1.Match;

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
                .setName(alert.getName())
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
    var gprcRequest = BatchCreateAlertMatchesRequest.newBuilder()
        .setAlert(request.getAlert())
        .addAllMatches(matches)
        .build();

    try {
      var response = getStub().batchCreateAlertMatches(gprcRequest);

      return BatchCreateAlertMatchesOut.builder()
          .alertMatches(mapAlertMatches(response.getMatchesList()))
          .build();
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
