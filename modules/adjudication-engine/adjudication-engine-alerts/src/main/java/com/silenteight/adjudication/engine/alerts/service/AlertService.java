package com.silenteight.adjudication.engine.alerts.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.engine.alerts.alert.AlertFacade;
import com.silenteight.adjudication.engine.alerts.match.MatchFacade;
import com.silenteight.adjudication.engine.alerts.match.NewAlertMatches;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

@RequiredArgsConstructor
@Service
class AlertService {

  @NonNull
  private final AlertFacade alertFacade;
  @NonNull
  private final MatchFacade matchFacade;

  Alert createAlert(CreateAlertRequest request) {
    return alertFacade.createAlerts(List.of(request.getAlert())).get(0);
  }

  BatchCreateAlertsResponse batchCreateAlerts(BatchCreateAlertsRequest request) {
    var alerts = alertFacade.createAlerts(request.getAlertsList());
    return BatchCreateAlertsResponse.newBuilder().addAllAlerts(alerts).build();
  }

  Match createMatch(CreateMatchRequest request) {
    var newAlertMatches = NewAlertMatches.builder()
        .parentAlert(request.getAlert())
        .match(request.getMatch())
        .build();

    return matchFacade.createMatches(List.of(newAlertMatches)).get(0);
  }

  BatchCreateAlertMatchesResponse batchCreateAlertMatches(BatchCreateAlertMatchesRequest request) {
    var newAlertMatches = NewAlertMatches.builder()
        .parentAlert(request.getAlert())
        .matches(request.getMatchesList())
        .build();

    var matches = matchFacade.createMatches(List.of(newAlertMatches));

    return BatchCreateAlertMatchesResponse.newBuilder()
        .addAllMatches(matches)
        .build();
  }

  BatchCreateMatchesResponse batchCreateMatches(BatchCreateMatchesRequest request) {
    var newAlertMatchesList = request
        .getAlertMatchesList()
        .stream()
        .map(r -> NewAlertMatches
            .builder()
            .parentAlert(r.getAlert())
            .matches(r.getMatchesList())
            .build())
        .collect(toUnmodifiableList());

    var matches = matchFacade.createMatches(newAlertMatchesList);

    return BatchCreateMatchesResponse.newBuilder()
        .addAllMatches(matches)
        .build();
  }
}
