package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.alert.AlertInfo;
import com.silenteight.hsbc.bridge.domain.AlertMatchIdComposite;
import com.silenteight.hsbc.bridge.match.MatchComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;

import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class AlertService {

  private final AlertServiceBlockingStub alertServiceBlockingStub;
  private final AlertFacade alertFacade;
  private final MatchFacade matchFacade;

  List<String> createBatchAlertsWithMatches(
      Collection<AlertMatchIdComposite> alertMatchIdComposites) {

    Map<Long, Collection<Long>> alertIdsToMatches = alertMatchIdComposites
        .stream()
        .collect(Collectors.toMap(
            AlertMatchIdComposite::getAlertId,
            AlertMatchIdComposite::getMatchIds));

    List<AlertInfo> alertInfos = alertFacade.getAlerts(alertIdsToMatches.keySet());

    BatchCreateAlertsRequest batchCreateAlertsRequest =
        prepareBatchCreateAlertsRequest(alertInfos);

    BatchCreateAlertsResponse batchCreateAlertsResponse =
        alertServiceBlockingStub.batchCreateAlerts(batchCreateAlertsRequest);

    registerMatches(alertIdsToMatches, batchCreateAlertsResponse);

    return batchCreateAlertsResponse
        .getAlertsList()
        .stream()
        .map(Alert::getName)
        .collect(Collectors.toList());

  }

  private void registerMatches(
      Map<Long, Collection<Long>> alertIdsToMatches,
      BatchCreateAlertsResponse batchCreateAlertsResponse) {
    batchCreateAlertsResponse.getAlertsList().stream().map(a -> {
      String alertId = a.getAlertId();
      List<Match> matches = alertIdsToMatches.get(Long.valueOf(alertId)).stream().map(m -> {
        MatchComposite match = matchFacade.getMatch(m);

        return Match.newBuilder()
            .setName(match.getId().toString())
            .setMatchId(alertId)
            .setCreateTime(currentTime())
            .build();
      }).collect(Collectors.toList());

      return BatchCreateAlertMatchesRequest
          .newBuilder()
          .setAlert(alertId)
          .addAllMatches(matches)
          .build();

    }).map(alertServiceBlockingStub::batchCreateAlertMatches);
  }

  private BatchCreateAlertsRequest prepareBatchCreateAlertsRequest(List<AlertInfo> alertInfos) {
    List<Alert> alerts = alertInfos.stream().map(x ->
        Alert.newBuilder()
            .setAlertId(x.getId().toString())
            .setName(String.valueOf(x.getCaseId()))
            .setAlertTime(currentTime())
            .build()).collect(Collectors.toList());

    return BatchCreateAlertsRequest.newBuilder().addAllAlerts(alerts).build();
  }

  Timestamp currentTime() {
    return Timestamp
        .newBuilder()
        .setSeconds(Instant.now().getEpochSecond())
        .build();
  }

}
