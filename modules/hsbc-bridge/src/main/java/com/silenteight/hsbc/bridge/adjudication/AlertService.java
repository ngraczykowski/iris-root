package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.hsbc.bridge.alert.event.UpdateAlertWithNameEvent;
import com.silenteight.hsbc.bridge.domain.AlertMatchIdComposite;
import com.silenteight.hsbc.bridge.match.event.UpdateMatchWithNameEvent;

import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class AlertService {

  private final AlertServiceBlockingStub alertServiceBlockingStub;
  private final ApplicationEventPublisher eventPublisher;

  List<String> createBatchAlertsWithMatches(
      Collection<AlertMatchIdComposite> alertMatchIdComposites) {

    var alertIdsToMatches = convertToMap(alertMatchIdComposites);
    var batchCreateAlertsResponse = registerAlerts(alertMatchIdComposites);
    registerMatches(alertIdsToMatches, batchCreateAlertsResponse);

    publishUpdateAlertWithNameEvent(batchCreateAlertsResponse);

    return batchCreateAlertsResponse
        .getAlertsList()
        .stream()
        .map(Alert::getName)
        .collect(Collectors.toList());
  }


  private BatchCreateAlertsResponse registerAlerts(
      Collection<AlertMatchIdComposite> alertMatchIdComposites) {
    var alerts = alertMatchIdComposites.stream().map(a ->
        Alert.newBuilder()
            .setAlertId(String.valueOf(a.getAlertId()))
            .setName(String.valueOf(a.getCaseId()))
            .build())
        .collect(Collectors.toList());

    var batchCreateAlertsRequest =
        BatchCreateAlertsRequest.newBuilder().addAllAlerts(alerts).build();

    return alertServiceBlockingStub.batchCreateAlerts(batchCreateAlertsRequest);
  }

  private void registerMatches(
      Map<Long, AlertMatchIdComposite> alertIdsToMatches,
      BatchCreateAlertsResponse batchCreateAlertsResponse) {

    var result = batchCreateAlertsResponse.getAlertsList().stream().map(a -> {
      String alertId = a.getAlertId();

      var matches = alertIdsToMatches
          .get(Long.valueOf(alertId))
          .getMatchIds()
          .stream()
          .map(m -> Match.newBuilder()
              .setName(m.toString())
              .setMatchId(alertId)
              .build())
          .collect(Collectors.toList());

      return BatchCreateAlertMatchesRequest
          .newBuilder()
          .setAlert(alertId)
          .addAllMatches(matches)
          .build();

    }).map(alertServiceBlockingStub::batchCreateAlertMatches)
        .map(BatchCreateAlertMatchesResponse::getMatchesList)
        .flatMap(List::stream)
        .collect(Collectors.toList());

    publishUpdateMatchWithNameEvent(result);
  }

  private void publishUpdateAlertWithNameEvent(
      BatchCreateAlertsResponse batchCreateAlertsResponse) {
    var updateAlertWithNameEvent =
        new UpdateAlertWithNameEvent(batchCreateAlertsResponse
            .getAlertsList()
            .stream()
            .collect(Collectors.toMap(Alert::getAlertId, Alert::getName)));

    eventPublisher.publishEvent(updateAlertWithNameEvent);

  }

  private void publishUpdateMatchWithNameEvent(List<Match> matches) {
    var updateMatchWithNameEvent = new UpdateMatchWithNameEvent(
        matches
            .stream()
            .map(Match::getName)
            .collect(Collectors.toList()));

    eventPublisher.publishEvent(updateMatchWithNameEvent);
  }

  private Map<Long, AlertMatchIdComposite> convertToMap(
      Collection<AlertMatchIdComposite> alertMatchIdComposites) {
    return alertMatchIdComposites
        .stream()
        .collect(Collectors.toMap(
            AlertMatchIdComposite::getAlertId, Function.identity()
        ));
  }

}
