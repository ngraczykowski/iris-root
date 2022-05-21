package com.silenteight.hsbc.bridge.grpc;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.hsbc.bridge.alert.AlertServiceClient;
import com.silenteight.hsbc.bridge.alert.dto.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

class AlertServiceClientMock implements AlertServiceClient {

  @Override
  public BatchCreateAlertsResponseDto batchCreateAlerts(List<Alert> alerts) {
    var requestedAlerts = alerts.stream().map(a -> AlertDto.builder()
            .alertId(a.getAlertId())
            .name("alerts/" + UUID.randomUUID())
            .build())
        .collect(Collectors.toList());

    return BatchCreateAlertsResponseDto.builder()
        .alerts(requestedAlerts)
        .build();
  }

  @Override
  public BatchCreateAlertMatchesResponseDto batchCreateAlertMatches(
      BatchCreateAlertMatchesRequestDto request) {
    return BatchCreateAlertMatchesResponseDto.builder()
        .alertMatches(createAlertMatches(request.getMatchIds()))
        .build();
  }

  private List<AlertMatchDto> createAlertMatches(Collection<String> matchIds) {
    return matchIds.stream()
        .map(m -> AlertMatchDto.builder()
            .matchId(m)
            .name("alerts/" + UUID.randomUUID() + "/matches/" + UUID.randomUUID())
            .build())
        .collect(Collectors.toList());
  }
}
